package org.phphub.app.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class SupportDate {

    public String date;

    @SerializedName("timezone_type")
    public int timezoneType;

    public String timezone;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTimezoneType() {
        return timezoneType;
    }

    public void setTimezoneType(int timezoneType) {
        this.timezoneType = timezoneType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
