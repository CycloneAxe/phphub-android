package org.estgroup.phphub.ui.view.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.ui.presenter.UserSpacePresenter;

import javax.inject.Inject;

import butterknife.Bind;
import eu.unicate.retroauth.AuthAccountManager;
import icepick.State;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.*;
import static org.estgroup.phphub.common.Constant.USER_ID_KEY;

@DeepLink("phphub://users")
@RequiresPresenter(UserSpacePresenter.class)
public class UserSpaceActivity extends BaseActivity<UserSpacePresenter> {
    private static final String INTENT_EXTRA_PARAM_USER_ID = "user_id";

    private static final String INTENT_EXTRA_DEEPLINK_PARAM_ID = "id";

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    @State
    int userId;

    User userInfo;

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_username)
    TextView userNameView;

    @Bind(R.id.tv_realname)
    TextView realNameView;

    @Bind(R.id.tv_description)
    TextView descriptionView;

    @Bind(R.id.tv_address)
    TextView addressView;

    @Bind(R.id.tv_github)
    TextView githubView;

    @Bind(R.id.tv_twitter)
    TextView twitterView;

    @Bind(R.id.tv_blog)
    TextView blogView;

    @Bind(R.id.tv_since)
    TextView sinceView;

    @Bind(R.id.percent_llyt_others)
    PercentLinearLayout othersView;

    boolean isMySelf;

    @Inject
    AccountManager accountManager;

    @Inject
    AuthAccountManager authAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle params = intent.getExtras();
            if (params != null && params.getString(INTENT_EXTRA_DEEPLINK_PARAM_ID) != null) {
                String value = params.getString(INTENT_EXTRA_DEEPLINK_PARAM_ID);
                if (!TextUtils.isEmpty(value)) {
                    userId = Integer.valueOf(value);
                }
            }
        } else {
            userId = intent.getIntExtra(INTENT_EXTRA_PARAM_USER_ID, 0);
        }

        Logger.d("user id : %d", userId);

        if (userId >0) {
            getPresenter().request(userId);
        }
    }

    public static Intent getCallingIntent(Context context, int userId) {
        Intent callingIntent = new Intent(context, UserSpaceActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_USER_ID, userId);
        return callingIntent;
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<UserSpacePresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<UserSpacePresenter>() {
            @Override
            public UserSpacePresenter createPresenter() {
                UserSpacePresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_user_space, menu);

        if (Utils.logined(this, accountManager)) {
            Account account = Utils.getActiveAccount(this, authAccountManager);
            String loginUserId = accountManager.getUserData(account, USER_ID_KEY);
            if (!TextUtils.isEmpty(loginUserId) && userId > 0) {
                isMySelf = loginUserId.equals(String.valueOf(userId));
            }
        }

        if (!isMySelf) {
            menu.findItem(R.id.menu_edit).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                navigator.navigateToEditUserProfile(this, userInfo);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.user_space;
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }

    public void initView(User userInfo) {
        this.userInfo = userInfo;

        if (!isMySelf) {
            othersView.setVisibility(VISIBLE);
        }

        avatarView.setImageURI(Uri.parse(userInfo.getAvatar()));
        userNameView.setText(userInfo.getName());
        realNameView.setText(userInfo.getRealName());
        descriptionView.setText(userInfo.getIntroduction());
        addressView.setText(userInfo.getCity());
        githubView.setText(userInfo.getGithubName());
        twitterView.setText(userInfo.getTwitterAccount());
        blogView.setText(userInfo.getPersonalWebsite());
        sinceView.setText(userInfo.getCreatedAt());
    }
}
