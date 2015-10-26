package org.estgroup.phphub.ui.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.levelmoney.velodrome.Velodrome;
import com.levelmoney.velodrome.annotations.OnActivityResult;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.UserEntity;
import org.estgroup.phphub.api.entity.element.Token;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.App;
import org.estgroup.phphub.common.Navigator;
import org.estgroup.phphub.common.service.NotificationService;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.UserModel;

import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import eu.unicate.retroauth.AuthenticationActivity;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

import static org.estgroup.phphub.common.Constant.LOGIN_TOKEN_KEY;
import static org.estgroup.phphub.common.Constant.USERNAME_KEY;
import static org.estgroup.phphub.common.Constant.USER_AVATAR_KEY;
import static org.estgroup.phphub.common.Constant.USER_ID_KEY;
import static org.estgroup.phphub.common.Constant.USER_SIGNATURE;
import static org.estgroup.phphub.common.Constant.USER_REPLY_URL_KEY;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_GUEST;
import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_REFRESH;

public class LoginActivity extends AuthenticationActivity {
    private final static int CODE_SCANNER = 100;

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    @Inject
    Navigator navigator;

    @Inject
    TokenModel tokenModel;

    @Inject
    UserModel userModel;

    @Inject
    AccountManager accountManager;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        initializeToolbar();
        ((App) getApplication()).getApiComponent().inject(this);

        if (accountManager.getAccountsByType(getString(R.string.auth_account_type)).length > 0) {
            Toast.makeText(this, "只能有一个账号", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Velodrome.handleResult(this, requestCode, resultCode, data);
    }

    protected void initializeToolbar() {
        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleView.setText(R.string.please_login);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @OnClick(R.id.btn_login_guide)
    public void loginGuide() {
        Uri uri = Uri.parse("http://7xnqwn.com1.z0.glb.clouddn.com/index.html");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            startActivity(Intent.createChooser(intent, "请选择浏览器"));
        }
    }

    @OnClick(R.id.btn_scanner)
    public void navigateToScanner() {
        navigator.navigateToScanner(this, CODE_SCANNER);
    }

    @OnActivityResult(CODE_SCANNER)
    public void onScanner(Intent data) {
        final String username = data.getStringExtra(USERNAME_KEY),
                loginToken = data.getStringExtra(LOGIN_TOKEN_KEY);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(loginToken)) {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("登陆中");
        dialog.setCancelable(false);

        final Account account = createOrGetAccount(username);
        subscription = tokenModel.tokenGenerator(username, loginToken)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        dialog.show();
                    }
                })
                .doOnNext(new Action1<Token>() {
                    @Override
                    public void call(Token token) {
                    }
                })
                .flatMap(new Func1<Token, Observable<User>>() {
                    @Override
                    public Observable<User> call(final Token token) {
                        return ((UserModel) userModel.once()
                                .setToken(token.getToken()))
                                .getMyselfInfo()
                                .map(new Func1<UserEntity.AUser, User>() {
                                    @Override
                                    public User call(UserEntity.AUser user) {
                                        return user.getData();
                                    }
                                })
                                .doOnNext(new Action1<User>() {
                                    @Override
                                    public void call(User user) {
                                        storeToken(account, getString(R.string.auth_token_type), token.getToken());
                                        storeUserData(account, AUTH_TYPE_REFRESH, token.getRefreshToken());
                                        storeUserData(account, USER_ID_KEY, String.valueOf(user.getId()));
                                        storeUserData(account, USERNAME_KEY, user.getName());
                                        storeUserData(account, USER_SIGNATURE, user.getSignature());
                                        storeUserData(account, USER_AVATAR_KEY, user.getAvatar());
                                        storeUserData(account, USER_REPLY_URL_KEY, user.getLinks().getRepliesWebView());

                                        JPushInterface.setAlias(getApplicationContext(), "userid_" + user.getId(), new TagAliasCallback() {
                                            @Override
                                            public void gotResult(int i, String s, Set<String> set) {

                                            }
                                        });
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                               @Override
                               public void call(User user) {
                                   dialog.dismiss();
                                   startService(new Intent(LoginActivity.this, NotificationService.class));
                                   finalizeAuthentication(account);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                Logger.e(throwable.toString());
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}