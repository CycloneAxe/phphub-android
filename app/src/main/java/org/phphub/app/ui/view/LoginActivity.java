package org.phphub.app.ui.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.levelmoney.velodrome.Velodrome;
import com.levelmoney.velodrome.annotations.OnActivityResult;
import com.orhanobut.logger.Logger;

import static org.phphub.app.common.Constant.*;

import org.phphub.app.R;
import org.phphub.app.api.entity.UserEntity;
import org.phphub.app.api.entity.element.Token;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.App;
import org.phphub.app.common.Navigator;
import org.phphub.app.model.TokenModel;
import org.phphub.app.model.UserModel;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.unicate.retroauth.AuthenticationActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

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

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        AccountManager accountManager = AccountManager.get(this);
        if (accountManager.getAccountsByType(getString(R.string.auth_account_type)).length > 0) {
            Toast.makeText(this, "只能有一个账号", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeToolbar();
        ((App) getApplication()).getApiComponent().inject(this);
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
        dialog.show();

        final Account account = createOrGetAccount(username);
        tokenModel.tokenGenerator(username, loginToken)
                .doOnNext(new Action1<Token>() {
                    @Override
                    public void call(Token token) {
                        storeToken(account, getString(R.string.auth_token_type), token.getToken());
                    }
                })
                .flatMap(new Func1<Token, Observable<User>>() {
                    @Override
                    public Observable<User> call(final Token token) {
                        return userModel.getMyselfInfo()
                                .map(new Func1<UserEntity.AUser, User>() {
                                    @Override
                                    public User call(UserEntity.AUser user) {
                                        return user.getData();
                                    }
                                })
                                .doOnNext(new Action1<User>() {
                                    @Override
                                    public void call(User user) {
                                        storeUserData(account, USER_ID_KEY, String.valueOf(user.getId()));
                                        storeUserData(account, USER_AVATAR_KEY, user.getAvatar());
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                               @Override
                               public void call(User user) {
                                   dialog.dismiss();
                                   finalizeAuthentication(account);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Logger.e(throwable.toString());
                            }
                        });
    }
}