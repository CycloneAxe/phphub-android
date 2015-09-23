package org.phphub.app.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.phphub.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusSupportFragment;

public abstract class BaseSupportFragment<PresenterType extends Presenter> extends NucleusSupportFragment<PresenterType> {
    @Nullable
    @Bind(R.id.tv_title)
    TextView titleView;

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Icepick.saveInstanceState(this, bundle);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this, view);

        if (titleView != null &&!TextUtils.isEmpty(getTitle())) {
            titleView.setText(getTitle());
        }
    }

    protected String getTitle() {
        return "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}