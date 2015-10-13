package org.phphub.app.ui.presenter;

import android.os.Bundle;

import org.phphub.app.api.entity.UserEntity;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.model.UserModel;
import org.phphub.app.ui.view.user.UserSpaceActivity;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import static org.phphub.app.common.qualifier.AuthType.*;

public class UserSpacePresenter extends BaseRxPresenter<UserSpaceActivity> {
    private static final int REQUEST_ID = 1;

    @Inject
    @Named(AUTH_TYPE_GUEST)
    UserModel userModel;

    int userId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(REQUEST_ID,
                new Func0<Observable<User>>() {
                    @Override
                    public Observable<User> call() {
                        return userModel.getUserInfo(userId)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<UserEntity.AUser, User>() {
                                    @Override
                                    public User call(UserEntity.AUser user) {
                                        return user.getData();
                                    }
                                });
                    }
                },
                new Action2<UserSpaceActivity, User>() {
                    @Override
                    public void call(UserSpaceActivity userSpaceActivity, User user) {
                        userSpaceActivity.initView(user);
                    }
                },
                new Action2<UserSpaceActivity, Throwable>() {
                    @Override
                    public void call(UserSpaceActivity userSpaceActivity, Throwable throwable) {
                        userSpaceActivity.onNetWorkError(throwable);
                    }
                });
    }

    public void request(int userId) {
        this.userId = userId;
        start(REQUEST_ID);
    }
}