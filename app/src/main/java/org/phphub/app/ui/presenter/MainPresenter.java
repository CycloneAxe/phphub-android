package org.phphub.app.ui.presenter;

import android.os.Bundle;

import org.phphub.app.api.entity.element.Token;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.model.TokenModel;
import org.phphub.app.ui.view.MainActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;

public class MainPresenter extends BaseRxPresenter<MainActivity> {
    public static final int REQUEST = 1;

    @Inject
    TokenModel tokenModel;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(REQUEST,
                new Func0<Observable<Token>>() {
                    @Override
                    public Observable<Token> call() {
                        return tokenModel.tokenGenerator();
                    }
                },
                new Action2<MainActivity, Token>() {
                    @Override
                    public void call(MainActivity mainActivity, Token token) {
                        mainActivity.storageClientToken(token);
                    }
                });
    }

    public void request() {
        start(REQUEST);
    }
}
