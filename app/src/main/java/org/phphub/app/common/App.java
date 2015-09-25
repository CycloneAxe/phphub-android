package org.phphub.app.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import org.phphub.app.common.internal.di.component.ApiComponent;
import org.phphub.app.common.internal.di.component.AppComponent;
import org.phphub.app.common.internal.di.component.DaggerApiComponent;
import org.phphub.app.common.internal.di.component.DaggerAppComponent;
import org.phphub.app.common.internal.di.module.AppModule;

public class App extends Application {
    private AppComponent appComponent;

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        LeakCanary.install(this);

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
