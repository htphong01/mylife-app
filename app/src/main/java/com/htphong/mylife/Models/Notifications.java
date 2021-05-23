package com.htphong.mylife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Notifications implements Comparable{
    @SerializedName("id")
    private Integer id;

    @SerializedName("sender_id")
    private Integer senderId;

    @SerializedName("content")
    private String content;

    @SerializedName("receiver_id")
    private Integer receiverId;

    @SerializedName("link")
    private String link;

    @SerializedName("image")
    private String image;

    @SerializedName("type")
    private Integer type;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public ArrayList<Notifications> getUpdateNotifications() {
        ArrayList<Notifications> notificationsArrayList = new ArrayList<>();
        return notificationsArrayList;
    }

    @Override
    public int compareTo(Object o) {
        Notifications notification = (Notifications)o;
        if(notification.id == this.id) {
            return 1;
        }
        return 0;
    }
}
