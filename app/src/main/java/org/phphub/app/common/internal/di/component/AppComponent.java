package org.phphub.app.common.internal.di.component;

import org.phphub.app.common.App;
import org.phphub.app.common.Navigator;
import org.phphub.app.common.internal.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);

    Navigator navigator();
}
