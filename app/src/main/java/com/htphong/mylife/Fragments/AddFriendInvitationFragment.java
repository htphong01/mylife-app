package com.htphong.mylife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.FriendService;
import com.htphong.mylife.Adapters.FriendInvitationAdapter;
import com.htphong.mylife.Models.Friend;
import com.htphong.mylife.POJO.FriendPOJO;
import com.htphong.mylife.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddFriendInvitationFragment extends Fragment {

    private View view;
    private Context mContext;
    private RecyclerView friendRequestRecyclerView;
    private RecyclerView.Adapter friendRequestAdapter;
    private ArrayList<Friend> friendArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_add_friend_invitation, container, false);
        mContext = container.getContext();
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getFriendRequest();
    }

    private void init() {
        friendRequestRecyclerView = view.findViewById(R.id.recycler_add_friend);
        friendRequestRecyclerView.setHasFixedSize(true);
        friendRequestRecyclerView.setNestedScrollingEnabled(true);
        friendRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        friendRequestRecyclerView.setItemViewCacheSize(10);
        friendRequestRecyclerView.setDrawingCacheEnabled(true);
        friendRequestAdapter = new FriendInvitationAdapter(friendArrayList, getContext());
        friendRequestRecyclerView.setAdapter(friendRequestAdapter);
    }

    private void getFriendRequest() {
        Retrofit retrofit = new Client().getRetrofit(getContext());
        FriendService friendService = retrofit.create(FriendService.class);
        Call<FriendPOJO> call = friendService.getFriendRequests();
        call.enqueue(new Callback<FriendPOJO>() {
            @Override
            public void onResponse(Call<FriendPOJO> call, Response<FriendPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    friendArrayList.addAll(response.body().getFriends());
                    friendRequestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FriendPOJO> call, Throwable t) {
                Log.d("FriendRequestErr: ", t.getMessage());
            }
        });
    }
}
