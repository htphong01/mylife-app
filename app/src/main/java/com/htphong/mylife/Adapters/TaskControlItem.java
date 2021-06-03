package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.TaskControlDetailsActivity;
import com.htphong.mylife.Activities.TaskDetailsActivity;
import com.htphong.mylife.Models.Task;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.util.ArrayList;

public class TaskControlItem extends RecyclerView.Adapter<TaskControlItem.ViewHolder> {

    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private Context context;

    public TaskControlItem(ArrayList<Task> taskArrayList, Context context) {
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_control_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = (Task) taskArrayList.get(position);
        if(task.getIsCompleted() == 1) {
            holder.unCheckBtn.setVisibility(View.VISIBLE);
        } else {
            holder.checkedBtn.setVisibility(View.VISIBLE);
        }
        holder.txtTaskTitle.setText(task.getTitle());
        holder.txtTaskDeadline.setText(Helper.formatDeadlineTime(task.getDeadline()));
        holder.txtTaskReceiver.setText(task.getReceiverName());
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
            Intent intent = new Intent(context, TaskControlDetailsActivity.class);
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

        private TextView txtTaskTitle, txtTaskDeadline, txtTaskStatus, txtTaskReceiver;
        private ImageButton moreBtn, unCheckBtn, checkedBtn;
        private LinearLayout layoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTaskTitle = itemView.findViewById(R.id.task_control_item_title);
            txtTaskDeadline = itemView.findViewById(R.id.task_control_item_deadline);
            txtTaskStatus = itemView.findViewById(R.id.task_control_item_status);
            txtTaskReceiver = itemView.findViewById(R.id.task_control_item_receiver);
            moreBtn = itemView.findViewById(R.id.task_control_item_more_btn);
            unCheckBtn = itemView.findViewById(R.id.task_control_item_uncheck);
            checkedBtn = itemView.findViewById(R.id.task_control_item_checked);
            layoutItem = itemView.findViewById(R.id.layout_task_control_item);
        }
    }
}
