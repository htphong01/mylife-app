package com.htphong.mylife.API;

import com.htphong.mylife.POJO.NotificationPOJO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationService {
    @GET("api/notifications/user")
    Call<NotificationPOJO> getNotification();
}
