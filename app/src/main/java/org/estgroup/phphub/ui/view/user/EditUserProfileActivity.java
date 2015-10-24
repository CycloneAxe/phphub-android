package org.estgroup.phphub.ui.view.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.EditUserProfilePresenter;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(EditUserProfilePresenter.class)
public class EditUserProfileActivity extends BaseActivity<EditUserProfilePresenter> {
    private static final String USER_INFO = "user_info";

    int userId;

    User userInfo;

    @Bind(R.id.et_username)
    EditText userNameView;

    @Bind(R.id.et_address)
    EditText addressView;

    @Bind(R.id.et_twitter)
    EditText twitterView;

    @Bind(R.id.et_github)
    EditText githubView;

    @Bind(R.id.et_blog)
    EditText blogView;

    @Bind(R.id.et_description)
    EditText desView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfo = (User) getIntent().getSerializableExtra(USER_INFO);
        if (userInfo.getId() > 0) {
            this.userId = userInfo.getId();
        }

        userNameView.setText(userInfo.getName());
        addressView.setText(userInfo.getCity());
        twitterView.setText(userInfo.getTwitterAccount());
        githubView.setText(userInfo.getGithubName());
        blogView.setText(userInfo.getPersonalWebsite());
        desView.setText(userInfo.getIntroduction());
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<EditUserProfilePresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<EditUserProfilePresenter>() {
            @Override
            public EditUserProfilePresenter createPresenter() {
                EditUserProfilePresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);

                return presenter;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_save_edit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                verifyProfile();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void verifyProfile() {
        SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        SweetAlertDialog loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        String username = userNameView.getText().toString();
        String city = addressView.getText().toString();
        String twitter = twitterView.getText().toString();
        String github = githubView.getText().toString();
        String blog = blogView.getText().toString();
        String des = desView.getText().toString();

        if (TextUtils.isEmpty(username)) {
            errorDialog.setTitleText("Oops...");
            errorDialog.setContentText(getString(R.string.username_error));
            errorDialog.show();
            return;
        }

        userInfo.setName(username);
        userInfo.setCity(city);
        userInfo.setTwitterAccount(twitter);
        userInfo.setGithubName(github);
        userInfo.setPersonalWebsite(blog);
        userInfo.setIntroduction(des);

        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#4394DA"));
        loadingDialog.setContentText(getString(R.string.submitting));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        getPresenter().request(userInfo);

        loadingDialog.dismiss();
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.edit_profile);
    }

    public static Intent getCallingIntent(Context context, User userInfo) {
        Intent callingIntent = new Intent(context, EditUserProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_INFO, userInfo);
        callingIntent.putExtras(bundle);

        return callingIntent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.edit_user_profile;
    }

    public void onSaveSuccessful(User user) {
        finish();
        navigator.navigateToUserSpace(this, user.getId());
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
        SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        errorDialog.setTitleText("Oops...");
        errorDialog.setContentText(getString(R.string.publish_error));
        errorDialog.show();
    }
}
