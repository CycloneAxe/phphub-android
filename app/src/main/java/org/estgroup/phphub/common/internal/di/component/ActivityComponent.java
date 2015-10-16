package org.estgroup.phphub.common.internal.di.component;

import android.app.Activity;

import org.estgroup.phphub.common.internal.di.module.ActivityModule;
import org.estgroup.phphub.common.internal.di.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {
    Activity activity();
}
