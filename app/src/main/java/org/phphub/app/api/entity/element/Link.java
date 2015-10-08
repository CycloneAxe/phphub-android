package org.phphub.app.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Link {
    @SerializedName("details_web_view")
    protected String detailsWebView;

    public String getDetailsWebView() {
        return detailsWebView;
    }

    public void setDetailsWebView(String detailsWebView) {
        this.detailsWebView = detailsWebView;
    }
}
