package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.htphong.mylife.R;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class InComingVideoCallActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnReject, btnAccept;
    private SharedPreferences roomSharedPreferences;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.180:3000/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_coming_video_call);
        mSocket.connect();
        socketOnEvents();
        roomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);
        mSocket.emit("newConnector", roomSharedPreferences.getString("room_id", "12"));
        init();
    }

    private void init() {
        btnReject = findViewById(R.id.btn_reject_video_call);
        btnAccept = findViewById(R.id.btn_accept_video_call);
        btnReject.setOnClickListener(this);
        btnAccept.setOnClickListener(this);

    }

    private void socketOnEvents() {
        mSocket.on("cancelVideoCall", onCancelVideoCall);
    }

    private Emitter.Listener cancelVideoCall = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            });
        }
    };

    private Emitter.Listener onCancelVideoCall = args -> runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("CancelVideoCall: ", "OK");
            mSocket.emit("stopConnect", roomSharedPreferences.getString("room_id", "12"));
            mSocket.disconnect();
            onBackPressed();
        }
    });


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reject_video_call: {
                mSocket.emit("rejectVideoCall", roomSharedPreferences.getString("room_id", "12"));
                mSocket.emit("stopConnect", roomSharedPreferences.getString("room_id", "12"));
                mSocket.disconnect();
                super.onBackPressed();
                break;
            }

            case R.id.btn_accept_video_call: {
                mSocket.emit("acceptVideoCall", roomSharedPreferences.getString("room_id", "12"));
                break;
            }
        }
    }
}