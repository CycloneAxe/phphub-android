package org.phphub.app.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import org.phphub.app.R;
import org.phphub.app.common.App;
import org.phphub.app.common.Navigator;
import org.phphub.app.common.internal.di.component.AppComponent;
import org.phphub.app.common.internal.di.module.ActivityModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;

public abstract class BaseActivity<PresenterType extends Presenter> extends NucleusAppCompatActivity<PresenterType> {
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Nullable
    @Bind(R.id.tv_title)
    TextView titleView;

    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(getLayoutResId());
        setupToolbar();
        navigator = getAppComponent().navigator();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    protected void setupToolbar() {
        if (toolbarView == null) {
            return;
        }
        toolbarView.setTitle("");
        setSupportActionBar(toolbarView);
        if (titleView != null) {
            titleView.setText(getTitleName());
        }
        if (!TextUtils.isEmpty(NavUtils.getParentActivityName(this))) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected CharSequence getTitleName() {
        return getTitle();
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    abstract protected @LayoutRes int getLayoutResId();
}