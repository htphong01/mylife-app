package com.htphong.mylife.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htphong.mylife.API.ChattingService;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.Adapters.ImageAdapter;
import com.htphong.mylife.Fragments.AccountFragment;
import com.htphong.mylife.Models.Room;
import com.htphong.mylife.POJO.RoomPOJO;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChattingSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack, icFind, icProfile, icNotification, icTask;
    private LinearLayout btnFindMessage, btnProfile, btnNotification, btnTask, btnChangeNickname, btnImages, btnCreateGroup, btnBlock, btnTaskManagement, btnDelete;
    private CircleImageView chatAvatar;
    private TextView chatName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> listImages = new ArrayList<>();
    private SharedPreferences roomSharedPreferences;
    private static int user_id = 1;
    private static String room_name = null;
    private EditText input;

    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_setting);
        init();
    }

    private void init() {
        retrofit = new Client().getRetrofit(getApplicationContext());
        roomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);

        recyclerView = findViewById(R.id.chat_setting_recycler_image);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setItemViewCacheSize(10);
        adapter = new ImageAdapter(listImages);
        recyclerView.setAdapter(adapter);

        chatAvatar = findViewById(R.id.chat_setting_avatar);
        chatName = findViewById(R.id.chat_setting_name);

        icFind = findViewById(R.id.chat_setting_ic_search);
        icProfile = findViewById(R.id.chat_setting_ic_profile);
        icNotification = findViewById(R.id.chat_setting_ic_notification);
        icTask = findViewById(R.id.chat_setting_ic_task);

        btnBack = findViewById(R.id.chat_setting_back_btn);
        btnFindMessage = findViewById(R.id.chat_setting_find_btn);
        btnProfile = findViewById(R.id.chat_setting_profile_btn);
        btnNotification = findViewById(R.id.chat_setting_notification_btn);
        btnTask = findViewById(R.id.chat_setting_task_btn);
        btnChangeNickname = findViewById(R.id.chat_setting_change_nickname_btn);
        btnImages = findViewById(R.id.chat_setting_images_btn);
        btnCreateGroup = findViewById(R.id.chat_setting_create_group_btn);
        btnBlock = findViewById(R.id.chat_setting_block_btn);
        btnTaskManagement = findViewById(R.id.chat_setting_task_management_btn);
        btnDelete = findViewById(R.id.chat_setting_delete_btn);

        icFind.setOnClickListener(this);
        icProfile.setOnClickListener(this);
        icNotification.setOnClickListener(this);
        icTask.setOnClickListener(this);

        btnBack.setOnClickListener(this);
        btnFindMessage.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
        btnTask.setOnClickListener(this);
        btnChangeNickname.setOnClickListener(this);
        btnImages.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);
        btnBlock.setOnClickListener(this);
        btnTaskManagement.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        getRoomInformation();
    }

    private void getRoomInformation() {
        chatName.setText(roomSharedPreferences.getString("room_name", ""));
        Picasso.get().load(Constant.DOMAIN + roomSharedPreferences.getString("room_photo", "")).resize(350,350).centerCrop().into(chatAvatar);

        retrofit.create(ChattingService.class).getChatRoom(roomSharedPreferences.getString("room_id", "12"))
        .enqueue(new Callback<RoomPOJO>() {
            @Override
            public void onResponse(Call<RoomPOJO> call, Response<RoomPOJO> response) {
                Log.d("ChatSetting: ", response.body().toString());
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Room room = (Room)response.body().getRooms().get(0);
                    listImages.addAll(room.getImages());
                    adapter.notifyDataSetChanged();
                    user_id = room.getUserId();
                    room_name = room.getName();
                }
            }

            @Override
            public void onFailure(Call<RoomPOJO> call, Throwable t) {
                Log.d("ChatSetting: ", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_setting_back_btn: {
                startActivity(new Intent(ChattingSettingActivity.this, ChattingActivity.class));
                break;
            }

            case R.id.chat_setting_task_btn: {
                startActivity(new Intent(ChattingSettingActivity.this, GivingTaskActivity.class));
                break;
            }

            case R.id.chat_setting_ic_profile:
            case R.id.chat_setting_profile_btn: {
                gotoProfile();
                break;
            }

            case R.id.chat_setting_change_nickname_btn: {
                openDialogNickName();
                break;
            }

            case R.id.chat_setting_images_btn: {
                showImageGallery();
                break;
            }

            case R.id.chat_setting_task_management_btn: {
                startActivity(new Intent(ChattingSettingActivity.this, TaskManagementActivity.class));
                break;
            }
        }
    }

    private void showImageGallery() {
    }

    private void openDialogNickName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChattingSettingActivity.this);
        builder.setTitle("Đổi tên gợi nhớ");
        builder.setIcon(R.drawable.ic_edit_35);
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_edittext, null);
        builder.setView(view);
        input = (EditText) view.findViewById(R.id.edt_layout_dialog);
        input.setText(room_name);
        builder.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(!input.getText().toString().isEmpty()) {
                    chatName.setText(input.getText().toString());
                    changeNickname(input.getText().toString());
                    room_name = input.getText().toString();
                }
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);

            }
        });



        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void changeNickname(String nickname) {
        retrofit.create(ChattingService.class)
                .changeNickname(String.valueOf(user_id), roomSharedPreferences.getString("room_id", "12"), nickname)
                .enqueue(new Callback<StatusPOJO>() {
                    @Override
                    public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {

                    }

                    @Override
                    public void onFailure(Call<StatusPOJO> call, Throwable t) {

                    }
                });
    }

    private void gotoProfile() {
        Intent intent;
        SharedPreferences userTargetPref = getApplicationContext().getSharedPreferences("user_target", MODE_PRIVATE);
        SharedPreferences.Editor editor = userTargetPref.edit();
        editor.putString("id", String.valueOf(user_id));
        editor.apply();
        intent = new Intent(ChattingSettingActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}