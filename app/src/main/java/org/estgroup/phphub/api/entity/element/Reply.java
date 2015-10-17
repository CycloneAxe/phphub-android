package org.estgroup.phphub.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Reply {

    @SerializedName("topic_id")
    int topicId;

    String body;

    @SerializedName("user_id")
    int userId;

    @SerializedName("body_original")
    String bodyOriginal;

    @SerializedName("updated_at")
    String updatedAt;

    @SerializedName("created_at")
    String createdAt;

    int id;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBodyOriginal() {
        return bodyOriginal;
    }

    public void setBodyOriginal(String bodyOriginal) {
        this.bodyOriginal = bodyOriginal;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
