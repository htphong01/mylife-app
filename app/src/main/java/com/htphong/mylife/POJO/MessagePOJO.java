package com.htphong.mylife.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Message;

import java.util.List;

public class MessagePOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("room_id")
    @Expose
    private String room_id;

    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
