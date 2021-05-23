package com.htphong.mylife.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Post;

import java.util.List;

public class PostPOJO {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
