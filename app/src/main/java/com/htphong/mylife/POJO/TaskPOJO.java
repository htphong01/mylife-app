package com.htphong.mylife.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htphong.mylife.Models.Task;

import java.util.List;

public class TaskPOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("tasks")
    @Expose
    private List<Task> tasks = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
