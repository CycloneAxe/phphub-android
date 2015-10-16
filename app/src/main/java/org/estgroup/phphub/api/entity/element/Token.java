package org.estgroup.phphub.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    String token;

    @SerializedName("token_type")
    String type;

    @SerializedName("expires_in")
    int expires;

    @SerializedName("refresh_token")
    String refreshToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
