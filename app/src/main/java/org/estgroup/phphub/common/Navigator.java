package org.estgroup.phphub.common;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.ui.view.GalleryActivity;
import org.estgroup.phphub.ui.view.ScannerActivity;
import org.estgroup.phphub.ui.view.WebViewPageActivity;
import org.estgroup.phphub.ui.view.settings.SettingsActivity;
import org.estgroup.phphub.ui.view.topic.TopicPublishActivity;
import org.estgroup.phphub.ui.view.topic.TopicDetailsActivity;
import org.estgroup.phphub.ui.view.topic.TopicReplyActivity;
import org.estgroup.phphub.ui.view.ReplyListActivity;
import org.estgroup.phphub.ui.view.user.EditUserProfileActivity;
import org.estgroup.phphub.ui.view.user.UserNotificationsActivity;
import org.estgroup.phphub.ui.view.user.UserSpaceActivity;
import org.estgroup.phphub.ui.view.user.UserTopicActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {
    @Inject
    public Navigator() {}

    public void navigateToTopicDetails(@NonNull Context context, int topicId) {
        Intent intentToLaunch = TopicDetailsActivity.getCallingIntent(context, topicId);
        context.startActivity(intentToLaunch);
    }

    public void navigateToSettings(@NonNull Context context) {
        Intent intentToLaunch = SettingsActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

    public void navigateToUserSpace(@NonNull Context context, int userId) {
        Intent intentToLaunch = UserSpaceActivity.getCallingIntent(context, userId);
        context.startActivity(intentToLaunch);
    }

    public void navigateToScanner(@NonNull Context context, int requestCode) {
        Intent intentToLaunch = ScannerActivity.getCallingIntent(context);
        ((FragmentActivity) context).startActivityForResult(intentToLaunch, requestCode);
    }

    public void navigateToPublishTopic(@NonNull Context context) {
        Intent intentToLaunch = TopicPublishActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

    public void navigateToReplyTopic(@NonNull Context context, int topicId, String replyUrl){
        Intent intentToLaunch = TopicReplyActivity.getCallingIntent(context, topicId, replyUrl);
        context.startActivity(intentToLaunch);
    }

    public void navigateToUserReply(@NonNull Context context, String replyUrl){
        Intent intentToLaunch = ReplyListActivity.getCallingIntent(context, replyUrl);
        context.startActivity(intentToLaunch);
    }

    public void navigateToUserReply(@NonNull Context context, int topicId, String replyUrl){
        Intent intentToLaunch = ReplyListActivity.getCallingIntent(context, topicId, replyUrl);
        context.startActivity(intentToLaunch);
    }

    public void navigateToUserTopic(@NonNull Context context, int userId, String userTopicType) {
        Intent intentToLaunch = UserTopicActivity.getCallingIntent(context, userId, userTopicType);
        context.startActivity(intentToLaunch);
    }

    public void navigateToEditUserProfile(@NonNull Context context, User userInfo) {
        Intent intentToLaunch = EditUserProfileActivity.getCallingIntent(context, userInfo);
        context.startActivity(intentToLaunch);
    }

    public void navigateToUserNotification(@NonNull Context context) {
        Intent intentToLaunch = UserNotificationsActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

    public void navigateToWebView(@NonNull Context context, String webUrl) {
        Intent intentToLaunch = WebViewPageActivity.getCallingIntent(context, webUrl);
        context.startActivity(intentToLaunch);
    }

    public void navigateToGallery(@NonNull Context context, String imageUrl) {
        Intent intentToLaunch = GalleryActivity.getCallingIntent(context, imageUrl);
        context.startActivity(intentToLaunch);
    }
}