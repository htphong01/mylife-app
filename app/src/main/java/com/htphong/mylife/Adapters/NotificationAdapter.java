package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.PostActivity;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Notifications;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.htphong.mylife.Utils.NotificationDiffUtilCallBacks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    public ArrayList<Notifications> list = new ArrayList<Notifications>();
    Context context;

    public NotificationAdapter(Context context, ArrayList<Notifications> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notifi_item, parent, false);
        return new ViewHolder(view);
    }

    public void updateNotification(ArrayList<Notifications> newNotifications) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotificationDiffUtilCallBacks(this.list, newNotifications));
        diffResult.dispatchUpdatesTo(this);
        this.list.clear();
        this.list.addAll(newNotifications);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications notification = list.get(position);
        Picasso.get().load(Constant.DOMAIN + notification.getImage()).resize(350,350).centerCrop().into(holder.userImage);
        holder.txtContent.setText(Html.fromHtml(notification.getContent()));
        holder.txtTime.setText(Helper.timeDifferent(notification.getCreatedAt()));
        holder.layoutNotification.setOnClickListener(v -> {
            notificationClicked(notification);
        });
    }

    private void notificationClicked(Notifications notification) {
        switch (notification.getType()) {
            case 1:
            case 4: {
                gotoProfile(String.valueOf(notification.getSenderId()));
                break;
            }
            case 2:
            case 3: {
                goToPost(notification.getLink());
                break;
            }

        }
    }

    private void goToPost(String post_id) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("post_id", Integer.parseInt(post_id));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void gotoProfile(String user_id) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user_id", user_id);
        SharedPreferences userPref = context.getApplicationContext().getSharedPreferences("user_target", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("id", user_id);
        editor.apply();
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle bundle = (Bundle)payloads.get(0);
            for(String key : bundle.keySet()) {
                Picasso.get().load(Constant.DOMAIN + bundle.getString("image")).resize(3350,350).centerCrop().into(holder.userImage);
                holder.txtContent.setText(Html.fromHtml(bundle.getString("content")));
                holder.txtTime.setText(Helper.timeDifferent(bundle.getString("created_at")));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userImage;
        private TextView txtContent, txtTime;
        private LinearLayout layoutNotification;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.notification_user_image);
            txtContent = itemView.findViewById(R.id.txt_notification_content);
            txtTime = itemView.findViewById(R.id.txt_notification_time);
            layoutNotification = itemView.findViewById(R.id.layout_notification_item);
        }
    }
}
