package com.htphong.mylife.API;

import com.htphong.mylife.POJO.StatusPOJO;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TaskService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/tasks")
    Call<StatusPOJO> createTask(@Field("receivers[]") ArrayList<String> receivers, @Field("room_id") String room_id, @Field("title") String title, @Field("content") String content, @Field("deadline") String deadline);
}
