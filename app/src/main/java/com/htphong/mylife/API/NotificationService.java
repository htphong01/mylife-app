package com.htphong.mylife.API;

import com.htphong.mylife.POJO.NotificationPOJO;
import com.htphong.mylife.POJO.StatusPOJO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("api/notifications/user")
    Call<NotificationPOJO> getNotification();

    @Headers("Accept: application/json")
    @PUT("api/notifications/{id}")
    Call<StatusPOJO> seenNotification(@Path("id") String id);
}
