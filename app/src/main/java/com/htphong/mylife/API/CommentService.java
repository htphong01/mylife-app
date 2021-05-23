package com.htphong.mylife.API;

import com.htphong.mylife.POJO.CommentPOJO;
import com.htphong.mylife.POJO.PostPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommentService {

    @GET("api/comments/post")
    Call<CommentPOJO> getCommentPost(@Query("post_id") String post_id);
}
