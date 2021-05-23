package com.htphong.mylife.POJO;

import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Friend;

import java.util.List;

public class FriendPOJO {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("friends")
    private List<Friend> friends = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}

