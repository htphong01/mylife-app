package com.htphong.mylife.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnAddPostImg, btnMicro;
    private ImageView newPostPhoto;
    private TextView btnAddNewPost;
    private EditText edtNewPost;
    private static final int GALLERY_ADD_POST = 2;
    private static final int SPEECH_REQUEST_CODE = 8;
    private Bitmap bitmap = null;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();
    }

    public String postBody = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void init() {
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnAddPostImg = findViewById(R.id.btn_addPostImg);
        newPostPhoto = findViewById(R.id.newPostPhoto);
        btnAddNewPost= findViewById(R.id.btn_addNewPost);
        btnMicro = findViewById(R.id.btn_add_post_micro);
        edtNewPost = findViewById(R.id.newPostText);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnAddPostImg.setOnClickListener(this);
        btnAddNewPost.setOnClickListener(this);
        btnMicro.setOnClickListener(this);
    }

    public void cancelPost(View view) {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addPostImg: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_ADD_POST);
                break;
            }

            case R.id.btn_addNewPost: {
                if(!edtNewPost.getText().toString().isEmpty()) {
                    post();
                } else {
                    Toast.makeText(this, "Test", Toast.LENGTH_SHORT);
                }
                break;
            }

            case R.id.btn_add_post_micro: {
                displaySpeechRecognizer();
                break;
            }
        }
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void post() {
        progressDialog.setMessage("Đang đăng tải bài viết");
        progressDialog.show();
        String encodedImage = Helper.bitmapToString(bitmap);
        Log.d("encodedImage", encodedImage);

        Retrofit retrofit = new Client().getRetrofit(getApplicationContext());;

        PostService postService = retrofit.create(PostService.class);
        Call<RequestBody> postCall = postService.savePost(edtNewPost.getText().toString(), encodedImage);
        postCall.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Log.d("onResponse: ", response.toString());
            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {
                Log.d("onFailure: ", t.getMessage());
            }
        });
        startActivity(new Intent(AddPostActivity.this, HomeActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_ADD_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                newPostPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            edtNewPost.setText(edtNewPost.getText().toString() + spokenText);
        }
    }
}