package org.phphub.app.common.internal.di.module;

import android.accounts.AccountManager;
import android.content.Context;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.common.App;
import org.phphub.app.common.internal.di.qualifier.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.unicate.retroauth.AuthAccountManager;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideAppContext() {
        return app;
    }

    @Provides
    @Singleton
    Prefser providePrefser(@ForApplication Context context) {
        return new Prefser(context);
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager(@ForApplication Context context) {
        return AccountManager.get(context);
    }

    @Provides
    @Singleton
    AuthAccountManager provideAuthAccountManager(@ForApplication Context context) {
        return new AuthAccountManager(context);
    }
}