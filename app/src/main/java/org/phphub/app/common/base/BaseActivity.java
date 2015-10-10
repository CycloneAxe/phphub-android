package org.phphub.app.common.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.github.pwittchen.prefser.library.Prefser;
import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.common.App;
import org.phphub.app.common.Navigator;
import org.phphub.app.common.internal.di.component.ApiComponent;
import org.phphub.app.common.internal.di.component.AppComponent;
import org.phphub.app.common.internal.di.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;

import static org.phphub.app.common.Constant.GUEST_TOKEN;

public abstract class BaseActivity<PresenterType extends Presenter> extends NucleusAppCompatActivity<PresenterType> {
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Nullable
    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectorPresenter();
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(getLayoutResId());
        initializeToolbar();
        navigator = getAppComponent().navigator();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isChild()) {
            onTitleChanged(getTitleName(), getTitleColor());
        }
    }


    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (toolbarTitleView == null) {
            return;
        }
        toolbarTitleView.setText(title);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    protected void initializeToolbar() {
        if (toolbarView == null) {
            return;
        }
        setSupportActionBar(toolbarView);
        if (toolbarTitleView != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (!TextUtils.isEmpty(NavUtils.getParentActivityName(this))) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
        }
    }

    protected Map<String, String> getHttpHeaderAuth() {
        final Prefser prefser = new Prefser(this);
        String clientToken = prefser.get(GUEST_TOKEN, String.class, "");
        Map<String, String> map = new HashMap<String, String>();

        map.put("Authorization", "Bearer " + clientToken);

        return map;
    }

    protected CharSequence getTitleName() {
        return getTitle();
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    protected ApiComponent getApiComponent() {
        return ((App) getApplication()).getApiComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected void injectorPresenter() {}

    abstract protected @LayoutRes int getLayoutResId();
}