package com.htphong.mylife.API;

import com.htphong.mylife.POJO.PostPOJO;
import com.htphong.mylife.POJO.StatusPOJO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/post")
    Call<RequestBody> savePost(@Field("desc") String desc, @Field("photo") String photo);

    @GET("api/posts")
    Call<PostPOJO> getPost(@Query("user") String user);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/likes")
    Call<StatusPOJO> likePost(@Field("post_id") String post_id);
}