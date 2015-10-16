package org.estgroup.phphub.api.entity.element;

import com.google.gson.annotations.SerializedName;

import org.estgroup.phphub.api.entity.NodeEntity;
import org.estgroup.phphub.api.entity.UserEntity;

public class Topic {
    protected int id;

    protected String title;

    protected String body;

    @SerializedName("user_id")
    protected int userId;

    @SerializedName("last_reply_user_id")
    protected int lastReplyUserId;

    @SerializedName("node_id")
    protected int nodeId;

    protected Link links;

    @SerializedName("is_excellent")
    protected boolean isExcellent;

    @SerializedName("reply_count")
    protected int replyCount;

    @SerializedName("view_count")
    protected int viewCount;

    @SerializedName("favorite_count")
    protected int favoriteCount;

    @SerializedName("vote_count")
    protected int voteCount;

    protected boolean favorite;

    protected boolean attention;

    @SerializedName("vote_up")
    protected boolean voteUp;

    @SerializedName("vote_down")
    protected boolean voteDown;

    @SerializedName("created_at")
    protected String createdAt;

    @SerializedName("updated_at")
    protected String updatedAt;

    protected UserEntity.AUser user;

    @SerializedName("last_reply_user")
    protected UserEntity.AUser lastReplyUser;

    protected NodeEntity.ANode node;

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

    public int getLastReplyUserId() {
        return lastReplyUserId;
    }

    public void setLastReplyUserId(int lastReplyUserId) {
        this.lastReplyUserId = lastReplyUserId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Link getLinks() {
        return links;
    }

    public void setLinks(Link links) {
        this.links = links;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isAttention() {
        return attention;
    }

    public void setAttention(boolean attention) {
        this.attention = attention;
    }

    public boolean isVoteUp() {
        return voteUp;
    }

    public void setVoteUp(boolean voteUp) {
        this.voteUp = voteUp;
    }

    public boolean isVoteDown() {
        return voteDown;
    }

    public void setVoteDown(boolean voteDown) {
        this.voteDown = voteDown;
    }

    public UserEntity.AUser getUser() {
        return user;
    }

    public void setUser(UserEntity.AUser user) {
        this.user = user;
    }

    public UserEntity.AUser getLastReplyUser() {
        return lastReplyUser;
    }

    public void setLastReplyUser(UserEntity.AUser lastReplyUser) {
        this.lastReplyUser = lastReplyUser;
    }

    public NodeEntity.ANode getNode() {
        return node;
    }

    public void setNode(NodeEntity.ANode node) {
        this.node = node;
    }
}
