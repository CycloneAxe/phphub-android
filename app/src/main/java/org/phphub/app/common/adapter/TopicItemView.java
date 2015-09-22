package org.phphub.app.common.adapter;

import android.content.Context;
import android.widget.TextView;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.base.BaseAdapterItemView;

import butterknife.Bind;

public class TopicItemView extends BaseAdapterItemView<Topic> {
    @Bind(R.id.tv_title)
    TextView titleView;

    public TopicItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recommended_list_item;
    }

    @Override
    public void bind(Topic topic) {
        titleView.setText(topic.getTitle());
    }
}
