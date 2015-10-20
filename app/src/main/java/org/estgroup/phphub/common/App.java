package org.estgroup.phphub.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.pwittchen.prefser.library.Prefser;
import com.squareup.leakcanary.LeakCanary;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.common.internal.di.component.ApiComponent;
import org.estgroup.phphub.common.internal.di.component.AppComponent;
import org.estgroup.phphub.common.internal.di.component.DaggerApiComponent;
import org.estgroup.phphub.common.internal.di.component.DaggerAppComponent;
import org.estgroup.phphub.common.internal.di.module.AppModule;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {
    private AppComponent appComponent;

    private ApiComponent apiComponent;

    @Inject
    Prefser prefser;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        LeakCanary.install(this);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        initializeInjector();
        initializeInjectorApi();
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initializeInjectorApi() {
        apiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public ApiComponent getApiComponent() {
        return apiComponent;
    }
}
