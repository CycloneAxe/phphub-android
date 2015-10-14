package org.phphub.app.common.internal.di.component;

import org.phphub.app.common.internal.di.module.ApiModule;
import org.phphub.app.ui.presenter.MePersenter;
import org.phphub.app.ui.presenter.RecommendedPresenter;
import org.phphub.app.ui.presenter.TopicDetailPresenter;
import org.phphub.app.ui.presenter.TopicPresenter;
import org.phphub.app.ui.presenter.UserSpacePresenter;
import org.phphub.app.ui.presenter.WikiPresenter;
import org.phphub.app.ui.view.LoginActivity;
import org.phphub.app.ui.view.MeFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApiModule.class)
public interface ApiComponent {
        void inject(TopicPresenter topicPresenter);

        void inject(RecommendedPresenter recommendedPresenter);

        void inject(WikiPresenter wikiPresenter);

        void inject(TopicDetailPresenter topicDetailPresenter);

        void inject(UserSpacePresenter userSpacePresenter);

        void inject(LoginActivity loginActivity);

        void inject(MePersenter mePersenter);
}