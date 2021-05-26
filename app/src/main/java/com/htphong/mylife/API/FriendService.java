package com.htphong.mylife.API;

import com.htphong.mylife.POJO.FriendPOJO;
import com.htphong.mylife.POJO.ProfilePOJO;
import com.htphong.mylife.POJO.SearchPOJO;
import com.htphong.mylife.POJO.StatusPOJO;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FriendService {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/friends")
    Call<FriendPOJO> addFriend(@Field("user_id2") String user_id2);

    @GET("api/friends/user")
    Call<FriendPOJO> getFriendOfUser();

    @GET("api/friends/requests")
    Call<FriendPOJO> getFriendRequests();

    @Headers("Accept: application/json")
    @PUT("api/friends/{id}")
    Call<FriendPOJO> acceptFriend(@Path("id") String id);

    @Headers("Accept: application/json")
    @DELETE("api/friends/{id}")
    Call<StatusPOJO> removeFriendRequest(@Path("id") String id);
}
