package com.htphong.mylife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htphong.mylife.API.ChattingService;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.FriendService;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Fragments.ProfileAlbumFragment;
import com.htphong.mylife.Fragments.ProfilePostFragment;
import com.htphong.mylife.Models.Friend;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.FriendPOJO;
import com.htphong.mylife.POJO.MessagePOJO;
import com.htphong.mylife.POJO.ProfilePOJO;
import com.htphong.mylife.POJO.RoomPOJO;
import com.htphong.mylife.POJO.StatusPOJO;
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
    private Button btnFriend, btnAddFriend, btnAcceptFriend, btnRemoveFriend, btnProfileChat;
    private static String friendInvitationId = "0";
    private Retrofit retrofit;
    private FriendService friendService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        retrofit = new Client().getRetrofit(ProfileActivity.this);
        friendService = retrofit.create(FriendService.class);

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
        btnAcceptFriend = findViewById(R.id.btn_profile_accept_friend);
        btnRemoveFriend = findViewById(R.id.btn_profile_remove_friend);
        btnAcceptFriend.setOnClickListener(this);
        btnRemoveFriend.setOnClickListener(this);
        btnProfileChat = findViewById(R.id.btn_profile_chat);
        btnProfileChat.setOnClickListener(this);
        getProfile();
    }



    private void getProfile() {
        SharedPreferences userTargetShared = getApplicationContext().getSharedPreferences("user_target", getApplicationContext().MODE_PRIVATE);
        UserService userService = retrofit.create(UserService.class);
        Call<ProfilePOJO> profileCall = userService.getFriend(userTargetShared.getString("id", "1"));
        profileCall.enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    friendInvitationId = String.valueOf(response.body().getFriendInvitationId());
                    user = response.body().getUser().get(0);
                    Picasso.get().load(Constant.DOMAIN + user.getPhoto()).resize(350, 350).centerCrop().into(imgProfileAvatar);
                    Picasso.get().load(Constant.DOMAIN + user.getCover()).into(imgProfileCover);
                    txtProfileName.setText(user.getUserName());
                    txtProfileFriendCount.setText(response.body().getFriendCount().toString());
                    txtProfilePostCount.setText(response.body().getPostCount().toString());
                    txtProfileCreatedAt.setText("Đã tham gia vào " + Constant.convertDateTime(user.getCreatedAt()));
                    setFriendButton(response.body().getStatusFriend(), response.body().getRequestSendByYou());
                }
            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {
                Log.d("ProfileErr: ", t.getMessage());
            }
        });
    }

    private void addFriend() {
        Call<FriendPOJO> call = friendService.addFriend(String.valueOf(user.getId()));
        call.enqueue(new Callback<FriendPOJO>() {
            @Override
            public void onResponse(Call<FriendPOJO> call, Response<FriendPOJO> response) {
                btnRemoveFriend.setVisibility(View.VISIBLE);
                btnAddFriend.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FriendPOJO> call, Throwable t) {
                Log.d("ProfileAddErr: ", t.getMessage());
            }
        });
    }

    private void acceptFriend() {
        Call<FriendPOJO> call = friendService.acceptFriend(friendInvitationId);
        call.enqueue(new Callback<FriendPOJO>() {
            @Override
            public void onResponse(Call<FriendPOJO> call, Response<FriendPOJO> response) {
                btnAcceptFriend.setVisibility(View.GONE);
                btnFriend.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<FriendPOJO> call, Throwable t) {

            }
        });
    }

    private void removeFriendRequest() {
        Call<StatusPOJO> call = friendService.removeFriendRequest(friendInvitationId);
        call.enqueue(new Callback<StatusPOJO>() {
            @Override
            public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                btnRemoveFriend.setVisibility(View.GONE);
                btnAddFriend.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<StatusPOJO> call, Throwable t) {

            }
        });
    }

    private void setFriendButton(Integer statusFriend, Boolean requestSendByYou) {
        if(statusFriend == 0) {

        } else if(statusFriend == 1) {
            btnAddFriend.setVisibility(View.GONE);
            if(requestSendByYou) {
                btnRemoveFriend.setVisibility(View.VISIBLE);
            } else {
                btnAcceptFriend.setVisibility(View.VISIBLE);
            }
        } else {
            btnAddFriend.setVisibility(View.GONE);
            btnFriend.setVisibility(View.VISIBLE);
        }
    }

    private void gotoChatting() {
        SharedPreferences userTargetShared = getApplicationContext().getSharedPreferences("user_target", getApplicationContext().MODE_PRIVATE);
        ChattingService chattingService = retrofit.create(ChattingService.class);
        Call<MessagePOJO> call = chattingService.getListMessage("0", userTargetShared.getString("id", "1"));
        call.enqueue(new Callback<MessagePOJO>() {
            @Override
            public void onResponse(Call<MessagePOJO> call, Response<MessagePOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    SharedPreferences chatRoom = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);
                    SharedPreferences.Editor editor = chatRoom.edit();
                    editor.putString("room_id", String.valueOf(response.body().getRoom_id()));
                    editor.apply();
                    if(chatRoom.getString("room_id", "12").equals(response.body().getRoom_id())) {
                        startActivity(new Intent(ProfileActivity.this, ChattingActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<MessagePOJO> call, Throwable t) {

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
                addFriend();
                break;
            }

            case R.id.btn_profile_accept_friend: {
                acceptFriend();
                break;
            }

            case R.id.btn_profile_remove_friend: {
                removeFriendRequest();
                break;
            }

            case R.id.btn_profile_chat: {
                gotoChatting();
                break;
            }
        }
    }

}