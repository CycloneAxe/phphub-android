package org.estgroup.phphub.ui.view.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.EditUserProfilePresenter;

import butterknife.Bind;
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
        desView.setText(userInfo.getSignature());
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
}
