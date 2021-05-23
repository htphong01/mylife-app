package com.htphong.mylife.POJO;

import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Comment;

import java.util.List;

public class CommentPOJO {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("comments")
    private List<Comment> comments = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
