package com.htphong.mylife.API;

import com.htphong.mylife.POJO.ProfilePOJO;
import com.htphong.mylife.POJO.UserPOJO;
import com.htphong.mylife.POJO.StatusPOJO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/login")
    Call<ProfilePOJO> login(@Field("email") String email, @Field("password") String password);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/register")
    Call<ProfilePOJO> register(@Field("email") String email, @Field("password") String password);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("api/save-user-infor")
    Call<ProfilePOJO> saveUserInfor(@Field("name") String name, @Field("photo") String photo, @Field("dateOfBirth") String dateOfBirth, @Field("gender") String gender);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("api/user/change-avatar")
    Call<StatusPOJO> changeAvatar(@Field("photo") String photo);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("api/user")
    Call<ProfilePOJO> updateUserInfor(@Field("name") String name, @Field("dateOfBirth") String dateOfBirth,  @Field("gender") String gender,
                                      @Field("address") String address, @Field("education") String education,  @Field("work") String work,
                                      @Field("phoneNumber") String phoneNumber, @Field("relationship") String relationship);

    @GET("api/search/user")
    Call<UserPOJO> getUser(@Query("q") String q);

    @GET("api/get/user")
    Call<ProfilePOJO> getFriend(@Query("id") String id);

    @GET("api/logout")
    Call<StatusPOJO> logout();
}
