package com.htphong.mylife;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack, postLikeIcon, btnImage, btnSticker, btnSend;
    private TextView txtPostTitle;
    private static int post_id = 1;
    private FragmentManager fragmentManager;
    private static final int GALLERY_ADD_PHOTO_COMMENT = 2;
    private PostFragment postFragment;
    private EditText edtComment;
    private Retrofit retrofit;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        post_id = getIntent().getExtras().getInt("post_id");
        init();
    }

    private void init() {
        retrofit = new Client().getRetrofit(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("post_id", String.valueOf(getIntent().getExtras().getInt("post_id")));
        postFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.framePostActivyContainer, postFragment).commit();
        btnBack = findViewById(R.id.post_back_btn);
        btnBack.setOnClickListener(this);
        txtPostTitle = findViewById(R.id.txt_post_title);
        btnImage = findViewById(R.id.comment_photo_btn);
        btnSticker = findViewById(R.id.comment_sticker_btn);
        btnSend = findViewById(R.id.comment_send_btn);
        edtComment = findViewById(R.id.edt_comment);
        btnImage.setOnClickListener(this);
        btnSticker.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        getPost();
    }

    private void getPost() {
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

            case R.id.comment_send_btn: {
                sendComment(edtComment.getText().toString(), "text");
                break;
            }

            case R.id.comment_photo_btn: {
                sendPhotoComment();
                break;
            }
        }
    }

    private void sendComment(String comment, String type) {
//        if(type.equals("image") || (!edtComment.getText().toString().isEmpty())) {
            CommentService commentService = retrofit.create(CommentService.class);
            Call<StatusPOJO> call = commentService.sendComment(String.valueOf(post_id), comment, type);
            edtComment.setText("");
            call.enqueue(new Callback<StatusPOJO>() {
                @Override
                public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                    Log.d("PostActivityComment:", String.valueOf(response.code()));
                    if(response.isSuccessful() && response.body().getSuccess()) {
                        postFragment.getComments();
                    }
                }

                @Override
                public void onFailure(Call<StatusPOJO> call, Throwable t) {
                    Log.d("AddCommentErr: ", t.getMessage());
                }
            });
//        }
    }

    private void sendPhotoComment() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_ADD_PHOTO_COMMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_ADD_PHOTO_COMMENT && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                sendComment(Helper.bitmapToString(bitmap), "image");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}