package org.estgroup.phphub.api.entity.element;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Link implements Serializable {
    @SerializedName("details_web_view")
    protected String detailsWebView;

    @SerializedName("replies_web_view")
    protected String repliesWebView;

    @SerializedName("web_url")
    protected String webURL;

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

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
