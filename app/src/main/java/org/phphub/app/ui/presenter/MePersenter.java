package org.phphub.app.ui.presenter;

import android.os.Bundle;

import org.phphub.app.api.entity.UserEntity;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.model.UserModel;
import org.phphub.app.ui.view.MeFragment;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

import static org.phphub.app.common.qualifier.AuthType.AUTH_TYPE_GUEST;
import static org.phphub.app.common.qualifier.AuthType.AUTH_TYPE_USER;

public class MePersenter extends BaseRxPresenter<MeFragment> {
    final static int REQUEST_ME_ID = 1;

    @Inject
    @Named(AUTH_TYPE_USER)
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_ME_ID,
                new Func0<Observable<User>>() {
                    @Override
                    public Observable<User> call() {
                        return userModel.getMyselfInfo()
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<UserEntity.AUser, User>() {
                                    @Override
                                    public User call(UserEntity.AUser aUser) {
                                        return aUser.getData();
                                    }
                                });
                    }
                },
                new Action2<MeFragment, User>() {
                    @Override
                    public void call(MeFragment meFragment, User user) {
                        meFragment.initView(user);
                    }
                },
                new Action2<MeFragment, Throwable>() {
                    @Override
                    public void call(MeFragment meFragment, Throwable throwable) {
                        meFragment.onNetworkError(throwable);
                    }
                });
    }

    public void request() {
        start(REQUEST_ME_ID);
    }
}
