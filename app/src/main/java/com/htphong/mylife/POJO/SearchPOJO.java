package com.htphong.mylife.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.User;

import java.util.List;

public class SearchPOJO {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}