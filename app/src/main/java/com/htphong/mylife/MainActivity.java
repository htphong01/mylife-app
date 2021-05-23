package com.htphong.mylife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // delay app 1.5s and then any thing in run method will run
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences userPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
                boolean isLoggedIn = userPreferences.getBoolean("isLoggedIn", false);
                Log.d("MainActivity isLoggin: ", String.valueOf(isLoggedIn));
                if(isLoggedIn) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    isFirstTime();
                }
//                isFirstTime();

            }
        }, 1500);
    }

    private void isFirstTime() {
        //for checking if the app is running for the very first time
        //we need to save a value to shared preferences
        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime",true);
        //default value true
        if (isFirstTime){
            // if its true then its first time and we will change it false
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime",false);
            editor.apply();

            // start Onboard activity
            startActivity(new Intent(MainActivity.this,OnBoardActivity.class));
            finish();
        }
        else {
            //start Auth Activity
            startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
            finish();
        }
    }
}