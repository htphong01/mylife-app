package com.htphong.mylife.Utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.htphong.mylife.Models.Notifications;

import java.util.ArrayList;

public class NotificationDiffUtilCallBacks extends DiffUtil.Callback {

    ArrayList<Notifications> oldNotifications = new ArrayList<>();
    ArrayList<Notifications> newNotifications = new ArrayList<>();

    public NotificationDiffUtilCallBacks(ArrayList<Notifications> oldNotifications, ArrayList<Notifications> newNotifications) {
        this.oldNotifications = oldNotifications;
        this.newNotifications = newNotifications;
    }

    @Override
    public int getOldListSize() {
        return oldNotifications != null ? oldNotifications.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newNotifications != null ? newNotifications.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newNotifications.get(newItemPosition).compareTo(oldNotifications.get(oldItemPosition));
        if(result == 0) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Notifications newNotification = newNotifications.get(newItemPosition);
        Notifications oldNotification = newNotifications.get(oldItemPosition);

        Bundle bundle = new Bundle();

        if(!(newNotification.getId() == oldNotification.getId())) {
            bundle.putString("id", String.valueOf(newNotification.getId()));
            bundle.putString("sender_id", String.valueOf(newNotification.getSenderId()));
            bundle.putString("content", newNotification.getContent());
            bundle.putString("receiver_id", String.valueOf(newNotification.getReceiverId()));
            bundle.putString("link", newNotification.getLink());
            bundle.putString("image", newNotification.getImage());
            bundle.putString("type", String.valueOf(newNotification.getType()));
            bundle.putString("created_at", newNotification.getCreatedAt());
            bundle.putString("updated_at", newNotification.getUpdatedAt());
        }

        if(!(bundle.size() == 0)) {
            return null;
        }

        return bundle;
    }
}
