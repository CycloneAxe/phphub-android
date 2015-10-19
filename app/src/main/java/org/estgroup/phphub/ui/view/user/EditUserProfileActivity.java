package org.estgroup.phphub.ui.view.user;

import android.content.Context;
import android.content.Intent;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.EditUserProfilePresenter;

import nucleus.factory.RequiresPresenter;

@RequiresPresenter(EditUserProfilePresenter.class)
public class EditUserProfileActivity extends BaseActivity<EditUserProfilePresenter> {
    private static final String USER_ID = "user_id";

    public static Intent getCallingIntent(Context context, int userId) {
        Intent callingIntent = new Intent(context, EditUserProfileActivity.class);
        callingIntent.putExtra(USER_ID, userId);

        return callingIntent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.edit_user_profile;
    }
}
