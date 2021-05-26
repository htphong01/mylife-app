package com.htphong.mylife.API;

import com.htphong.mylife.POJO.CommentPOJO;
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

public interface CommentService {

    @GET("api/comments/post")
    Call<CommentPOJO> getCommentPost(@Query("post_id") String post_id);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/comments")
    Call<StatusPOJO> sendComment(@Field("post_id") String post_id, @Field("comment") String comment, @Field("type") String type);
}
