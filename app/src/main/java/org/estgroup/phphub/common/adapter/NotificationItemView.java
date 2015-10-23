package org.estgroup.phphub.common.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ocpsoft.pretty.time.PrettyTime;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Notification;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseAdapterItemView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;

import static org.estgroup.phphub.common.qualifier.ClickType.CLICK_TYPE_USER_CLICKED;

public class NotificationItemView extends BaseAdapterItemView<Notification> {

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_msg_date)
    TextView msgDateView;

    @Bind(R.id.tv_msg_details)
    TextView msgDetailsView;

    @Bind(R.id.tv_msg_reply)
    TextView msgReplyView;

    public NotificationItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.message_item;
    }

    @Override
    public void bind(Notification notification) {
        String msgType = notification.getType();

        User user = notification.getFromUser().getData();
        Topic topic = notification.getTopic().getData();
        String msgDate = user.getName();

        avatarView.setImageURI(Uri.parse(user.getAvatar()));

        if (notification.getCreatedAt() != null) {
            Locale locale = getResources().getConfiguration().locale;
            PrettyTime prettyTime = new PrettyTime(locale);
            String dateStr = notification.getCreatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                String prettyTimeString = prettyTime.format(sdf.parse(dateStr));
                msgDate += " • " + prettyTimeString;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        msgDateView.setText(msgDate);
        msgDetailsView.setText(notification.getTypeMsg() + " : " + topic.getTitle());

        if (msgType.equals("new_reply")) {
            msgReplyView.setText(notification.getBody());
            msgReplyView.setVisibility(VISIBLE);
        } else {
            msgReplyView.setVisibility(GONE);
        }

        avatarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(CLICK_TYPE_USER_CLICKED);
            }
        });
    }
}
