package org.phphub.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.phphub.app.R;
import org.phphub.app.common.base.BaseSupportFragment;

import icepick.State;

public class TopicFragment extends BaseSupportFragment {
    public static final String TOPIC_TYPE_KEY = "topic_type";

    @State
    protected String topicType = "recent";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(TOPIC_TYPE_KEY))) {
            topicType = getArguments().getString(TOPIC_TYPE_KEY);
        }
    }
}
