package com.htphong.mylife.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.htphong.mylife.Activities.AccountInformationActivity;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private ImageView accountImgCover;
    private CircleImageView accountImgAvatar;
    private TextView accountFriendCount, accountPostCount, accountName, accountCreatedAt;
    private Button btnAccountViewInfor;
    SharedPreferences userSharedPreferences;
    private LinearLayout accountPostPage, accountAlbumPage;
    private ViewPager viewPagerAccount;
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private Fragment childFragment;
    private FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account, container, false);
        context = container.getContext();
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        childFragment = new AccountPostFragment();
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frameAccountContainer, childFragment).commit();
    }

    private void init() {
        userSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        accountImgCover = view.findViewById(R.id.account_img_cover);
        accountImgAvatar = view.findViewById(R.id.account_img_avatar);
        accountFriendCount = view.findViewById(R.id.txt_account_friend_count);
        accountPostCount = view.findViewById(R.id.txt_account_post_count);
        accountName = view.findViewById(R.id.txt_account_name);
        accountCreatedAt = view.findViewById(R.id.txt_account_created_at);
        btnAccountViewInfor = view.findViewById(R.id.btn_account_view_infor);
        accountPostPage = view.findViewById(R.id.account_fragment_post);
        accountAlbumPage = view.findViewById(R.id.account_fragment_album);
        accountPostPage.setOnClickListener(this);
        accountAlbumPage.setOnClickListener(this);
        btnAccountViewInfor.setOnClickListener(this);
        setUserInformation();
    }

    private void setUserInformation() {
        Picasso.get().load(Constant.DOMAIN + userSharedPreferences.getString("cover", "store/covers/default.jpg"))
                .resize(828,465)
                .centerCrop()
                .into(accountImgCover);
        Picasso.get().load(Constant.DOMAIN + userSharedPreferences.getString("avatar", "store/profiles/nouser1.png"))
                .resize(350,350)
                .centerCrop()
                .into(accountImgAvatar);
        accountName.setText(userSharedPreferences.getString("name", "Tên người dùng"));
        String created_at = userSharedPreferences.getString("created_at", "2021-05-14T21:17:18.000");
        accountCreatedAt.setText("Đã tham gia vào " + convertTime(created_at));
        accountFriendCount.setText(userSharedPreferences.getString("friendCount", ""));
        accountPostCount.setText(userSharedPreferences.getString("postCount", ""));

    }

    private String convertTime(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account_view_infor: {
                startActivity(new Intent(getContext(), AccountInformationActivity.class));
                break;
            }
            case R.id.account_fragment_post: {
                childFragment = new AccountPostFragment();
                transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameAccountContainer, childFragment).commit();
                break;
            }

            case R.id.account_fragment_album: {
                childFragment = new AccountAlbumFragment();
                transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameAccountContainer, childFragment).commit();
                break;
            }
        }
    }


}
