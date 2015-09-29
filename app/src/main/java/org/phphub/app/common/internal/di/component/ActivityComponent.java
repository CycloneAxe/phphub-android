package org.phphub.app.common.internal.di.component;

import android.app.Activity;

import org.phphub.app.common.internal.di.module.ActivityModule;
import org.phphub.app.common.internal.di.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {
    Activity activity();
}
