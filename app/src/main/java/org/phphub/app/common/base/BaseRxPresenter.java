package org.phphub.app.common.base;

import android.os.Bundle;

import icepick.Icepick;
import nucleus.presenter.RxPresenter;

public class BaseRxPresenter<View> extends RxPresenter<View> {
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }
}