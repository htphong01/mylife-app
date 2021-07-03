package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.PostActivity;
import com.htphong.mylife.Activities.TaskDetailsActivity;
import com.htphong.mylife.Models.Task;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {

    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private Context context;

    public TaskItemAdapter(ArrayList<Task> taskArrayList, Context context) {
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_management_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences useSharedPreferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        Task task = (Task) taskArrayList.get(position);
        if(task.getIsCompleted() == 1) {
            holder.checkedBtn.setVisibility(View.GONE);
            holder.unCheckBtn.setVisibility(View.VISIBLE);
        } else {
            holder.unCheckBtn.setVisibility(View.GONE);
            holder.checkedBtn.setVisibility(View.VISIBLE);
        }
        holder.txtTaskTitle.setText(task.getTitle());
        holder.txtTaskDeadline.setText(Helper.formatDeadlineTime(task.getDeadline()));

        if(task.getIsSubmitted() == 1) {
            holder.txtTaskStatus.setText("Chưa hoàn thành");
        } else {
            if(task.getIsCompleted() == 1) {
                holder.txtTaskStatus.setText("Đã nộp");
            } else {
                holder.txtTaskStatus.setText("Đã hoàn thành");
            }
        }
        holder.layoutItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTaskTitle, txtTaskDeadline, txtTaskStatus;
        private ImageButton moreBtn, unCheckBtn, checkedBtn;
        private LinearLayout layoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTaskTitle = itemView.findViewById(R.id.task_management_item_title);
            txtTaskDeadline = itemView.findViewById(R.id.task_management_item_deadline);
            txtTaskStatus = itemView.findViewById(R.id.task_management_item_status);
            moreBtn = itemView.findViewById(R.id.task_management_item_more_btn);
            unCheckBtn = itemView.findViewById(R.id.task_management_item_uncheck);
            checkedBtn = itemView.findViewById(R.id.task_management_item_checked);
            layoutItem = itemView.findViewById(R.id.layout_task_management_item);
        }
    }
}
