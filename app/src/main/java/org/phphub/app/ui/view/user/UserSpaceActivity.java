package org.phphub.app.ui.view.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.UserSpacePresenter;

import butterknife.Bind;
import icepick.State;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.*;
import static org.phphub.app.common.Constant.USER_ID_KEY;

@RequiresPresenter(UserSpacePresenter.class)
public class UserSpaceActivity extends BaseActivity<UserSpacePresenter> {
    private static final String INTENT_EXTRA_PARAM_USER_ID = "user_id";

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    @State
    int userId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getIntExtra(INTENT_EXTRA_PARAM_USER_ID, 0);
        if (userId <= 0) {
            return;
        }

        getPresenter().request(userId);
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

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.auth_account_type));

        String loginUserId = "";

        for (Account account : accounts) {
            loginUserId = accountManager.getUserData(account, USER_ID_KEY);
        }

        int userId = getIntent().getIntExtra(INTENT_EXTRA_PARAM_USER_ID, 0);

        isMySelf = loginUserId.equals(String.valueOf(userId));

        if (!isMySelf) {
            menu.findItem(R.id.menu_edit).setVisible(false);
        }

        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.user_space;
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }

    public void initView(User userInfo) {

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
