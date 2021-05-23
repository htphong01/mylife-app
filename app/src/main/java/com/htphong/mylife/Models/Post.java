package com.htphong.mylife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {

    @SerializedName("id")
    private Integer id;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("description")
    private String description;
    @SerializedName("photo")
    private String photo;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("commentsCount")
    private Integer commentsCount;
    @SerializedName("likesCount")
    private Integer likesCount;
    @SerializedName("selfLike")
    private Boolean selfLike;
    @SerializedName("user")
    private User user;
    @SerializedName("comments")
    private List<Comment> comments = null;
    @SerializedName("likes")
    private List<Like> likes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Boolean getSelfLike() {
        return selfLike;
    }

    public void setSelfLike(Boolean selfLike) {
        this.selfLike = selfLike;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
