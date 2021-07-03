package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        init();
    }

    private void init() {
        image = findViewById(R.id.image);
        Picasso.get().load(Constant.DOMAIN + getIntent().getExtras().getString("url")).into(image);
    }
}