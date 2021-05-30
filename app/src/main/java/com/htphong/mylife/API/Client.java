package com.htphong.mylife.API;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Activities.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private MainActivity mainActivity;

    public Retrofit getRetrofit(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + sharedPreferences.getString("token", ""))
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMAIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
