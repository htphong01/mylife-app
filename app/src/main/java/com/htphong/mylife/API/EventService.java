package com.htphong.mylife.API;

import com.htphong.mylife.POJO.EventPOJO;
import com.htphong.mylife.POJO.StatusPOJO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/events")
    Call<StatusPOJO> createEvent(@Field("title") String title,
                                 @Field("date_start") String date_start,
                                 @Field("date_end") String date_end,
                                 @Field("content") String content,
                                 @Field("address") String address,
                                 @Field("image") String image,
                                 @Field("privacy") String privacy);

    @GET("api/events")
    Call<EventPOJO> getEvents(@Query("order") String order);

    @GET("api/events/{id}")
    Call<EventPOJO> getEventInfo(@Path("id") Integer id);

    @PUT("api/event-attenders/{id}")
    Call<StatusPOJO> updateAttender(@Path("id") Integer id, @Query("event_id") Integer event_id);
}
