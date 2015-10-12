package org.phphub.app.ui.view.user;

import android.content.Context;
import android.content.Intent;

import org.phphub.app.R;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.UserSpacePresenter;

import nucleus.factory.RequiresPresenter;

@RequiresPresenter(UserSpacePresenter.class)
public class UserSpaceActivity extends BaseActivity<UserSpacePresenter> {
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserSpaceActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.user_space;
    }
}
