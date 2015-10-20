package org.estgroup.phphub.common.internal.di.component;

import org.estgroup.phphub.common.Navigator;
import org.estgroup.phphub.common.internal.di.module.AppModule;
import org.estgroup.phphub.ui.view.settings.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SettingsFragment settingsFragment);

    Navigator navigator();
}
