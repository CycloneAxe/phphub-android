package org.phphub.app.common.internal.di.module;

import android.content.Context;

import org.phphub.app.common.internal.di.qualifier.ForApplication;
import org.phphub.app.model.TopicModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class ApiModule {
    @Provides
    @Singleton
    TopicModel provideTopicModel(@ForApplication Context context) {
        return new TopicModel(context);
    }
}
