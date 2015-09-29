package org.phphub.app.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Topic {
    protected int id;

    protected String title;

    @SerializedName("user_id")
    protected int userId;

    @SerializedName("is_excellent")
    protected boolean isExcellent;

    @SerializedName("reply_count")
    protected int replyCount;

    @SerializedName("updated_at")
    protected SupportDate updatedAt;

    @SerializedName("user")
    protected UserInfo userInfo;

    @SerializedName("last_reply_user")
    protected UserInfo lastReplyUser;

    public class UserInfo {
        User data;

        public User getData() {
            return data;
        }

        public void setData(User data) {
            this.data = data;
        }
    }

    @SerializedName("node")
    protected NodeInfo nodeInfo;

    public class NodeInfo {
        Node data;

        public Node getData() {
            return data;
        }

        public void setData(Node data) {
            this.data = data;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isExcellent() {
        return isExcellent;
    }

    public void setIsExcellent(boolean isExcellent) {
        this.isExcellent = isExcellent;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public SupportDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(SupportDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getLastReplyUser() {
        return lastReplyUser;
    }

    public void setLastReplyUser(UserInfo lastReplyUser) {
        this.lastReplyUser = lastReplyUser;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
}
