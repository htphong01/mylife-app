package com.htphong.mylife.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.ProfilePOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserInforActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextInputLayout layoutName;
    private TextInputEditText txtName, txtDoBUserInfor;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView imgUserInfo;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPreferences;
    private ProgressDialog dialog;
    private RadioGroup radioGenderGroup;
    private RadioButton radioGender;
    private SharedPreferences userPref;
    private String gender = null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        init();
    }

    private void init() {
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutName = findViewById(R.id.txtLayoutName);
        txtName = findViewById(R.id.txtNameUserInfor);
        txtDoBUserInfor = findViewById(R.id.txt_DoB_User_Infor);
        txtSelectPhoto = findViewById(R.id.txtSelectPhoto);
        btnContinue = findViewById(R.id.btnContinue);
        imgUserInfo = findViewById(R.id.imgUserInfo);
        radioGenderGroup = findViewById(R.id.radio_gender_user_infor);
        radioGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGender = (RadioButton) findViewById(checkedId);
                gender = radioGender.getText().toString();
            }
        });
        txtDoBUserInfor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDatePickerDialog();
            }
        });

        txtSelectPhoto.setOnClickListener(this);

        btnContinue.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            imgUserInfo.setImageURI(imgUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        if (txtName.getText().toString().isEmpty()){
            layoutName.setErrorEnabled(true);
            layoutName.setError("Họ tên không được bỏ trống");
            return false;
        }

        return true;
    }

    private void saveUserInfo() {
        dialog.setMessage("Đang lưu lại");
        dialog.show();
        Retrofit retrofit = new Client().getRetrofit(getApplicationContext());;

        UserService userService = retrofit.create(UserService.class);
        retrofit2.Call<ProfilePOJO> call = userService.saveUserInfor(txtName.getText().toString(), bitmapToString(bitmap), Helper.saveBirthDay(txtDoBUserInfor.getText().toString()), gender);
        call.enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(retrofit2.Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                Log.d("user token: ", userPref.getString("token", "khong token"));
                if (response.isSuccessful() && response.body().getSuccess()) {
                    User user = response.body().getUser().get(0);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("name", user.getUserName());
                    editor.putString("avatar", user.getPhoto());
                    editor.putString("dateOfBirth", user.getDateOfBirth());
                    editor.putString("gender", user.getGender());
                    editor.apply();
                    startActivity(new Intent(UserInforActivity.this, HomeActivity.class));
                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Log.d("UserInfor: ", user.getDateOfBirth());

                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {
                Log.d("user token: ", userPref.getString("token", "khong token"));
                dialog.dismiss();
            }
        });
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array,Base64.NO_WRAP);
        }

        return "";
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().YEAR,
                Calendar.getInstance().MONTH,
                Calendar.getInstance().DAY_OF_MONTH);
        datePickerDialog.show();
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.txtSelectPhoto: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_ADD_PROFILE);
                break;
            }
            case R.id.btnContinue: {
                if(validate()) {
                    saveUserInfo();
                }
                break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        txtDoBUserInfor.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
    }
}