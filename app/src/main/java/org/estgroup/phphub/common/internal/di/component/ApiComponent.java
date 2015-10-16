package org.estgroup.phphub.common.internal.di.component;

import org.estgroup.phphub.common.internal.di.module.ApiModule;
import org.estgroup.phphub.ui.presenter.TopicPublishPresenter;
import org.estgroup.phphub.ui.presenter.RecommendedPresenter;
import org.estgroup.phphub.ui.presenter.TopicDetailPresenter;
import org.estgroup.phphub.ui.presenter.TopicPresenter;
import org.estgroup.phphub.ui.presenter.UserSpacePresenter;
import org.estgroup.phphub.ui.presenter.WikiPresenter;
import org.estgroup.phphub.ui.view.LoginActivity;

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

        void inject(TopicPublishPresenter publishTopicPresenter);
}