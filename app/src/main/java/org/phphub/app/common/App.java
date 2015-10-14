package org.phphub.app.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.pwittchen.prefser.library.Prefser;
import com.squareup.leakcanary.LeakCanary;

import org.phphub.app.common.internal.di.component.ApiComponent;
import org.phphub.app.common.internal.di.component.AppComponent;
import org.phphub.app.common.internal.di.component.DaggerApiComponent;
import org.phphub.app.common.internal.di.component.DaggerAppComponent;
import org.phphub.app.common.internal.di.module.AppModule;
import org.phphub.app.common.util.ApiUtils;
import static org.phphub.app.common.Constant.*;

import javax.inject.Inject;

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

        initializeInjector();
        initializeInjectorApi();

        appComponent.inject(this);
        ApiUtils.asRequestInterceptor()
                .setToken(prefser.get(GUEST_TOKEN_KEY, String.class, null));
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
