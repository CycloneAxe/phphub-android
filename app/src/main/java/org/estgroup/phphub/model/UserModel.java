package org.estgroup.phphub.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;

import org.estgroup.phphub.api.UserApi;
import org.estgroup.phphub.api.entity.NotificationEntity;
import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.UserEntity;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.Constant;
import org.estgroup.phphub.common.base.BaseModel;
import org.estgroup.phphub.common.provider.TokenProvider;

import java.util.HashMap;

import rx.Observable;

public class UserModel extends BaseModel<UserApi, UserModel> {
    public UserModel(@NonNull Context context, @Nullable TokenProvider tokenProvider) {
        super(context, tokenProvider);
    }

    @Override
    protected Class<UserApi> getServiceClass() {
        return UserApi.class;
    }

    public Observable<UserEntity.AUser> getMyselfInfo() {
        return getService().getMyselfInfo();
    }

    public Observable<UserEntity.AUser> getUserInfo(int userId) {
        return getService().getUserInfo(userId);
    }

    public Observable<TopicEntity> getAttentions(int userId) {
        HashMap<String, String> options = new HashMap<>();
        options.put("include", "user,node,last_reply_user");

        return getService().getAttentions(userId, options);
    }

    public Observable<TopicEntity> getFavorites(int userId) {
        HashMap<String, String> options = new HashMap<>();
        options.put("include", "user,node,last_reply_user");

        return getService().getFavorites(userId, options);
    }

    public Observable<TopicEntity> getTopics(int userId) {
        HashMap<String, String> options = new HashMap<>();
        options.put("include", "user,node,last_reply_user");

        return getService().getTopics(userId, options);
    }

    public Observable<NotificationEntity> getMyNotifications(int pageIndex) {
        HashMap<String, String> options = new HashMap<>();
        options.put("per_page", String.valueOf(Constant.PER_PAGE));
        options.put("include", "from_user,topic");
        options.put("page", String.valueOf(pageIndex));
        return getService().getMyNotifications(options);
    }

    public Observable<JsonObject> getUnreadNotifications() {
        return getService().getUnreadNotifications();
    }

    public Observable<UserEntity.AUser> saveUserProfile(User userInfo) {

        return getService().saveUserProfile(userInfo.getId(), userInfo);
    }
}
