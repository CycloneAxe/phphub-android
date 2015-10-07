package org.phphub.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.phphub.app.R;
import org.phphub.app.common.base.BaseSupportFragment;

import butterknife.OnClick;

public class MeFragment extends BaseSupportFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.percent_rlyt_settings)
    public void navigateToSettings() {
        navigator.navigateToSettings(getActivity());
    }

    @Override
    protected String getTitle() {
        return getString(R.string.me);
    }
}
