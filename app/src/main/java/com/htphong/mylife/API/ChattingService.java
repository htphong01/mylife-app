package com.htphong.mylife.API;

import com.htphong.mylife.POJO.CommentPOJO;
import com.htphong.mylife.POJO.MessagePOJO;
import com.htphong.mylife.POJO.RoomPOJO;
import com.htphong.mylife.POJO.StatusPOJO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChattingService {

    @GET("api/rooms")
    Call<RoomPOJO> getListChatRoom();

    @GET("api/rooms/{room_id}")
    Call<RoomPOJO> getChatRoom(@Path("room_id") String room_id);

    @GET("api/messages/{room_id}")
    Call<MessagePOJO> getListMessage(@Path("room_id") String room_id, @Query("user_id") String user_id);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/messages")
    Call<MessagePOJO> sendMessage(@Field("room_id") String room_id, @Field("message") String message, @Field("type") String type);
}
