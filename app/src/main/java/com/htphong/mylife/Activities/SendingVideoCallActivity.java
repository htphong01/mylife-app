package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SendingVideoCallActivity extends AppCompatActivity {

    private CircleImageView imgVideoCallPhoto;
    private TextView txtVideoCallName;
    private ImageButton cancelBtn;
    SharedPreferences roomSharedPreferences;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.180:3000/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        roomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_video_call);
        mSocket.connect();
        mSocket.on("rejectVideoCall", rejectVideoCall);
        mSocket.emit("newConnector", roomSharedPreferences.getString("room_id", "12"));
        init();
    }

    private Emitter.Listener rejectVideoCall = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.emit("stopConnect", roomSharedPreferences.getString("room_id", "12"));
                    mSocket.disconnect();
                    onBackPressed();
                }
            });
        }
    };

    private void init() {
        SharedPreferences roomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);
        imgVideoCallPhoto = findViewById(R.id.img_video_call_photo);
        txtVideoCallName = findViewById(R.id.txt_video_call_name);
        cancelBtn= findViewById(R.id.btn_cancel_video_call);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("cancelVideoCall", roomSharedPreferences.getString("room_id", "12"));
                mSocket.emit("stopConnect", roomSharedPreferences.getString("room_id", "12"));
                mSocket.disconnect();
                onBackPressed();
            }
        });
        Picasso.get().load(Constant.DOMAIN + roomSharedPreferences.getString("room_photo", "")).resize(450,450).centerCrop().into(imgVideoCallPhoto);
        txtVideoCallName.setText(roomSharedPreferences.getString("room_name", ""));

    }
}