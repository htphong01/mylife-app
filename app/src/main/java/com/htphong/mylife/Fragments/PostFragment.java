package com.htphong.mylife.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.CommentService;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.Activities.ImageActivity;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.Adapters.CommentAdapter;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Comment;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.CommentPOJO;
import com.htphong.mylife.POJO.PostPOJO;
import com.htphong.mylife.POJO.StatusPOJO;
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

public class PostFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context mContext;
    private ImageButton postLikeIcon;
    private TextView txtPostTitle, txtPostAuthorName, txtPostTime, txtPostDescription, txtPostCountLike;
    private ImageView imgPostPhoto;
    private CircleImageView imgPostAuthorAvatar;
    private LinearLayout btnLikePost, btnCommentPost;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView commentRecyclerView;
    private RecyclerView.Adapter commentAdapter;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private Retrofit retrofit;
    private static int post_id = 1;
    private Post post = new Post();

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
        commentRecyclerView.setItemViewCacheSize(10);
        commentRecyclerView.setDrawingCacheEnabled(true);
        commentRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        commentAdapter = new CommentAdapter(commentArrayList, getContext());
        commentRecyclerView.setAdapter(commentAdapter);

        swipeRefreshLayout = view.findViewById(R.id.layout_post_fragment_swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getComments();
            getPost();
        });

        btnLikePost.setOnClickListener(this);
        btnCommentPost.setOnClickListener(this);
        imgPostPhoto.setOnClickListener(this);
        txtPostAuthorName.setOnClickListener(this);

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
                    post = response.body().getPosts().get(0);
                    txtPostAuthorName.setText(post.getUser().getUserName());
                    txtPostTime.setText(Helper.timeDifferent(post.getCreatedAt()));
                    txtPostDescription.setText(post.getDescription());
                    txtPostCountLike.setText(post.getLikesCount() + " người đã thích bài viết này");
                    Picasso.get().load(Constant.DOMAIN + post.getUser().getPhoto()).resize(350,350).centerCrop().into(imgPostAuthorAvatar);
                    Picasso.get().load(Constant.DOMAIN + post.getPhoto()).into(imgPostPhoto);
                    postLikeIcon.setImageResource(post.getSelfLike() ? R.drawable.ic_baseline_favorite_red : R.drawable.ic_baseline_favorite_outline);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostPOJO> call, Throwable t) {
                Log.d("PostActivityError: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getComments() {
        commentArrayList.clear();
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
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CommentPOJO> call, Throwable t) {
                Log.d("PostActivityError: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void likePost() {
        postLikeIcon.setImageResource(post.getSelfLike() ? R.drawable.ic_baseline_favorite_outline : R.drawable.ic_baseline_favorite_red );
        Retrofit retrofit = new Client().getRetrofit(getContext());
        PostService postService = retrofit.create(PostService.class);
        Call<StatusPOJO> call = postService.likePost(String.valueOf(post.getId()));
        call.enqueue(new Callback<StatusPOJO>() {
            @Override
            public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    post.setSelfLike(!post.getSelfLike());
                    post.setLikesCount(post.getSelfLike() ? (post.getLikesCount() + 1) : (post.getLikesCount() - 1));
                    txtPostCountLike.setText(post.getLikesCount() + " người đã thích bài viết này");
                }
            }

            @Override
            public void onFailure(Call<StatusPOJO> call, Throwable t) {
                Log.d("PostLike: ", t.getMessage());
            }
        });
    }

    private void gotoProfile(String user_id) {
        Intent intent;
        SharedPreferences userTargetPref = getContext().getSharedPreferences("user_target", getContext().MODE_PRIVATE);
        SharedPreferences userPref = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = userTargetPref.edit();
        editor.putString("id", user_id);
        editor.apply();
        if(user_id.equals(userPref.getString("id", "1"))) {
            HomeActivity homeActivity = (HomeActivity)getContext();
            homeActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer, new AccountFragment()).commit();
        } else {
            intent = new Intent(getContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_like_btn: {
                likePost();
                break;
            }

            case R.id.post_comment_btn: {

                break;
            }

            case R.id.img_post_photo: {
                Intent intent = new Intent(getContext(), ImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", post.getPhoto());
                startActivity(intent);
                break;
            }

            case R.id.txt_post_author_name: {
                gotoProfile(String.valueOf(post.getUserId()));
                break;
            }
        }
    }
}
