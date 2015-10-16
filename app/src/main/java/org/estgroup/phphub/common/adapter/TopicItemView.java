package org.estgroup.phphub.common.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ocpsoft.pretty.time.PrettyTime;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseAdapterItemView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import cn.bingoogolapple.badgeview.BGABadgeRelativeLayout;
import static org.estgroup.phphub.common.qualifier.ClickType.*;

public class TopicItemView extends BaseAdapterItemView<Topic> {
    @Bind(R.id.bga_rlyt_content)
    BGABadgeRelativeLayout topicContentView;

    @Bind(R.id.tv_title)
    TextView titleView;

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_description)
    TextView descriptionView;

    public TopicItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.topic_item;
    }

    @Override
    public void bind(Topic topic) {
        String des = "";

        Uri avatarUri = Uri.parse(topic.getUser().getData().getAvatar());
        String commentCount = String.valueOf(topic.getReplyCount());

        if (topic.getReplyCount() > 99) {
            commentCount = "99+";
        }

        if (topic.getNode() != null) {
            des += topic.getNode().getData().getName();
        }

        if (topic.getLastReplyUser() != null) {
            des += " • " + getResources().getString(R.string.last_for) + " " + topic.getLastReplyUser().getData().getName();
        }

        if (topic.getUpdatedAt() != null) {
            Locale locale = getResources().getConfiguration().locale;
            PrettyTime prettyTime = new PrettyTime(locale);
            String dateStr = topic.getUpdatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                String prettyTimeString = prettyTime.format(sdf.parse(dateStr));
                des += " • " + prettyTimeString;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        topicContentView.showTextBadge(commentCount);
        titleView.setText(topic.getTitle());
        avatarView.setImageURI(avatarUri);
        descriptionView.setText(des);

        topicContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(CLICK_TYPE_TOPIC_CLICKED);
            }
        });

        avatarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(CLICK_TYPE_USER_CLICKED);
            }
        });
    }
}
