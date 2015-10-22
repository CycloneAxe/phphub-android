package org.estgroup.phphub.common.event;

import org.estgroup.phphub.api.entity.element.Notification;

import java.util.List;

public class NotificationChangeEvent {

    public final List<Notification> notificationList;

    public NotificationChangeEvent(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public String getNotificationLenght(){

        return String.valueOf(notificationList.size());

    }


}
