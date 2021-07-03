package com.htphong.mylife.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.User;

import java.util.List;

public class ProfilePOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("friendInvitationId")
    @Expose
    private Integer friendInvitationId;

    @SerializedName("postCount")
    @Expose
    private String postCount;

    @SerializedName("friendCount")
    @Expose
    private String friendCount;

    @SerializedName("statusFriend")
    @Expose
    private Integer statusFriend;

    @SerializedName("requestSendByYou")
    @Expose
    private Boolean requestSendByYou;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user")
    @Expose
    private List<User> user = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getFriendInvitationId() {
        return friendInvitationId;
    }

    public void setFriendInvitationId(Integer friendInvitationId) {
        this.friendInvitationId = friendInvitationId;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public Integer getStatusFriend() {
        return statusFriend;
    }

    public void setStatusFriend(Integer statusFriend) {
        this.statusFriend = statusFriend;
    }

    public Boolean getRequestSendByYou() {
        return requestSendByYou;
    }

    public void setRequestSendByYou(Boolean requestSendByYou) {
        this.requestSendByYou = requestSendByYou;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    @Override
    public String toString() {
        return "ProfilePOJO{" +
                "success=" + success +
                ", user=" + user +
                '}';
    }
}
