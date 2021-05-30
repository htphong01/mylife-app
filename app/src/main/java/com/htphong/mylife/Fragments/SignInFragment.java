package com.htphong.mylife.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.htphong.mylife.Activities.AuthActivity;
import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.R;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.ProfilePOJO;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignInFragment extends Fragment {
    private View view;
    private TextInputLayout layoutEmail, layoutPassword;
    private TextInputEditText txtEmail, txtPassword;
    private TextView txtSignUp;
    private Button btnSignIn;
    private ProgressDialog dialog;
    private SharedPreferences userPref;

    public SignInFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        view = inflater.inflate(R.layout.layout_sign_in, container, false);
        init();
        return view;
    }

    private void init() {
        layoutEmail = view.findViewById(R.id.txtLayoutEmailSignIn);
        layoutPassword = view.findViewById(R.id.txtLayoutPasswordSignIn);
        txtEmail = view.findViewById(R.id.txtEmailSignIn);
        txtPassword = view.findViewById(R.id.txtPasswordSignIn);
        txtSignUp = view.findViewById(R.id.txtSignUp);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtSignUp.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignUpFragment()).commit();
        });

        btnSignIn.setOnClickListener(v->{
            //validate field to sign in
            if(validate()) {
                login();
            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!txtEmail.getText().toString().isEmpty()) {
                    layoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtPassword.getText().toString().length() > 7) {
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void login() {
        dialog.setMessage("Đang đăng nhập");
        dialog.show();

        userPref = getActivity().getApplicationContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        Retrofit retrofit = new Client().getRetrofit(getActivity().getApplicationContext());;
        UserService userService = retrofit.create(UserService.class);
        Call<ProfilePOJO> call = userService.login(txtEmail.getText().toString(), txtPassword.getText().toString());
        call.enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    User user = response.body().getUser().get(0);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("id", user.getId());
                    editor.putString("name", user.getUserName());
                    editor.putString("email", user.getEmail());
                    editor.putString("cover", user.getCover());
                    editor.putString("avatar", user.getPhoto());
                    editor.putString("created_at", user.getCreatedAt());
                    editor.putString("dateOfBirth", user.getDateOfBirth());
                    editor.putString("gender", user.getGender());
                    editor.putString("address", user.getAddress());
                    editor.putString("education", user.getEducation());
                    editor.putString("work", user.getWork());
                    editor.putString("phoneNumber", user.getPhoneNumber());
                    editor.putString("relationship", user.getRelationship());
                    editor.putString("postCount", response.body().getPostCount());
                    editor.putString("friendCount", response.body().getFriendCount());
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                    startActivity(new Intent(((AuthActivity)getContext()), HomeActivity.class));
                    ((AuthActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {
                Log.d("SignIn Error: ", t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private boolean validate() {
        if(txtEmail.getText().toString().isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email không được để trống");
            return false;
        }

        if(txtPassword.getText().toString().length() < 8) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Mật khẩu phải ít nhất có 8 kí tự");
            return false;
        }

        return true;
    }

}
