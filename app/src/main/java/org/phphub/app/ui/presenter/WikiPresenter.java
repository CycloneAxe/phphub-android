package org.phphub.app.ui.presenter;

import android.os.Bundle;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.App;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.model.TokenModel;
import org.phphub.app.model.TopicModel;
import org.phphub.app.ui.view.WikiFragment;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

public class WikiPresenter extends BaseRxPresenter<WikiFragment> {
    public static final int REQUEST_WIKI_ID = 1;

    @Inject
    TopicModel topicModel;

    @Inject
    TokenModel tokenModel;

    @Inject
    Prefser prefser;

    protected int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_WIKI_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByWiki(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                })
                                .compose(WikiPresenter.this.<List<Topic>>applyRetryByGuest(tokenModel, prefser));
                    }
                },
                new Action2<WikiFragment, List<Topic>>() {
                    @Override
                    public void call(WikiFragment wikiFragment, List<Topic> topics) {
                        wikiFragment.onChangeItems(topics, pageIndex);
                    }
                },
                new Action2<WikiFragment, Throwable>() {
                    @Override
                    public void call(WikiFragment wikiFragment, Throwable throwable) {
                        wikiFragment.onNetworkError(throwable, pageIndex);
                    }
                }
        );
    }

    public void refresh() {
        pageIndex = 1;
        start(REQUEST_WIKI_ID);
    }

    public void nextPage() {
        pageIndex++;
        start(REQUEST_WIKI_ID);
    }
}
