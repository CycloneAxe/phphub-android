package org.estgroup.phphub.api.entity;

import org.estgroup.phphub.api.entity.element.Reply;

import java.util.List;

public class ReplyEntity {

    public class Replys {
        public List<Reply> data;

        public List<Reply> getData() {
            return data;
        }

        public void setData(List<Reply> data) {
            this.data = data;
        }
    }

    public class AReply {
        Reply data;

        public Reply getData() {
            return data;
        }

        public void setData(Reply data) {
            this.data = data;
        }
    }

}
