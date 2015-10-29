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
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.ui.presenter.UserSpacePresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import eu.unicate.retroauth.AuthAccountManager;
import icepick.State;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.*;
import static org.estgroup.phphub.common.Constant.USER_ID_KEY;
import static org.estgroup.phphub.common.qualifier.UserTopicType.*;

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

    @Bind(R.id.percent_rlyt_topics)
    PercentRelativeLayout topicsView;

    @Bind(R.id.percent_rlyt_following)
    PercentRelativeLayout followView;

    @Bind(R.id.percent_rlyt_replys)
    PercentRelativeLayout replysView;

    @Bind(R.id.percent_rlyt_favorites)
    PercentRelativeLayout favoritesView;

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
        descriptionView.setText(TextUtils.isEmpty(userInfo.getIntroduction()) ? getString(R.string.empty_content) : userInfo.getIntroduction());
        addressView.setText(TextUtils.isEmpty(userInfo.getCity()) ? getString(R.string.empty_content) : userInfo.getCity());
        githubView.setText(TextUtils.isEmpty(userInfo.getGithubName()) ? getString(R.string.empty_content) : userInfo.getGithubName());
        twitterView.setText(TextUtils.isEmpty(userInfo.getTwitterAccount()) ? getString(R.string.empty_content) : userInfo.getTwitterAccount());
        blogView.setText(TextUtils.isEmpty(userInfo.getPersonalWebsite()) ? getString(R.string.empty_content) : userInfo.getPersonalWebsite());
        sinceView.setText(userInfo.getCreatedAt());
    }

    @OnClick(R.id.percent_llyt_github)
    public void navigateToGitHubView(){
        if (!TextUtils.isEmpty(userInfo.getGithubName())) {
            navigator.navigateToWebView(this, userInfo.getGithubUrl());
        }
    }

    @OnClick(R.id.percent_llyt_blog)
    public void navigateToBlogView(){
        if (!TextUtils.isEmpty(userInfo.getPersonalWebsite())) {
            navigator.navigateToWebView(this, "http://" + userInfo.getPersonalWebsite());
        }
    }

    @OnClick(R.id.percent_rlyt_topics)
    public void navigateToTopics() {
        navigator.navigateToUserTopic(this, userId, USER_TOPIC_TYPE);
    }

    @OnClick(R.id.percent_rlyt_favorites)
    public void navigateToFavorites() {
        navigator.navigateToUserTopic(this, userId, USER_TOPIC_FAVORITE_TYPE);
    }

    @OnClick(R.id.percent_rlyt_following)
    public void navigateToFollow() {
        navigator.navigateToUserTopic(this, userId, USER_TOPIC_FOLLOW_TYPE);
    }

    @OnClick(R.id.percent_rlyt_replys)
    public void navigateToReplys() {
        navigator.navigateToUserReply(this, userInfo.getLinks().getRepliesWebView());
    }
}
