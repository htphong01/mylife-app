package com.htphong.mylife.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("creater_id")
    @Expose
    private Integer createrId;
    @SerializedName("receiver_id")
    @Expose
    private Integer receiverId;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("isSubmitted")
    @Expose
    private Integer isSubmitted;
    @SerializedName("submitted_at")
    @Expose
    private Object submittedAt;
    @SerializedName("isCompleted")
    @Expose
    private Integer isCompleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("creater_name")
    @Expose
    private String createrName;

    @SerializedName("creater_avatar")
    @Expose
    private String createrAvatar;

    @SerializedName("receiver_name")
    @Expose
    private String receiverName;

    @SerializedName("receiver_avatar")
    @Expose
    private String receiverAvatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Integer createrId) {
        this.createrId = createrId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(Integer isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public Object getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Object submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
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

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCreaterAvatar() {
        return createrAvatar;
    }

    public void setCreaterAvatar(String createrAvatar) {
        this.createrAvatar = createrAvatar;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }
}
