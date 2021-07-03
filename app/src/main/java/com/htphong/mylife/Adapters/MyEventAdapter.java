package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.EventInfoActivity;
import com.htphong.mylife.Models.Event;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.util.ArrayList;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> eventLists = new ArrayList<>();

    public MyEventAdapter(Context context, ArrayList<Event> eventLists) {
        this.context = context;
        this.eventLists = eventLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = (Event) eventLists.get(position);
        holder.txtOrder.setText(String.valueOf(position + 1));
        holder.txtTitle.setText(event.getTitle());
        holder.txtTime.setText(Helper.formatDeadlineTime(event.getDateStart()));
        holder.txtPlace.setText(event.getAddress());
        holder.txtStatus.setText(event.getStatus());
        holder.eventDetailsBtn.setOnClickListener(v -> gotoEvent(event.getId()));
        holder.eventItem.setOnClickListener(v -> gotoEvent(event.getId()));
    }

    private void gotoEvent(int event_id) {
        Intent intent = new Intent(context, EventInfoActivity.class);
        intent.putExtra("event_id", event_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return eventLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtOrder, txtTitle, txtTime, txtPlace, txtStatus;
        private ImageButton eventDetailsBtn;
        private LinearLayout eventItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrder = itemView.findViewById(R.id.txt_order);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtPlace = itemView.findViewById(R.id.txt_place);
            txtStatus = itemView.findViewById(R.id.txt_status);
            eventDetailsBtn = itemView.findViewById(R.id.event_details_btn);
            eventItem = itemView.findViewById(R.id.event_my_item);
        }
    }
}
