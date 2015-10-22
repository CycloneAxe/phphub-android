package org.estgroup.phphub.common.event;

public class NotificationChangeEvent {

    public final int notificationLength;

    public NotificationChangeEvent(int notificationLength) {
        this.notificationLength = notificationLength;
    }

    public String getNotificationLenght(){
        return String.valueOf(notificationLength);
    }


}
