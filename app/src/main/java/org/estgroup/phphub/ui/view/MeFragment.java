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
import com.squareup.otto.Subscribe;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseSupportFragment;
import org.estgroup.phphub.common.event.NotificationChangeEvent;
import org.estgroup.phphub.common.provider.BusProvider;
import org.estgroup.phphub.common.util.Utils;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeRelativeLayout;
import eu.unicate.retroauth.AuthAccountManager;

import static org.estgroup.phphub.common.Constant.*;
import static org.estgroup.phphub.common.qualifier.UserTopicType.*;

public class MeFragment extends BaseSupportFragment {

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_username)
    TextView usernameView;

    @Bind(R.id.tv_sign)
    TextView signView;

    @Bind(R.id.bga_rlyt_messages)
    BGABadgeRelativeLayout messagesView;

    AccountManager accountManager;

    AuthAccountManager authAccountManager;

    String accountType, tokenType;

    Account account;

    int userId;

    String userReplyUrl;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accountManager = AccountManager.get(getContext());
        authAccountManager = new AuthAccountManager(getContext(), accountManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        account = Utils.getActiveAccount(getContext(), authAccountManager);
        refreshView();
    }

    private void refreshView() {
        userId = -1;
        userReplyUrl = "";
        String avatarUrl = null, username = null, signature = null;
        signView.setVisibility(View.GONE);

        if (account != null) {
            String id = accountManager.getUserData(account, USER_ID_KEY);
            if (!TextUtils.isEmpty(id)) {
                userId = Integer.valueOf(id);
            }

            username = accountManager.getUserData(account, USERNAME_KEY);
            signature = accountManager.getUserData(account, USER_SIGNATURE);
            avatarUrl = accountManager.getUserData(account, USER_AVATAR_KEY);
            userReplyUrl = accountManager.getUserData(account, USER_REPLY_URL_KEY);

            if (!TextUtils.isEmpty(signature)) {
                signView.setVisibility(View.VISIBLE);
                signView.setText(signature);
            }
        }

        avatarView.setImageURI(!TextUtils.isEmpty(avatarUrl) ? Uri.parse(avatarUrl) : null);
        usernameView.setText(!TextUtils.isEmpty(username) ? username : "未登陆");
    }

    @OnClick(R.id.percent_rlyt_settings)
    public void navigateToSettings() {
        navigator.navigateToSettings(getActivity());
    }

    @OnClick(R.id.user_container)
    public void navigateToUserSpace() {
        if (userId <= 0) {
            needLogin();
            return;
        }
        navigator.navigateToUserSpace(getContext(), this.userId);
    }

    @OnClick(R.id.percent_rlyt_replys)
    public void navigateToUserReplys() {
        if (userId <= 0) {
            needLogin();
            return;
        }
        navigator.navigateToUserReply(getContext(), this.userReplyUrl);
    }

    @OnClick(R.id.percent_rlyt_topics)
    public void navigateToUserTopic() {
        if (userId <= 0) {
            needLogin();
            return;
        }
        navigator.navigateToUserTopic(getContext(), this.userId, USER_TOPIC_TYPE);
    }

    @OnClick(R.id.percent_rlyt_following)
    public void navigateToUserFollow() {
        if (userId <= 0) {
            needLogin();
            return;
        }
        navigator.navigateToUserTopic(getContext(), this.userId, USER_TOPIC_FOLLOW_TYPE);
    }

    @OnClick(R.id.percent_rlyt_favorites)
    public void navigateToUserFavorite() {
        if (userId <= 0) {
            needLogin();
            return;
        }
        navigator.navigateToUserTopic(getContext(), this.userId, USER_TOPIC_FAVORITE_TYPE);
    }

    private void needLogin() {
        authAccountManager.addAccount(getActivity(), accountType, tokenType);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.me);
    }

    @Subscribe
    public void onNotificationChange(NotificationChangeEvent event) {
        if (messagesView == null) {
            return;
        }

        int lenght = event.getNotificationLength();

        if (lenght > 0) {
            messagesView.showTextBadge(String.valueOf(lenght));
        } else {
            messagesView.hiddenBadge();
        }
    }
}