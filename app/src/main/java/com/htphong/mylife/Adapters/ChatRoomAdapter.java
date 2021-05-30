package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.ChattingActivity;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Room;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private Context context;

    public ChatRoomAdapter(ArrayList<Room> roomArrayList, Context context) {
        this.roomArrayList = roomArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = (Room)roomArrayList.get(position);
        Picasso.get().load(Constant.DOMAIN + room.getPhoto()).resize(350, 350).centerCrop().into(holder.imgChatRoom);
        holder.txtChatRoomName.setText(room.getName());
        holder.txtChatRoomMessage.setText(room.getMessage());
        holder.txtChatRoomTime.setText(Helper.timeDifferent(room.getMessageTime()));
        holder.layoutChatRoomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("room_id", room.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences chatRoom = context.getApplicationContext().getSharedPreferences("chat_room", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = chatRoom.edit();
                editor.putString("room_id", String.valueOf(room.getId()));
                editor.apply();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgChatRoom;
        private TextView txtChatRoomName, txtChatRoomMessage, txtChatRoomTime;
        private LinearLayout layoutChatRoomItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChatRoom = itemView.findViewById(R.id.img_chat_room_photo);
            txtChatRoomName = itemView.findViewById(R.id.txt_chat_room_name);
            txtChatRoomMessage = itemView.findViewById(R.id.txt_chat_room_message);
            txtChatRoomTime = itemView.findViewById(R.id.txt_chat_room_time);
            layoutChatRoomItem = itemView.findViewById(R.id.layout_chatroom_item);
        }
    }
}
