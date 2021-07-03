package com.htphong.mylife.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.htphong.mylife.Fragments.AccountFragment;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.R;

public class ScanQRActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        init();
    }

    private void init() {
        codeScannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goToProfile(result.getText());
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    private void goToProfile(String id) {
        Intent intent;
        SharedPreferences userTargetPref = getApplicationContext().getSharedPreferences("user_target", MODE_PRIVATE);
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userTargetPref.edit();
        editor.putString("id", id);
        editor.apply();
        if(id.equals(userPref.getString("id", ""))) {
            Toast.makeText(this, "Đây là QR Code của bạn", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}