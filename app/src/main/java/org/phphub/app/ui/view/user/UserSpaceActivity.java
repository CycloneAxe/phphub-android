package org.phphub.app.ui.view.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.phphub.app.R;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.UserSpacePresenter;

import icepick.State;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(UserSpacePresenter.class)
public class UserSpaceActivity extends BaseActivity<UserSpacePresenter> {
    private static final String INTENT_EXTRA_PARAM_USER_ID = "user_id";

    @State
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getIntExtra(INTENT_EXTRA_PARAM_USER_ID, 0);
        if (userId <= 0) {
            return;
        }
    }

    public static Intent getCallingIntent(Context context, int userId) {
        Intent callingIntent = new Intent(context, UserSpaceActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_USER_ID, userId);
        return callingIntent;
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<UserSpacePresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<UserSpacePresenter>() {
            @Override
            public UserSpacePresenter createPresenter() {
                UserSpacePresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.user_space;
    }
}
