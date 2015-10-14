package org.phphub.app.ui.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.base.BaseSupportFragment;
import org.phphub.app.ui.presenter.MePersenter;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MePersenter.class)
public class MeFragment extends BaseSupportFragment<MePersenter>  {

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_username)
    TextView userNameView;

    @Bind(R.id.tv_sign)
    TextView signView;

    int userId;
    String userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me, container, false);
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<MePersenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<MePersenter>() {
            @Override
            public MePersenter createPresenter() {
                MePersenter persenter = superFactory.createPresenter();
                getApiComponent().inject(persenter);
                return persenter;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPresenter().request();
    }

    public void initView(User userInfo) {
        this.userId = userInfo.getId();
        this.userName = userInfo.getName();

        avatarView.setImageURI(Uri.parse(userInfo.getAvatar()));
        userNameView.setText(userInfo.getName());
        signView.setText(userInfo.getSignature());
    }

    @OnClick(R.id.percent_rlyt_settings)
    public void navigateToSettings() {
        navigator.navigateToSettings(getActivity());
    }

    @OnClick(R.id.user_container)
    public void navigateToUserSpace() {
        navigator.navigateToUserSpace(getContext(), this.userId);
    }

    public void onNetworkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }

    @Override
    protected String getTitle() {
        return getString(R.string.me);
    }
}
