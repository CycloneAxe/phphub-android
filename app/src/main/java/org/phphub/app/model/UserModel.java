package org.phphub.app.model;

import android.content.Context;

import org.phphub.app.api.UserApi;
import org.phphub.app.api.entity.NotificationEntity;
import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.UserEntity;
import org.phphub.app.common.base.BaseModel;

import java.util.HashMap;

import rx.Observable;

public class UserModel extends BaseModel<UserApi> {
    public UserModel(Context context) {
        super(context, UserApi.class);
    }

    public Observable<UserEntity.AUser> getMyselfInfo() {
        return getService().getMyselfInfo();
    }

    public Observable<UserEntity.AUser> getUserInfo(int userId) {
        return getService().getUserInfo(userId);
    }

    public Observable<TopicEntity> getMyAttentions(int userId) {
        return getService().getMyAttentions(userId);
    }

    public Observable<TopicEntity> getMyFavorites(int userId) {
        return getService().getMyAttentions(userId);
    }

    public Observable<TopicEntity> getMyTopics(int userId) {
        return getService().getMyAttentions(userId);
    }

    public Observable<NotificationEntity> getMyNotifications() {
        HashMap<String, String> options = new HashMap<>();
        options.put("include", "from_user,topic");
        return getService().getMyNotifications(options);
    }
}
