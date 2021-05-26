package com.htphong.mylife.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Room;

import java.util.List;

public class RoomPOJO {
    @SerializedName("success")
    @Expose

    private Boolean success;
    @SerializedName("rooms")
    @Expose
    private List<Room> rooms = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

}
