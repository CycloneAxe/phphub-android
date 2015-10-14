package org.phphub.app.api.entity.element;

import com.google.gson.annotations.SerializedName;

import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.UserEntity;

public class Notification {
    private int id;

    private String type;

    private String body;

    @SerializedName("topic_id")
    private int topicId;

    @SerializedName("reply_id")
    private int replyId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("from_user_id")
    private int fromUserId;

    @SerializedName("type_msg")
    private String typeMsg;

    private String message;

    @SerializedName("from_user")
    private UserEntity.AUser fromUser;

    private TopicEntity.ATopic topic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getTypeMsg() {
        return typeMsg;
    }

    public void setTypeMsg(String typeMsg) {
        this.typeMsg = typeMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserEntity.AUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity.AUser fromUser) {
        this.fromUser = fromUser;
    }

    public TopicEntity.ATopic getTopic() {
        return topic;
    }

    public void setTopic(TopicEntity.ATopic topic) {
        this.topic = topic;
    }
}
