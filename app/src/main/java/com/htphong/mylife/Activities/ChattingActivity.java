package com.htphong.mylife.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.htphong.mylife.API.ChattingService;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.Adapters.MessageAdapter;
import com.htphong.mylife.Models.Message;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.MessagePOJO;
import com.htphong.mylife.POJO.RoomPOJO;
import com.htphong.mylife.POJO.UserPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;
import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backBtn, callBtn, videoCallBtn, barBtn, moreBtn, imageBtn, emotionBtn, heartBtn, sendBtn;
    private CircleImageView chatRoomAvatar;
    private TextView chatRoomName;
    private EditText edtChatMessage;
    private RecyclerView recyclerChatting;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private static String room_id = "12";
    private Retrofit retrofit;
    private ChattingService chattingService;
    private static final int GALLERY_SEND_PHOTO_MESSAGE = 3;
    private Bitmap bitmap;
    private SharedPreferences userSharedPreferences;
    private SharedPreferences chatRoomSharedPreferences;

    public static StringeeClient stringeeClient;
    private String stringeeToken = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSzJmQ29oeFRHZkx6ZkNac01YOVFHbldwSVZiYnphQzgtMTYyMjUzOTYwMSIsImlzcyI6IlNLMmZDb2h4VEdmTHpmQ1pzTVg5UUduV3BJVmJiemFDOCIsImV4cCI6MTYyNTEzMTYwMSwidXNlcklkIjoiMSJ9.IpQsU0n0pEc9nUtttsWnchgqrh7hJXxEqUrlp44S4sI";

    public static Map<String, StringeeCall> callsMap = new HashMap<>();

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.180:3000/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        userSharedPreferences = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
        chatRoomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", getApplicationContext().MODE_PRIVATE);
        mSocket.connect();

        socketOnEvents();
        mSocket.emit("newConnector", chatRoomSharedPreferences.getString("room_id", "12"));

        init();

        initStringee();

        requirePermission();
    }

    private void initStringee() {
        stringeeClient = new StringeeClient(ChattingActivity.this);
        stringeeClient.setConnectionListener(new StringeeConnectionListener() {
            @Override
            public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
                runOnUiThread(() -> {
                    Log.d("stringeeClient", stringeeClient.getUserId());
                });
            }

            @Override
            public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {

            }

            @Override
            public void onIncomingCall(StringeeCall stringeeCall) {
                runOnUiThread(() -> {
                    callsMap.put(stringeeCall.getCallId(), stringeeCall);
                    Intent intent = new Intent(ChattingActivity.this, RemoteVideoCallActivity.class);
                    intent.putExtra("call_id", stringeeCall.getCallId());
                    startActivity(intent);
                });
            }

            @Override
            public void onIncomingCall2(StringeeCall2 stringeeCall2) {

            }

            @Override
            public void onConnectionError(StringeeClient stringeeClient, StringeeError stringeeError) {

            }

            @Override
            public void onRequestNewToken(StringeeClient stringeeClient) {

            }

            @Override
            public void onCustomMessage(String s, JSONObject jsonObject) {

            }

            @Override
            public void onTopicMessage(String s, JSONObject jsonObject) {

            }
        });

        stringeeClient.connect(userSharedPreferences.getString("stringeeToken", stringeeToken));
    }


    private void requirePermission() {
        ActivityCompat.requestPermissions(ChattingActivity.this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(ChattingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void init() {
        retrofit = new Client().getRetrofit(getApplicationContext());
        chattingService = retrofit.create(ChattingService.class);

        backBtn = findViewById(R.id.chat_ic_back);
        callBtn = findViewById(R.id.chat_ic_call);
        videoCallBtn = findViewById(R.id.chat_ic_video_call);
        barBtn = findViewById(R.id.chat_ic_bar);
        moreBtn = findViewById(R.id.chat_ic_more);
        imageBtn = findViewById(R.id.chat_ic_image);
        emotionBtn = findViewById(R.id.chat_ic_emotion);
        heartBtn = findViewById(R.id.chat_ic_heart);
        sendBtn = findViewById(R.id.chat_ic_send);
        chatRoomAvatar = findViewById(R.id.img_conversation_avatar);
        chatRoomName = findViewById(R.id.txt_conversation_name);
        edtChatMessage = findViewById(R.id.edt_chat_message);
        edtChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtChatMessage.getText().toString().isEmpty()) {
                    sendBtn.setVisibility(View.VISIBLE);
                    heartBtn.setVisibility(View.GONE);

                } else {
                    sendBtn.setVisibility(View.GONE);
                    heartBtn.setVisibility(View.VISIBLE);
                }
                recyclerChatting.scrollToPosition(messageArrayList.size() - 1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerChatting = findViewById(R.id.recycler_conversation);
        recyclerChatting.setItemViewCacheSize(10);
        recyclerChatting.setDrawingCacheEnabled(true);
        recyclerChatting.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerChatting.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        messageAdapter = new MessageAdapter(messageArrayList, this);
        recyclerChatting.setAdapter(messageAdapter);

        sendBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        callBtn.setOnClickListener(this);
        videoCallBtn.setOnClickListener(this);
        barBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        emotionBtn.setOnClickListener(this);
        heartBtn.setOnClickListener(this);
        getRoomInformation();
        getMessage();
    }

    private void socketOnEvents() {
        mSocket.on("receiveMessage", onNewMessage);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.chat_ic_send: {
                String message = edtChatMessage.getText().toString();
                if(!message.isEmpty()) {
                    edtChatMessage.setText("");
                    sendMessage(message, "text");
                }
                break;
            }

            case R.id.chat_ic_back: {
                super.onBackPressed();
                mSocket.emit("stopConnect", chatRoomSharedPreferences.getString("room_id", "12"));
                break;
            }

            case R.id.chat_ic_heart: {

                break;
            }

            case R.id.chat_ic_image: {
                sendPhotoMessage();
                break;
            }

            case R.id.chat_ic_call: {
                sendPhoneCall();
                break;
            }

            case R.id.chat_ic_video_call: {
                sendVideoCall();
                break;
            }

            case R.id.chat_ic_bar: {
                actionIconBarClick();
                break;
            }

            case R.id.chat_ic_more: {
                actionIconMoreClick();
                break;
            }

            case R.id.chat_ic_emotion: {

                break;
            }
        }
    }

    private void actionIconMoreClick() {
    }

    private void actionIconBarClick() {
        startActivity(new Intent(ChattingActivity.this, ChattingSettingActivity.class));
    }

    private void sendVideoCall() {
        ChattingService chattingService = retrofit.create(ChattingService.class);
        Call<UserPOJO> call = chattingService.getRoomUsers(chatRoomSharedPreferences.getString("room_id", "12"));
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    List<User> users = response.body().getUsers();
                    for(int i = 0; i < users.size(); i++) {
                        if(!String.valueOf(users.get(i).getId()).equals(userSharedPreferences.getString("id", "1"))) {
                            Intent intent = new Intent(ChattingActivity.this, LocalVideoCallActivity.class);
                            intent.putExtra("from", stringeeClient.getUserId());
                            intent.putExtra("to", String.valueOf(users.get(i).getId()));
                            startActivity(intent);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {

            }
        });

    }

    private void sendPhoneCall() {
    }

    private void getRoomInformation() {
        Call<RoomPOJO> call = chattingService.getChatRoom(chatRoomSharedPreferences.getString("room_id", "12"));
        call.enqueue(new Callback<RoomPOJO>() {
            @Override
            public void onResponse(Call<RoomPOJO> call, retrofit2.Response<RoomPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Picasso.get().load(Constant.DOMAIN + response.body().getRooms().get(0).getPhoto()).resize(350,350).centerCrop().into(chatRoomAvatar);
                    chatRoomName.setText(response.body().getRooms().get(0).getName());
                    SharedPreferences.Editor editor = chatRoomSharedPreferences.edit();
                    editor.putString("room_name", String.valueOf(response.body().getRooms().get(0).getName()));
                    editor.putString("room_photo", String.valueOf(response.body().getRooms().get(0).getPhoto()));
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<RoomPOJO> call, Throwable t) {

            }
        });
    }

    private void sendPhotoMessage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_SEND_PHOTO_MESSAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_SEND_PHOTO_MESSAGE && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                sendMessage(Helper.bitmapToString(bitmap), "image");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String message, String type) {
        Call<MessagePOJO> call = chattingService.sendMessage(chatRoomSharedPreferences.getString("room_id", "12"), message, type);
        call.enqueue(new Callback<MessagePOJO>() {
            @Override
            public void onResponse(Call<MessagePOJO> call, retrofit2.Response<MessagePOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Message message1 = response.body().getMessages().get(0);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", message1.getId());
                    jsonObject.addProperty("room_id", message1.getRoomId());
                    jsonObject.addProperty("user_id", message1.getUserId());
                    jsonObject.addProperty("message", message1.getMessage());
                    jsonObject.addProperty("type", message1.getType());
                    jsonObject.addProperty("status", message1.getStatus());
                    jsonObject.addProperty("created_at", message1.getCreatedAt());
                    jsonObject.addProperty("updated_at", message1.getUpdatedAt());
                    jsonObject.addProperty("nickname", message1.getNickname());
                    jsonObject.addProperty("avatar", message1.getAvatar());
                    int size = messageArrayList.size();
                    messageArrayList.add(size,message1);
                    messageAdapter.notifyItemInserted(size);
                    recyclerChatting.smoothScrollToPosition(messageArrayList.size() - 1);
                    mSocket.emit("newMessage", jsonObject);
                }
            }

            @Override
            public void onFailure(Call<MessagePOJO> call, Throwable t) {
                Log.d("SENDMESSAGE", t.getMessage());
            }
        });
    }

    private void getMessage() {
        messageArrayList.clear();
        Call<MessagePOJO> call = chattingService.getListMessage(chatRoomSharedPreferences.getString("room_id", "12"), "");
        call.enqueue(new Callback<MessagePOJO>() {
            @Override
            public void onResponse(Call<MessagePOJO> call, retrofit2.Response<MessagePOJO> response) {
                Log.d("ChattingRes:", response.body().toString());
                if(response.isSuccessful() && response.body().getSuccess()) {
                    messageArrayList.addAll(response.body().getMessages());
                    messageAdapter.notifyDataSetChanged();
                    recyclerChatting.scrollToPosition(messageArrayList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<MessagePOJO> call, Throwable t) {
                Log.d("ChattingErr:", t.getMessage());
            }
        });
    }

//    private Emitter.Listener onNewVideoCall = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(ChattingActivity.this, ReceiveVideoCallActivity.class));
//                }
//            });
//        }
//    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Message message = new Message();
                    try {
                        message.setId(Integer.parseInt(data.getString("id")));
                        message.setRoomId(Integer.parseInt(data.getString("room_id")));
                        message.setUserId(Integer.parseInt(data.getString("user_id")));
                        message.setMessage(data.getString("message"));
                        message.setType(data.getString("type"));
                        message.setStatus(Integer.parseInt(data.getString("status")));
                        message.setCreatedAt(data.getString("created_at"));
                        message.setUpdatedAt(data.getString("updated_at"));
                        message.setNickname(data.getString("nickname"));
                        message.setAvatar(data.getString("avatar"));
                        Log.d("onReceiveMess: ", message.toString());
                        if(String.valueOf(message.getRoomId()).equals(chatRoomSharedPreferences.getString("room_id", "12"))) {
                            if(!String.valueOf(message.getUserId()).equals(userSharedPreferences.getString("id", "1"))) {
                                int size = messageArrayList.size();
                                messageArrayList.add(size,message);
                                messageAdapter.notifyItemInserted(size);
                                recyclerChatting.smoothScrollToPosition(messageArrayList.size() - 1);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSocket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}