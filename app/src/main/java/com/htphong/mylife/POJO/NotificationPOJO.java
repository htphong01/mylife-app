package com.htphong.mylife.POJO;

import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Notifications;

import java.util.List;

public class NotificationPOJO {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("notifications")
    private List<Notifications> notifications = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }
}
