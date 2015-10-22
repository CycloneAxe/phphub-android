package org.estgroup.phphub.common.internal.di.module;

import android.content.Context;

import com.github.pwittchen.prefser.library.Prefser;

import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.provider.GuestTokenProvider;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.model.UserModel;
import static org.estgroup.phphub.common.qualifier.AuthType.*;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class ApiModule {
    @Provides
    @Singleton
    @Named(AUTH_TYPE_USER)
    TopicModel provideTopicModelByAuth(@ForApplication Context context) {
        return new TopicModel(context, null);
    }


    @Provides
    @Singleton
    TopicModel provideTopicModel(@ForApplication Context context, Prefser prefser) {
        return new TopicModel(context, new GuestTokenProvider(prefser));
    }

    @Provides
    @Singleton
    TokenModel provideTokenModel(@ForApplication Context context) {
        return new TokenModel(context, null);
    }

    @Provides
    @Singleton
    @Named(AUTH_TYPE_USER)
    UserModel provideUserModelByAuth(@ForApplication Context context) {
        return new UserModel(context, null);
    }

    @Provides
    @Singleton
    UserModel provideUserModelByUser(@ForApplication Context context, Prefser prefser) {
        return new UserModel(context, new GuestTokenProvider(prefser));
    }
}