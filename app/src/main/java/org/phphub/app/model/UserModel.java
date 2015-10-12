package org.phphub.app.model;

import android.content.Context;

import org.phphub.app.api.UserApi;
import org.phphub.app.api.entity.UserEntity;
import org.phphub.app.common.base.BaseModel;

import rx.Observable;

public class UserModel extends BaseModel<UserApi> {
    public UserModel(Context context, boolean injectGuestToken) {
        super(context, injectGuestToken, UserApi.class);
    }

    public Observable<UserEntity.AUser> getMyselfInfo() {
        return service.getMyselfInfo();
    }

    public Observable<UserEntity.AUser> getUserInfo(int userId) {
        return service.getUserInfo(userId);
    }
}
