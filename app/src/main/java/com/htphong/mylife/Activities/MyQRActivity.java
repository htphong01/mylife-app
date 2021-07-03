package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MyQRActivity extends AppCompatActivity {

    private ImageView qrImage;
    private Button btnScan;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);
        init();
    }

    private void init() {
        btnBack = findViewById(R.id.back_btn);
        btnScan = findViewById(R.id.scan_btn);
        qrImage = findViewById(R.id.qr_image);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnScan.setOnClickListener(v-> {
            startActivity(new Intent(MyQRActivity.this, ScanQRActivity.class));
        });
        generateQRCode();
    }

    private void generateQRCode() {
        SharedPreferences userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        String stringBitmap = userSharedPreferences.getString("QRCODE", "");
        if(stringBitmap.isEmpty()) {
            String data = userSharedPreferences.getString("id", "");
            QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
            Bitmap qrBitmap = qrgEncoder.getBitmap();
            qrImage.setImageBitmap(qrBitmap);
            SharedPreferences.Editor editor = userSharedPreferences.edit();
            editor.putString("QRCode", Helper.bitmapToString(qrBitmap));
        } else {
            Bitmap qrBitmap = Helper.stringToBitMap(stringBitmap);
            qrImage.setImageBitmap(qrBitmap);
        }
    }
}