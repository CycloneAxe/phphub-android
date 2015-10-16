package org.estgroup.phphub.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Response {

    String message;

    @SerializedName("status_code")
    int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
