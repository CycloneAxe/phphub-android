package org.estgroup.phphub.ui.presenter;

import android.os.Bundle;

import org.estgroup.phphub.api.entity.UserEntity;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.model.UserModel;
import org.estgroup.phphub.ui.view.user.EditUserProfileActivity;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_USER;

public class EditUserProfilePresenter extends BaseRxPresenter<EditUserProfileActivity> {
    private static final int REQUEST_EDIT_ID = 1;

    User userInfo;

    @Inject
    @Named(AUTH_TYPE_USER)
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_EDIT_ID,
                new Func0<Observable<User>>() {
                    @Override
                    public Observable<User> call() {
                        return userModel.saveUserProfile(userInfo)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<UserEntity.AUser, User>() {
                                    @Override
                                    public User call(UserEntity.AUser aUser) {
                                        return aUser.getData();
                                    }
                                });
                    }
                },
                new Action2<EditUserProfileActivity, User>() {
                    @Override
                    public void call(EditUserProfileActivity editUserProfileActivity, User user) {
                        editUserProfileActivity.onSaveSuccessful(user);
                    }
                },
                new Action2<EditUserProfileActivity, Throwable>() {
                    @Override
                    public void call(EditUserProfileActivity editUserProfileActivity, Throwable throwable) {
                        editUserProfileActivity.onNetWorkError(throwable);
                    }
                });

    }

    public void request(User user) {
        if (user.getId() > 0) {
            this.userInfo = user;
            start(REQUEST_EDIT_ID);
        }

    }

}
