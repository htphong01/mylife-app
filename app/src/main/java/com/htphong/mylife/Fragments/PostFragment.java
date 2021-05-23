package com.htphong.mylife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.CommentService;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Adapters.CommentAdapter;
import com.htphong.mylife.Constant;
import com.htphong.mylife.Models.Comment;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.CommentPOJO;
import com.htphong.mylife.POJO.PostPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostFragment extends Fragment {
    private View view;
    private Context mContext;
    private ImageButton postLikeIcon;
    private TextView txtPostTitle, txtPostAuthorName, txtPostTime, txtPostDescription, txtPostCountLike;
    private ImageView imgPostPhoto;
    private CircleImageView imgPostAuthorAvatar;
    private LinearLayout btnLikePost, btnCommentPost;
    private RecyclerView commentRecyclerView;
    private RecyclerView.Adapter commentAdapter;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private Retrofit retrofit;
    private static int post_id = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_post_fragment, container, false);
        mContext = container.getContext();
        post_id = Integer.parseInt(getArguments().getString("post_id"));
        init();
        return view;
    }

    private void init() {
        retrofit = new Client().getRetrofit(getContext());
        postLikeIcon = view.findViewById(R.id.post_like_icon);
        txtPostAuthorName = view.findViewById(R.id.txt_post_author_name);
        txtPostTime = view.findViewById(R.id.txt_post_different_time);
        txtPostDescription = view.findViewById(R.id.txt_post_description);
        txtPostCountLike = view.findViewById(R.id.txt_post_count_like);
        imgPostPhoto = view.findViewById(R.id.img_post_photo);
        imgPostAuthorAvatar = view.findViewById(R.id.post_img_avatar);
        btnLikePost = view.findViewById(R.id.post_like_btn);
        btnCommentPost = view.findViewById(R.id.post_comment_btn);
        commentRecyclerView = view.findViewById(R.id.post_recycler_comment);
        commentRecyclerView.setNestedScrollingEnabled(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setItemViewCacheSize(10);
        commentRecyclerView.setDrawingCacheEnabled(true);
        commentRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        commentAdapter = new CommentAdapter(commentArrayList, getContext());
        commentRecyclerView.setAdapter(commentAdapter);
        getPost();
        getComments();
    }

    private void getPost() {
        PostService postService = retrofit.create(PostService.class);
        Call<PostPOJO> call = postService.getSpecificPost(String.valueOf(post_id));
        call.enqueue(new Callback<PostPOJO>() {
            @Override
            public void onResponse(Call<PostPOJO> call, Response<PostPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Post post = response.body().getPosts().get(0);
                    txtPostAuthorName.setText(post.getUser().getUserName());
                    txtPostTime.setText(Helper.timeDifferent(post.getCreatedAt()));
                    txtPostDescription.setText(post.getDescription());
                    txtPostCountLike.setText(post.getLikesCount() + " người đã thích bài viết này");
                    Picasso.get().load(Constant.DOMAIN + post.getUser().getPhoto()).resize(350,350).centerCrop().into(imgPostAuthorAvatar);
                    Picasso.get().load(Constant.DOMAIN + post.getPhoto()).into(imgPostPhoto);
                    postLikeIcon.setImageResource(post.getSelfLike() ? R.drawable.ic_baseline_favorite_red : R.drawable.ic_baseline_favorite_outline);
                }
            }

            @Override
            public void onFailure(Call<PostPOJO> call, Throwable t) {
                Log.d("PostActivityError: ", t.getMessage());
            }
        });
    }

    private void getComments() {
        CommentService commentService = retrofit.create(CommentService.class);
        Call<CommentPOJO> commentPOJOCall = commentService.getCommentPost(String.valueOf(post_id));
        commentPOJOCall.enqueue(new Callback<CommentPOJO>() {
            @Override
            public void onResponse(Call<CommentPOJO> call, Response<CommentPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    List<Comment> commentList = response.body().getComments();
                    commentArrayList.addAll(commentList);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentPOJO> call, Throwable t) {
                Log.d("PostActivityError: ", t.getMessage());
            }
        });
    }

}
