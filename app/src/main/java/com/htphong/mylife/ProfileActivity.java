package com.htphong.mylife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Fragments.ProfileAlbumFragment;
import com.htphong.mylife.Fragments.ProfilePostFragment;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.ProfilePOJO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static User user = new User();
    private CircleImageView imgProfileAvatar;
    private TextView txtProfileName, txtProfileFriendCount, txtProfilePostCount, txtProfileCreatedAt;
    private ImageView imgProfileCover;
    private LinearLayout profilePost, profileAlbum;
    private ArrayList<Post> list = new ArrayList<>();
    private ImageButton btnBack;
    private FragmentManager fragmentManager;
    private Button btnFriend, btnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        getProfile();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameProfileContainer, new ProfilePostFragment()).commit();
        btnBack = findViewById(R.id.btn_search_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setBackgroundResource(R.drawable.button_pressed);
                onBackPressed();
            }
        });
        imgProfileAvatar = findViewById(R.id.img_profile_avatar);
        txtProfileName = findViewById(R.id.txt_profile_name);
        imgProfileCover = findViewById(R.id.img_profile_cover);
        profilePost = findViewById(R.id.profile_fragment_post);
        profileAlbum = findViewById(R.id.profile_fragment_album);
        profilePost.setOnClickListener(this);
        profileAlbum.setOnClickListener(this);
        txtProfileFriendCount = findViewById(R.id.txt_profile_friend_count);
        txtProfilePostCount = findViewById(R.id.txt_profile_post_count);
        txtProfileCreatedAt = findViewById(R.id.txt_profile_created_at);
        btnAddFriend = findViewById(R.id.btn_profile_add_friend);
        btnAddFriend.setOnClickListener(this);
        btnFriend = findViewById(R.id.btn_profile_friend);
    }

    private void getProfile() {
        Retrofit retrofit = new Client().getRetrofit(ProfileActivity.this);;
        SharedPreferences userTargetShared = getApplicationContext().getSharedPreferences("user_target", getApplicationContext().MODE_PRIVATE);

        UserService userService = retrofit.create(UserService.class);
        Call<ProfilePOJO> profileCall = userService.getFriend(userTargetShared.getString("id", "1"));
        profileCall.enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                user = response.body().getUser().get(0);
                Picasso.get().load(Constant.DOMAIN + user.getPhoto()).resize(350, 350).centerCrop().into(imgProfileAvatar);
                Picasso.get().load(Constant.DOMAIN + user.getCover()).into(imgProfileCover);
                txtProfileName.setText(user.getUserName());
                txtProfileFriendCount.setText(response.body().getFriendCount().toString());
                txtProfilePostCount.setText(response.body().getPostCount().toString());
                txtProfileCreatedAt.setText("Đã tham gia vào " + Constant.convertDateTime(user.getCreatedAt()));
                if(response.body().getIsFriend()) {
                    btnAddFriend.setVisibility(View.GONE);
                    btnFriend.setVisibility(View.VISIBLE);
                } else {
                    btnAddFriend.setVisibility(View.VISIBLE);
                    btnFriend.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {
                Log.d("Profile: ", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.profile_fragment_post: {
                fragmentManager.beginTransaction().replace(R.id.frameProfileContainer, new ProfilePostFragment()).commit();
                break;
            }

            case R.id.profile_fragment_album: {
                fragmentManager.beginTransaction().replace(R.id.frameProfileContainer, new ProfileAlbumFragment()).commit();
                break;
            }

            case R.id.btn_profile_add_friend: {

                break;
            }
        }
    }
}