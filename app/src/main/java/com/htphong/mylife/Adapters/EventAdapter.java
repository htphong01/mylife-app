package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.ChattingActivity;
import com.htphong.mylife.Activities.EventInfoActivity;
import com.htphong.mylife.Models.Event;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> list = new ArrayList<>();

    public EventAdapter(Context context, ArrayList<Event> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = (Event) list.get(position);
        Picasso.get().load(Constant.DOMAIN + event.getImage()).resize(400, 400).centerCrop().into(holder.imgAvatar);
        holder.name.setText(event.getCreaterName());
        holder.title.setText(event.getTitle());
        holder.place.setText(event.getAddress());
        holder.time.setText(Helper.formatDeadlineTime(event.getDateStart()));
        holder.status.setText(event.getStatus());
        holder.eventItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventInfoActivity.class);
            intent.putExtra("event_id", event.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;
        private TextView name, title, place, time, status;
        private LinearLayout eventItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.avatar_user);
            name = itemView.findViewById(R.id.username);
            title = itemView.findViewById(R.id.name_event);
            place = itemView.findViewById(R.id.place_event);
            time = itemView.findViewById(R.id.time_event);
            status = itemView.findViewById(R.id.status_event);
            eventItem = itemView.findViewById(R.id.event_item);

        }
    }
}
