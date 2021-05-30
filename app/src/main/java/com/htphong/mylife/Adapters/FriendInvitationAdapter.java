package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.FriendService;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Friend;
import com.htphong.mylife.POJO.FriendPOJO;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FriendInvitationAdapter extends RecyclerView.Adapter<FriendInvitationAdapter.ViewHolder> {

    private ArrayList<Friend> friendList = new ArrayList<>();
    private Context context;

    public FriendInvitationAdapter(ArrayList<Friend> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_friend_invitation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = (Friend)friendList.get(position);
        Picasso.get().load(Constant.DOMAIN + friend.getUser1().getPhoto()).resize(350, 350).centerCrop().into(holder.friendAvatar);
        holder.friendName.setText(friend.getUser1().getUserName());
        holder.friendTime.setText(Helper.timeDifferent(friend.getCreatedAt()));
        holder.friendAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile(String.valueOf(friend.getUserId1()));
            }
        });
        holder.friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile(String.valueOf(friend.getUserId1()));
            }
        });
        holder.acceptFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.acceptFriendBtn.setVisibility(View.GONE);
                holder.removeFriendBtn.setVisibility(View.GONE);
                holder.friendShowBtn.setVisibility(View.VISIBLE);
                holder.friendShowBtn.setText("Bạn bè");
                acceptFriendRequest(String.valueOf(friend.getId()));
            }
        });

        holder.removeFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.acceptFriendBtn.setVisibility(View.GONE);
                holder.removeFriendBtn.setVisibility(View.GONE);
                holder.friendShowBtn.setVisibility(View.VISIBLE);
                holder.friendShowBtn.setText("Đã xóa");
                removeFriendRequest(String.valueOf(friend.getId()));
            }
        });
    }

    private void gotoProfile(String user_id) {
        Intent intent;
        SharedPreferences userTargetPref = context.getApplicationContext().getSharedPreferences("user_target", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userTargetPref.edit();
        editor.putString("id", user_id);
        editor.apply();
        intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void acceptFriendRequest(String id) {
        Retrofit retrofit = new Client().getRetrofit(context);
        FriendService friendService = retrofit.create(FriendService.class);
        Call<FriendPOJO> call = friendService.acceptFriend(id);
        call.enqueue(new Callback<FriendPOJO>() {
            @Override
            public void onResponse(Call<FriendPOJO> call, Response<FriendPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Log.d("AcceptFriend: ", "success");
                }
            }

            @Override
            public void onFailure(Call<FriendPOJO> call, Throwable t) {
                Log.d("AcceptFriend: ", t.getMessage());
            }
        });
    }

    private void removeFriendRequest(String id) {
        Retrofit retrofit = new Client().getRetrofit(context);
        FriendService friendService = retrofit.create(FriendService.class);
        Call<StatusPOJO> call = friendService.removeFriendRequest(id);
        call.enqueue(new Callback<StatusPOJO>() {
            @Override
            public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    Log.d("RemoveFriendRequest: ", "success");
                }
            }

            @Override
            public void onFailure(Call<StatusPOJO> call, Throwable t) {
                Log.d("RemoveFriendRequest: ", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView friendAvatar;
        private TextView friendName, friendTime;
        private Button acceptFriendBtn, friendShowBtn;
        private ImageButton removeFriendBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendAvatar = itemView.findViewById(R.id.friend_invitation_avartar);
            friendName = itemView.findViewById(R.id.txt_friend_invitation_name);
            friendTime = itemView.findViewById(R.id.txt_friend_invitation_time);
            acceptFriendBtn = itemView.findViewById(R.id.friend_invitation_accept_btn);
            removeFriendBtn = itemView.findViewById(R.id.friend_invitation_remove_btn);
            friendShowBtn = itemView.findViewById(R.id.friend_invitation_show);
        }
    }
}
