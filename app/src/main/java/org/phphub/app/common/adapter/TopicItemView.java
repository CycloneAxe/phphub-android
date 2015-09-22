package org.phphub.app.common.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.base.BaseAdapterItemView;

import butterknife.Bind;
import cn.bingoogolapple.badgeview.BGABadgeRelativeLayout;

public class TopicItemView extends BaseAdapterItemView<Topic> {
    @Bind(R.id.bga_rlyt_content)
    BGABadgeRelativeLayout topicContentView;

    @Bind(R.id.tv_title)
    TextView titleView;

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    public TopicItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recommended_list_item;
    }

    @Override
    public void bind(Topic topic) {
        topicContentView.showTextBadge(String.valueOf(topic.getReplyCount()));
        titleView.setText(topic.getTitle());
    }
}
