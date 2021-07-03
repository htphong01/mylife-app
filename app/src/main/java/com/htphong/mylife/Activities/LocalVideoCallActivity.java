package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.squareup.picasso.Picasso;
import com.stringee.call.StringeeCall;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class LocalVideoCallActivity extends AppCompatActivity {

    private FrameLayout vRemote, vLocal;

    private CircleImageView imgPhoto;
    private TextView txtName;

    private LinearLayout llInfo;

    private ImageButton btnCancel;

    private StringeeCall stringeeCall;
    private StringeeCall.SignalingState state;
    private StringeeCall.MediaState mMediaState;

    private String from;
    private String to;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video_call);

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        init();

        makeCall();
        startMusic();
    }

    private void startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.send_call_sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void makeCall() {
        stringeeCall = new StringeeCall(ChattingActivity.stringeeClient, from, to);
        stringeeCall.setVideoCall(true);
        stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {
                state = signalingState;
                switch (signalingState) {
                    case CALLING: {
                        break;
                    }

                    case RINGING: {
                        break;
                    }

                    case ANSWERED: {
                        mediaPlayer.stop();
                        vLocal.setVisibility(View.VISIBLE);
                        llInfo.setVisibility(View.GONE);
                        break;
                    }

                    case BUSY:
                    case ENDED: {
                        mediaPlayer.stop();
                        finish();
                        break;
                    }
                }
            }

            @Override
            public void onError(StringeeCall stringeeCall, int i, String s) {

            }

            @Override
            public void onHandledOnAnotherDevice(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s) {

            }

            @Override
            public void onMediaStateChange(StringeeCall stringeeCall, StringeeCall.MediaState mediaState) {
                runOnUiThread(() -> {
                    mMediaState = mediaState;
                    if(mediaState == StringeeCall.MediaState.CONNECTED) {
                        if(state == StringeeCall.SignalingState.ANSWERED) {
                            vLocal.setVisibility(View.VISIBLE);
                            llInfo.setVisibility(View.GONE);
                        }
                    }

                });
            }

            @Override
            public void onLocalStream(StringeeCall stringeeCall) {
                runOnUiThread(() -> {
                    vLocal.removeAllViews();
                    vLocal.addView(stringeeCall.getLocalView());
                    stringeeCall.renderLocalView(true);
                });
            }

            @Override
            public void onRemoteStream(StringeeCall stringeeCall) {
                runOnUiThread(() -> {
                    vRemote.removeAllViews();
                    vRemote.addView(stringeeCall.getRemoteView());
                    stringeeCall.renderRemoteView(false);
                });
            }

            @Override
            public void onCallInfo(StringeeCall stringeeCall, JSONObject jsonObject) {

            }
        });
        stringeeCall.makeCall();
    }

    private void init() {
        SharedPreferences roomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);
        vRemote = findViewById(R.id.local_video_call_frame_remote);
        vLocal = findViewById(R.id.local_video_call_frame_local);
        vLocal.setVisibility(View.GONE);

        btnCancel = findViewById(R.id.local_video_call_cancel_btn);
        imgPhoto = findViewById(R.id.local_video_call_photo);
        txtName  = findViewById(R.id.local_video_call_name);
        Picasso.get().load(Constant.DOMAIN + roomSharedPreferences.getString("room_photo", "")).resize(450,450).centerCrop().into(imgPhoto);
        txtName.setText(roomSharedPreferences.getString("room_name", ""));

        llInfo = findViewById(R.id.local_video_call_infor);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringeeCall != null) {
                    stringeeCall.hangup();
                    finish();
                }
                mediaPlayer.stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}