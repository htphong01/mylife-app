package com.htphong.mylife.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.htphong.mylife.R;
import com.stringee.call.StringeeCall;
import com.stringee.common.StringeeConstant;
import com.stringee.listener.StatusListener;

import org.json.JSONObject;

public class RemoteVideoCallActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout vRemote, vLocal;

    private ImageButton btnReject, btnAccept, btnCancel;

    private StringeeCall stringeeCall;
    private StringeeCall.SignalingState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_video_call);

        init();
        
        initAnswer();

        requirePermission();
    }

    private void requirePermission() {
        ActivityCompat.requestPermissions(RemoteVideoCallActivity.this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(RemoteVideoCallActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initAnswer() {
        String callId = getIntent().getStringExtra("call_id");
        stringeeCall = ChattingActivity.callsMap.get(callId);
        stringeeCall.enableVideo(true);
        stringeeCall.setQuality(StringeeConstant.QUALITY_FULLHD);
        stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {
                state = signalingState;
                if(state == StringeeCall.SignalingState.ENDED) {
                    finish();
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

            }

            @Override
            public void onLocalStream(StringeeCall stringeeCall) {
                runOnUiThread(() -> {
                    vLocal.addView(stringeeCall.getLocalView());
                    stringeeCall.renderLocalView(true);
                });
            }

            @Override
            public void onRemoteStream(StringeeCall stringeeCall) {
                runOnUiThread(() -> {

                    vRemote.addView(stringeeCall.getRemoteView());
                    stringeeCall.renderRemoteView(false);
                });
            }

            @Override
            public void onCallInfo(StringeeCall stringeeCall, JSONObject jsonObject) {

            }
        });
        stringeeCall.ringing(new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d("Stringee: ", "Ringing");
            }
        });
        Log.d("StringeeAnswer", stringeeCall.getCallId());
    }

    private void init() {
        vLocal = findViewById(R.id.remote_video_call_frame_local);
        vRemote = findViewById(R.id.remote_video_call_frame_remote);
        btnReject = findViewById(R.id.remote_video_call_reject_btn);
        btnAccept = findViewById(R.id.remote_video_call_accept_btn);
        btnCancel = findViewById(R.id.remote_video_call_cancel_btn);
        btnCancel.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remote_video_call_accept_btn: {
                if(stringeeCall != null) {
                    Log.d("RemoteVideoCall", "answer");
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    stringeeCall.answer();
                }
                break;
            }

            case R.id.remote_video_call_reject_btn: {
                if(stringeeCall != null) {
                    stringeeCall.reject();
                    finish();
                    Log.d("RemoteVideoCall", "reject");
                }
                break;
            }

            case R.id.remote_video_call_cancel_btn: {
                if(stringeeCall != null) {
                    stringeeCall.hangup();
                    finish();
                    Log.d("RemoteVideoCall", "reject");
                }
                break;
            }
        }
    }
}