package org.phphub.app.common;

import android.content.Context;
import android.content.Intent;

import org.phphub.app.ui.view.settings.SettingsActivity;
import org.phphub.app.ui.view.topic.TopicDetailsActivity;
import org.phphub.app.ui.view.user.UserSpaceActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {
    @Inject
    public Navigator() {}

    public void navigateToTopicDetails(Context context, int topicId) {
        if (context == null) {
            return;
        }

        Intent intentToLaunch = TopicDetailsActivity.getCallingIntent(context, topicId);
        context.startActivity(intentToLaunch);
    }

    public void navigateToSettings(Context context) {
        if (context == null) {
            return;
        }

        Intent intentToLaunch = SettingsActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

    public void navigateToUserSpace(Context context, int userId) {
        if (context == null) {
            return;
        }

        Intent intentToLaunch = UserSpaceActivity.getCallingIntent(context, userId);
        context.startActivity(intentToLaunch);
    }
}