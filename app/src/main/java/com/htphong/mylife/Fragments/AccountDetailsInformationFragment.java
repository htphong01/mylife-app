package com.htphong.mylife.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.ProfilePOJO;
import com.htphong.mylife.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountDetailsInformationFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private FragmentManager fragmentManager;
    private SharedPreferences useSharedPreferences;
    private Button accountBasicBtn, updateBtn;
    private EditText edtAddress, edtEducation, edtWork, edtPhoneNumber, edtRelationship;
    private String address = null, education = null, work = null, phoneNumber = null, relationship = null;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account_details_information, container, false);
        context = container.getContext();
        init();
        return view;
    }

    private void init() {
        useSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        fragmentManager = getFragmentManager();
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        accountBasicBtn = view.findViewById(R.id.account_basic_btn);
        accountBasicBtn.setOnClickListener(this);
        edtAddress = view.findViewById(R.id.edt_account_details_address);
        edtEducation = view.findViewById(R.id.edt_account_details_education);
        edtWork = view.findViewById(R.id.edt_account_details_work);
        edtPhoneNumber = view.findViewById(R.id.edt_account_details_phone);
        edtRelationship = view.findViewById(R.id.edt_account_details_relationship);
        updateBtn = view.findViewById(R.id.account_details_update_btn);
        updateBtn.setOnClickListener(this);
        setUserInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_basic_btn: {
                fragmentManager.beginTransaction().replace(R.id.frameAccountInformationContainer, new AccountBasicInformationFragment()).commit();
                break;
            }

            case R.id.account_details_update_btn: {
                updateUserInformation();
                break;
            }
        }
    }

    private void setUserInformation() {
        edtAddress.setText(useSharedPreferences.getString("address", ""));
        edtEducation.setText(useSharedPreferences.getString("education", ""));
        edtWork.setText(useSharedPreferences.getString("work", ""));
        edtPhoneNumber.setText(useSharedPreferences.getString("phoneNumber", ""));
        edtRelationship.setText(useSharedPreferences.getString("relationship", ""));
    }

    private void getUserInformation() {
        address = edtAddress.getText().toString();
        education = edtEducation.getText().toString();
        work = edtWork.getText().toString();
        phoneNumber = edtPhoneNumber.getText().toString();
        relationship = edtRelationship.getText().toString();
    }

    private void updateUserInformation() {
        getUserInformation();
        dialog.setMessage("Đang cập nhật thông tin cá nhân");
        dialog.show();
        Retrofit retrofit = new Client().getRetrofit(getActivity().getApplicationContext());;
        UserService userService = retrofit.create(UserService.class);
        Call<ProfilePOJO> call = userService.updateUserInfor(null, null, null, address, education, work, phoneNumber, relationship);
        call.enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    User user = response.body().getUser().get(0);
                    SharedPreferences.Editor editor = useSharedPreferences.edit();
                    editor.putString("address", address);
                    editor.putString("education", education);
                    editor.putString("work", work);
                    editor.putString("phoneNumber", phoneNumber);
                    editor.putString("relationship", relationship);

                    editor.apply();
                    Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {
                dialog.dismiss();
                Log.d("Account Basic Infor: " , t.getMessage());
            }
        });
    }
}
