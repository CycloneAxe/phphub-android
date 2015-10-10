package org.phphub.app.api.entity;

import org.phphub.app.api.entity.element.Topic;

import java.util.List;

public class TopicEntity {
    protected List<Topic> data;

    public List<Topic> getData() {
        return data;
    }

    public void setData(List<Topic> data) {
        this.data = data;
    }

    public class TopicObj {
        protected Topic data;

        public Topic getData() {
            return data;
        }

        public void setData(Topic data) {
            this.data = data;
        }
    }
}
