package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.ImageActivity;
import com.htphong.mylife.Activities.TaskDetailsActivity;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Message;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private Context context;

    public MessageAdapter(ArrayList<Message> messageArrayList, Context context) {
        this.messageArrayList = messageArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("id", "1");
        Message message = (Message)messageArrayList.get(position);
        holder.txtMessageTime.setText(Helper.timeDifferent(message.getCreatedAt()));

        // Auth message
        if(message.getUserId().toString().equals(authId)) {
            holder.layoutOtherMessage.setVisibility(View.GONE);
            if(message.getType().equals("image")) {
                holder.txtMyMessage.setVisibility(View.GONE);
                Picasso.get().load(Constant.DOMAIN + message.getMessage()).into(holder.imgMyPhoto);
            } else if(message.getType().equals("text")) {
                holder.imgMyPhoto.setVisibility(View.GONE);
                holder.txtMyMessage.setText(message.getMessage());
            } else if(message.getType().startsWith("task")) {
                holder.imgMyPhoto.setVisibility(View.GONE);
                holder.txtMyMessage.setText(message.getNickname() + " " + message.getMessage() + "\nNhấp để xem chi tiết");
            }

            if(position < messageArrayList.size() - 1 ) {
                Message nextMessage = (Message)messageArrayList.get(position + 1);
                if(nextMessage.getUserId() != message.getUserId()) {
                    holder.layoutMyMessage.setPadding(0,0,0,50);
                }
            }
        } else {
            holder.layoutMyMessage.setVisibility(View.GONE);
            if(message.getType().equals("image")) {
                holder.txtOtherMessage.setVisibility(View.GONE);
                Picasso.get().load(Constant.DOMAIN + message.getMessage()).into(holder.imgOtherPhoto);
            } else if(message.getType().equals("text")) {
                holder.imgOtherPhoto.setVisibility(View.GONE);
                holder.txtOtherMessage.setText(message.getMessage());
            } else if(message.getType().startsWith("task")) {
                holder.imgOtherPhoto.setVisibility(View.GONE);
                holder.txtOtherMessage.setText(message.getNickname() + " " + message.getMessage() + "\nNhấp để xem chi tiết");
            }

            if(position == messageArrayList.size() - 1) {
                Picasso.get().load(Constant.DOMAIN + message.getAvatar()).resize(350, 350).centerCrop().into(holder.imgMessageAvatar);
            }

            if(position < messageArrayList.size() - 1 ) {
                Message nextMessage = (Message)messageArrayList.get(position + 1);
                if(nextMessage.getUserId() != message.getUserId()) {
                    Picasso.get().load(Constant.DOMAIN + message.getAvatar()).resize(350, 350).centerCrop().into(holder.imgMessageAvatar);
                    holder.layoutOtherMessage.setPadding(0,0,0,50);
                }
            }
        }

        holder.txtOtherMessage.setOnClickListener(v -> {
            if(message.getType().startsWith("task")) {
                String task_id = message.getType().substring(5);
                gotoTask(Integer.parseInt(task_id));
            } else {
                if(holder.txtMessageTime.getVisibility() == View.VISIBLE) {
                    holder.txtMessageTime.setVisibility(View.GONE);
                } else {
                    holder.txtMessageTime.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.txtMyMessage.setOnClickListener(v -> {
            if(message.getType().startsWith("task")) {
                String task_id = message.getType().substring(5);
                gotoTask(Integer.parseInt(task_id));
            } else {
                if(holder.txtMessageTime.getVisibility() == View.VISIBLE) {
                    holder.txtMessageTime.setVisibility(View.GONE);
                } else {
                    holder.txtMessageTime.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.imgMyPhoto.setOnClickListener(v -> openImage(message.getMessage()));
        holder.imgOtherPhoto.setOnClickListener(v -> openImage(message.getMessage()));
    }

    private void openImage(String url) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private void gotoTask(int task_id) {
        Intent intent = new Intent(context, TaskDetailsActivity.class);
        intent.putExtra("task_id", task_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public void addMessage(Message message) {
        messageArrayList.add(message);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtMessageTime, txtOtherMessage, txtMyMessage;
        private CircleImageView imgMessageAvatar;
        private LinearLayout layoutOtherMessage, layoutMyMessage ;
        private ImageView imgMyPhoto, imgOtherPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessageTime= itemView.findViewById(R.id.txt_message_time);
            txtOtherMessage= itemView.findViewById(R.id.txt_other_message);
            txtMyMessage= itemView.findViewById(R.id.txt_my_message);
            imgMessageAvatar = itemView.findViewById(R.id.img_message_avatar);
            layoutOtherMessage = itemView.findViewById(R.id.layout_other_message_item);
            layoutMyMessage = itemView.findViewById(R.id.layout_my_message_item);
            imgMyPhoto = itemView.findViewById(R.id.img_my_message_photo);
            imgOtherPhoto = itemView.findViewById(R.id.img_other_message_photo);

        }
    }
}
