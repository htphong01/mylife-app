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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Constant;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.ProfilePOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountBasicInformationFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private CircleImageView accountBasicImg;
    private EditText edtName, edtEmail, edtDoB;
    private SharedPreferences useSharedPreferences;
    private Button accountDetailsBtn, accountBasicUpdateBtn;
    private FragmentManager fragmentManager;
    private String name, dateOfBirth, gender;
    private RadioGroup genderRadioGroup;
    private RadioButton radioMale, radioFemale, radioOther;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account_basic_information, container, false);
        context = container.getContext();
        init();
        return view;
    }

    private void init() {
        useSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        fragmentManager = getFragmentManager();
        accountBasicImg = view.findViewById(R.id.account_basic_img);
        edtName = view.findViewById(R.id.edt_account_basic_name);
        edtEmail = view.findViewById(R.id.edt_account_basic_email);
        edtDoB = view.findViewById(R.id.edt_account_basic_dob);
        accountDetailsBtn = view.findViewById(R.id.account_details_btn);
        accountBasicUpdateBtn = view.findViewById(R.id.account_basic_update_btn);
        accountDetailsBtn.setOnClickListener(this);
        accountBasicUpdateBtn.setOnClickListener(this);
        genderRadioGroup = view.findViewById(R.id.account_radio_gender);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioGender = (RadioButton) view.findViewById(checkedId);
                gender = radioGender.getText().toString();
            }
        });
        radioMale = view.findViewById(R.id.account_basic_gender_male);
        radioFemale = view.findViewById(R.id.account_basic_gender_female);
        radioOther = view.findViewById(R.id.account_basic_gender_other);

        setUserInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_details_btn: {
                fragmentManager.beginTransaction().replace(R.id.frameAccountInformationContainer, new AccountDetailsInformationFragment()).commit();
                break;
            }

            case R.id.account_basic_update_btn: {
                updateUserInformation();
                break;
            }
        }
    }

    private void updateUserInformation() {
        getUserInformation();
        dialog.setMessage("Đang cập nhật thông tin cá nhân");
        dialog.show();
        Retrofit retrofit = new Client().getRetrofit(getActivity().getApplicationContext());;
        UserService userService = retrofit.create(UserService.class);
        Call<ProfilePOJO> call = userService.updateUserInfor(name, dateOfBirth, gender, null, null, null, null, null);
        call.enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    User user = response.body().getUser().get(0);
                    SharedPreferences.Editor editor = useSharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("dateOfBirth", dateOfBirth);
                    editor.putString("gender", gender);
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

    private void setUserInformation() {
        Picasso.get().load(Constant.DOMAIN + useSharedPreferences.getString("avatar", "store/profiles/nouser1.png")).resize(350,350).centerCrop().into(accountBasicImg);
        edtName.setText(useSharedPreferences.getString("name", ""));
        edtEmail.setText(useSharedPreferences.getString("email", ""));
        edtDoB.setText(Helper.setBirthDay(useSharedPreferences.getString("dateOfBirth", "")));
        gender = useSharedPreferences.getString("gender", "");
        if(gender.equals("Nam")) {
            radioMale.setChecked(true);
        } else if(gender.equals("Nữ")) {
            radioFemale.setChecked(true);
        } else if(gender.equals("Khác")) {
            radioOther.setChecked(true);
        }
    }

    private void getUserInformation() {
        name = edtName.getText().toString();
        dateOfBirth = Helper.saveBirthDay(edtDoB.getText().toString());
    }
}
