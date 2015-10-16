package org.estgroup.phphub.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Link {
    @SerializedName("details_web_view")
    protected String detailsWebView;

    @SerializedName("replies_web_view")
    protected String repliesWebView;

    public String getDetailsWebView() {
        return detailsWebView;
    }

    public void setDetailsWebView(String detailsWebView) {
        this.detailsWebView = detailsWebView;
    }

    public String getRepliesWebView() {
        return repliesWebView;
    }

    public void setRepliesWebView(String repliesWebView) {
        this.repliesWebView = repliesWebView;
    }
}
