package org.phphub.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.App;
import org.phphub.app.common.base.BaseFragment;
import org.phphub.app.model.TopicModel;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RecommendedFragment extends BaseFragment {
    TopicModel topicModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicModel = new TopicModel(App.getInstance());
        topicModel.getTopicsByExcellent(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<TopicEntity, List<Topic>>() {
                    @Override
                    public List<Topic> call(TopicEntity topicEntity) {
                        return topicEntity.getData();
                    }
                })
                .subscribe(new Action1<List<Topic>>() {
                    @Override
                    public void call(List<Topic> topics) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommended, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.recommended);
    }
}
