package org.estgroup.phphub.ui.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseSupportFragment;

import butterknife.Bind;
import butterknife.OnClick;
import static org.estgroup.phphub.common.Constant.*;
import static org.estgroup.phphub.common.qualifier.UserTopicType.*;

public class MeFragment extends BaseSupportFragment {

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_username)
    TextView usernameView;

    @Bind(R.id.tv_sign)
    TextView signView;

    AccountManager accountManager;

    String accountType, tokenType;

    Account[] accounts;

    int userId;

    String avatarUrl, username, signature, userReplyUrl;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accountManager = AccountManager.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        accounts = accountManager.getAccountsByType(accountType);
        if (accounts.length > 0) {
            userId = Integer.valueOf(accountManager.getUserData(accounts[0], USER_ID_KEY));
            username = accountManager.getUserData(accounts[0], USERNAME_KEY);
            signature = accountManager.getUserData(accounts[0], USER_SIGNATURE);
            avatarUrl = accountManager.getUserData(accounts[0], USER_AVATAR_KEY);
            userReplyUrl = accountManager.getUserData(accounts[0], USER_REPLY_URL_KEY);
        }
        /**
         * TODO
         * 需要在用户退出登录的时候还原成未登陆状态
         */
        refreshView();
    }

    private void refreshView() {
        if (!TextUtils.isEmpty(avatarUrl)) {
            avatarView.setImageURI(Uri.parse(avatarUrl));
        }
        usernameView.setText(username != null ? username : "未登陆");
        signView.setText(signature);
    }

    @OnClick(R.id.percent_rlyt_settings)
    public void navigateToSettings() {
        navigator.navigateToSettings(getActivity());
    }

    @OnClick(R.id.user_container)
    public void navigateToUserSpace() {
        if (userId > 0) {
            navigator.navigateToUserSpace(getContext(), this.userId);
        }
    }

    @OnClick(R.id.percent_rlyt_replys)
    public void navigateToUserReplys() {
        if (userId > 0) {
            navigator.navigateToUserReply(getContext(), this.userReplyUrl);
        }
    }

    @OnClick(R.id.percent_rlyt_topics)
    public void navigateToUserTopic() {
        if (userId > 0) {
            navigator.navigateToUserTopic(getContext(), this.userId, USER_TOPIC_TYPE);
        }
    }

    @OnClick(R.id.percent_rlyt_following)
    public void navigateToUserFollow() {
        if (userId > 0) {
            navigator.navigateToUserTopic(getContext(), this.userId, USER_TOPIC_FOLLOW_TYPE);
        }
    }

    @OnClick(R.id.percent_rlyt_favorites)
    public void navigateToUserFavorite() {
        if (userId > 0) {
            navigator.navigateToUserTopic(getContext(), this.userId, USER_TOPIC_FAVORITE_TYPE);
        }
    }

    @Override
    protected String getTitle() {
        return getString(R.string.me);
    }
}
