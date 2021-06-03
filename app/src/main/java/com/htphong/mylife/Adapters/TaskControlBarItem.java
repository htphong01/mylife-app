package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.TaskControlDetailsActivity;
import com.htphong.mylife.Activities.TaskDetailsActivity;
import com.htphong.mylife.Models.Task;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TaskControlBarItem extends RecyclerView.Adapter<TaskControlBarItem.ViewHolder> {

    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private Context context;

    public TaskControlBarItem(ArrayList<Task> taskArrayList, Context context) {
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_bar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = (Task) taskArrayList.get(position);
        if(task.getIsSubmitted() == 1) {
            holder.progressBar.setProgress(0);
            holder.txtPercent.setText("0%");
        } else {
            if(task.getIsCompleted() == 1) {
                holder.progressBar.setProgress(50);
                holder.txtPercent.setText("50%");
            } else {
                holder.progressBar.setProgress(100);
                holder.txtPercent.setText("100%");
            }
        }

        holder.txtTitle.setText(task.getTitle());
        holder.txtReceiver.setText(task.getReceiverName());
        if(task.getIsCompleted() == 2 ) {
            holder.txtDeadline.setText("Đã hoàn thành");
        } else {
            holder.txtDeadline.setText(timeDifferent(task.getDeadline()));
        }
        holder.layoutTaskControlBar.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskControlDetailsActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    public static String timeDifferent(String dataDate) {

        String convTime = "";

        String prefix = "Còn ";

        try {
            dataDate = Helper.changeTimeZone(dataDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();
            long dateDiff = pasTime.getTime() - nowTime.getTime();
            if(dateDiff < 0) {
                dateDiff = -dateDiff;
                prefix = "Trễ ";
            }

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = prefix + second + " giây";
            } else if (minute < 60) {
                convTime = prefix + minute + " phút";
            } else if (hour < 24) {
                convTime = prefix + hour + " giờ";
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = prefix + (day / 360) + " năm";
                } else if (day > 30) {
                    convTime = prefix + (day / 30) + " tháng";
                } else {
                    convTime = prefix + (day / 7) + " tuần";
                }
            } else if (day < 7) {
                convTime = prefix + day+" ngày";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        private TextView txtPercent, txtTitle, txtReceiver, txtDeadline;
        private LinearLayout layoutTaskControlBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.task_control_bar_progress);
            txtPercent = itemView.findViewById(R.id.task_control_bar_percent);
            txtTitle = itemView.findViewById(R.id.task_control_bar_title);
            txtReceiver = itemView.findViewById(R.id.task_control_bar_receiver);
            txtDeadline = itemView.findViewById(R.id.task_control_bar_deadline);
            layoutTaskControlBar = itemView.findViewById(R.id.layout_task_control_bar);
        }
    }
}
