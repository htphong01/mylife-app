package com.htphong.mylife.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.AuthActivity;
import com.htphong.mylife.Constant;
import com.htphong.mylife.HomeActivity;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context mContext;
    private CircleImageView menuAccountAvatar;
    private LinearLayout menuFriend, menuEvent, menuAlbum, menuNotification, menuSetting, menuHelp, menuLogout, menuAccount;
    private FragmentManager fragmentManager;
    private TextView menuAccountName;
    private SharedPreferences userPref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu, container, false);
        mContext = container.getContext();
        init();
        return view;
    }

    private void init() {
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        fragmentManager = getFragmentManager();
        String avatar = userPref.getString("avatar", "store/profiles/nouser1.png");
        menuAccountAvatar = view.findViewById(R.id.menu_account_img_avatar);
        Picasso.get().load( Constant.DOMAIN + avatar).resize(350,350).centerCrop().into(menuAccountAvatar);
        menuAccountName = view.findViewById(R.id.menu_account_name);
        menuAccountName.setText(userPref.getString("name", "Tên người dùng"));
        menuFriend = view.findViewById(R.id.menu_friend);
        menuEvent = view.findViewById(R.id.menu_event);
        menuAlbum = view.findViewById(R.id.menu_album);
        menuNotification = view.findViewById(R.id.menu_notification);
        menuSetting = view.findViewById(R.id.menu_setting);
        menuHelp = view.findViewById(R.id.menu_help);
        menuLogout = view.findViewById(R.id.menu_logout);
        menuAccount = view.findViewById(R.id.menu_account);
        menuFriend.setOnClickListener(this);
        menuEvent.setOnClickListener(this);
        menuAlbum.setOnClickListener(this);
        menuNotification.setOnClickListener(this);
        menuSetting.setOnClickListener(this);
        menuHelp.setOnClickListener(this);
        menuLogout.setOnClickListener(this);
        menuAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_account: {
                fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new AccountFragment()).commit();
                break;
            }

            case R.id.menu_logout: {
                logout();
                break;
            }
        }
    }

    private void logout() {
        Retrofit retrofit = new Client().getRetrofit(getContext());
        UserService userService = retrofit.create(UserService.class);
        Call<StatusPOJO> call = userService.logout();
        call.enqueue(new Callback<StatusPOJO>() {
            @Override
            public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                Log.d("Logout Res: ", response.body().toString());
                if(response.isSuccessful() && response.body().getSuccess()) {
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.clear();
                    editor.apply();
                    SharedPreferences userTarget = getActivity().getApplicationContext().getSharedPreferences("user_target", getContext().MODE_PRIVATE);
                    editor = userTarget.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(((HomeActivity)getContext()), AuthActivity.class));
                    ((HomeActivity)getContext()).finish();
                }
            }

            @Override
            public void onFailure(Call<StatusPOJO> call, Throwable t) {
                Log.d("Logout Error: ", t.getMessage());
            }
        });

    }
}
