package com.htphong.mylife.API;

import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.POJO.TaskPOJO;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/tasks")
    Call<StatusPOJO> createTask(@Field("receivers[]") ArrayList<String> receivers, @Field("room_id") String room_id, @Field("title") String title, @Field("content") String content, @Field("deadline") String deadline);

    @GET("api/tasks/room/{room_id}")
    Call<TaskPOJO> getRoomTask(@Path("room_id") String room_id);

    @GET("api/tasks/{id}")
    Call<TaskPOJO> getTask(@Path("id") String id);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("api/tasks/{id}")
    Call<StatusPOJO> submitTask(@Path("id") String id, @Field("note") String note, @Field("file") String file);

    @Headers("Accept: application/json")
    @PUT("api/tasks/complete/{id}")
    Call<StatusPOJO> completeTask(@Path("id") String id);
}
