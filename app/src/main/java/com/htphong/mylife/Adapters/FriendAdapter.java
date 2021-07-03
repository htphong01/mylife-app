package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.ChattingService;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.Activities.ChattingActivity;
import com.htphong.mylife.Activities.PostActivity;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.Models.Friend;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.MessagePOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private ArrayList<Friend> friendList = new ArrayList<>();
    private Context context;

    public FriendAdapter(ArrayList<Friend> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = (Friend) friendList.get(position);
        Picasso.get().load(Constant.DOMAIN + friend.getFriendAvatar()).resize(350,350).centerCrop().into(holder.imgFriendAvatar);
        holder.txtFriendName.setText(friend.getFriendName());
        holder.txtFriendDate.setText("Bạn bè từ " + Helper.convertDateTime(friend.getCreatedAt()));
        holder.friendItem.setOnClickListener(v -> {
            gotoProfile(friend);
        });

        holder.btnMessage.setOnClickListener(v -> {
            gotoChatting(friend);
        });
    }

    private void gotoChatting(Friend friend) {
        Retrofit retrofit = new Client().getRetrofit(context);
        ChattingService chattingService = retrofit.create(ChattingService.class);
        Call<MessagePOJO> call = chattingService.getListMessage("0", String.valueOf(friend.getFriendId()));
        call.enqueue(new Callback<MessagePOJO>() {
            @Override
            public void onResponse(Call<MessagePOJO> call, Response<MessagePOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    SharedPreferences chatRoom = context.getSharedPreferences("chat_room", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = chatRoom.edit();
                    editor.putString("room_id", String.valueOf(response.body().getRoom_id()));
                    editor.apply();
                    context.startActivity(new Intent(context, ChattingActivity.class));
                }
            }

            @Override
            public void onFailure(Call<MessagePOJO> call, Throwable t) {

            }
        });
    }


    private void gotoProfile(Friend friend) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user_id", String.valueOf(friend.getFriendId()));
        SharedPreferences userPref = context.getApplicationContext().getSharedPreferences("user_target", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("id", String.valueOf(friend.getFriendId()));
        editor.apply();
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView friendItem;
        private CircleImageView imgFriendAvatar;
        private TextView txtFriendName, txtFriendDate;
        private ImageButton btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendItem = itemView.findViewById(R.id.friend_item);
            imgFriendAvatar = itemView.findViewById(R.id.img_friend_avatar);
            txtFriendName = itemView.findViewById(R.id.txt_friend_name);
            txtFriendDate = itemView.findViewById(R.id.txt_friend_date);
            btnMessage = itemView.findViewById(R.id.btn_message);
        }
    }
}
