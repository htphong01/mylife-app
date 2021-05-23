package com.htphong.mylife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnToLogin, btnToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    public void init() {
        btnToLogin = findViewById(R.id.btn_to_login);
        btnToRegister = findViewById(R.id.btn_to_register);

        btnToLogin.setOnClickListener(this);
        btnToRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_login: {
                Intent intent = new Intent(WelcomeActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.btn_to_register: {

                break;
            }
        }
    }
}