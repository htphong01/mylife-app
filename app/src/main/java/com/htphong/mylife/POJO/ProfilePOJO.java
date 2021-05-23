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

    @SerializedName("isFriend")
    @Expose
    private Boolean isFriend;

    @SerializedName("postCount")
    @Expose
    private String postCount;

    @SerializedName("friendCount")
    @Expose
    private String friendCount;

    @SerializedName("user")
    @Expose
    private List<User> user = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Boolean isFriend) {
        this.isFriend = isFriend;
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

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    @Override
    public String toString() {
        return "ProfilePOJO{" +
                "success=" + success +
                ", isFriend=" + isFriend +
                ", user=" + user +
                '}';
    }
}
