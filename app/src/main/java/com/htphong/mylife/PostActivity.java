package com.htphong.mylife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.CommentService;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Adapters.CommentAdapter;
import com.htphong.mylife.Fragments.PostFragment;
import com.htphong.mylife.Models.Comment;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.CommentPOJO;
import com.htphong.mylife.POJO.PostPOJO;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack, postLikeIcon;
    private TextView txtPostTitle;
    private static int post_id = 1;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        post_id = getIntent().getExtras().getInt("post_id");
        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("post_id", String.valueOf(getIntent().getExtras().getInt("post_id")));
        postFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.framePostActivyContainer, postFragment).commit();
        btnBack = findViewById(R.id.post_back_btn);
        btnBack.setOnClickListener(this);
        txtPostTitle = findViewById(R.id.txt_post_title);
    }

    private void getPost() {
        Retrofit retrofit = new Client().getRetrofit(getApplicationContext());
        PostService postService = retrofit.create(PostService.class);
        Call<PostPOJO> call = postService.getSpecificPost(String.valueOf(post_id));
        call.enqueue(new Callback<PostPOJO>() {
            @Override
            public void onResponse(Call<PostPOJO> call, Response<PostPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Post post = response.body().getPosts().get(0);
                    txtPostTitle.setText("Bài viết của " + post.getUser().getUserName());
                }
            }

            @Override
            public void onFailure(Call<PostPOJO> call, Throwable t) {
                Log.d("PostActivityError: ", t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_back_btn: {
                super.onBackPressed();
                break;
            }
        }
    }
}