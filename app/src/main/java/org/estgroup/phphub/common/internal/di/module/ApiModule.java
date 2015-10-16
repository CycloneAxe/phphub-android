package org.estgroup.phphub.common.internal.di.module;

import android.content.Context;

import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
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
    TopicModel provideTopicModel(@ForApplication Context context) {
        return new TopicModel(context);
    }

    @Provides
    @Singleton
    TokenModel provideTokenModel(@ForApplication Context context) {
        return new TokenModel(context);
    }

    @Provides
    @Singleton
    @Named(AUTH_TYPE_USER)
    UserModel provideUserModelByAuth(@ForApplication Context context) {
        UserModel userModel = new UserModel(context);
        userModel.ignoreToken(true);
        return userModel;
    }

    @Provides
    @Singleton
    @Named(AUTH_TYPE_GUEST)
    UserModel provideUserModelByGuest(@ForApplication Context context) {
        return new UserModel(context);
    }
}