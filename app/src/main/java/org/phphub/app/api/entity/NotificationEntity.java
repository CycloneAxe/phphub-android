package org.phphub.app.api.entity;

import org.phphub.app.api.entity.element.Notification;

import java.util.List;

public class NotificationEntity {
    private List<Notification> data;

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }
}
