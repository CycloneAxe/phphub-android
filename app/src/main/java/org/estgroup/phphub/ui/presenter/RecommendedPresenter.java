package org.estgroup.phphub.ui.presenter;

import android.os.Bundle;

import com.github.pwittchen.prefser.library.Prefser;

import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.transformer.TokenGeneratorTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.view.RecommendedFragment;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;


public class RecommendedPresenter extends BaseRxPresenter<RecommendedFragment> {
    private static final int REQUEST_ID = 1;

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

        restartableLatestCache(REQUEST_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByExcellent(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                })
                                .compose(new TokenGeneratorTransformer<List<Topic>>(tokenModel, prefser));
                    }
                },
                new Action2<RecommendedFragment, List<Topic>>() {
                    @Override
                    public void call(RecommendedFragment recommendedFragment, List<Topic> topics) {
                        recommendedFragment.onChangeItems(topics, pageIndex);
                    }
                },
                new Action2<RecommendedFragment, Throwable>() {
                    @Override
                    public void call(RecommendedFragment recommendedFragment, Throwable throwable) {
                        recommendedFragment.onNetworkError(throwable, pageIndex);
                    }
                });
    }

    public void refresh() {
        pageIndex = 1;
        start(REQUEST_ID);
    }

    public void nextPage() {
        pageIndex++;
        start(REQUEST_ID);
    }
}
