package org.phphub.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.phphub.app.R;
import org.phphub.app.common.base.BaseFragment;

public class TopicFragment extends BaseFragment {
    public static final String TOPIC_TYPE_KEY = "topic_type";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic, container, false);
    }
}
