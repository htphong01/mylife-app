package com.htphong.mylife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.htphong.mylife.R;

public class AddFriendInvitationFragment extends Fragment {

    private View view;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_add_friend_invitation, container, false);
        mContext = container.getContext();
        init();
        return view;
    }

    private void init() {
        
    }
}
