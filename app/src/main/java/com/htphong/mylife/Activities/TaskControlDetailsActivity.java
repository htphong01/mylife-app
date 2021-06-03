package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.TaskService;
import com.htphong.mylife.Models.Task;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.POJO.TaskPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskControlDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imgTaskAvatar;
    private TextView txtTaskTitle, txtTaskDesc, txtTaskDeadline, txtTaskReceiverName, edtTaskNote;
    private ImageView imgTaskFile;
    private ImageButton backBtn;
    private Button submitBtn;
    private static String task_id;
    private static Task task;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_control_details);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(TaskControlDetailsActivity.this);
        dialog.setCancelable(false);

        imgTaskAvatar = findViewById(R.id.task_control_details_avatar);
        txtTaskTitle = findViewById(R.id.task_control_details_title);
        txtTaskDesc = findViewById(R.id.task_control_details_desc);
        txtTaskDeadline = findViewById(R.id.task_control_details_deadline);
        txtTaskReceiverName = findViewById(R.id.task_control_details_receiver_name);
        edtTaskNote = findViewById(R.id.task_control_details_note);
        imgTaskFile = findViewById(R.id.task_control_details_file);
        backBtn = findViewById(R.id.task_control_details_back_btn);
        submitBtn = findViewById(R.id.task_control_details_submit_btn);

        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        getTask();
    }

    private void getTask() {
        Retrofit retrofit = new Client().getRetrofit(TaskControlDetailsActivity.this);
        retrofit.create(TaskService.class)
                .getTask(String.valueOf(getIntent().getExtras().getInt("task_id")))
                .enqueue(new Callback<TaskPOJO>() {
                    @Override
                    public void onResponse(Call<TaskPOJO> call, Response<TaskPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            task = response.body().getTasks().get(0);
                            Picasso.get().load(Constant.DOMAIN + task.getReceiverAvatar()).resize(350,350).centerCrop().into(imgTaskAvatar);
                            txtTaskReceiverName.setText("Bạn đã giao cho " + task.getReceiverName());
                            txtTaskTitle.setText(task.getTitle());
                            txtTaskDesc.setText(task.getContent());
                            txtTaskDeadline.setText(Helper.formatDeadlineTime(task.getDeadline()));
                            edtTaskNote.setText(task.getNote());
                            Picasso.get().load(Constant.DOMAIN + task.getFile()).into(imgTaskFile);
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskPOJO> call, Throwable t) {

                    }
                });
    }

    private void completeTask() {
        dialog.setMessage("Đang thực hiện");
        dialog.show();
        Retrofit retrofit = new Client().getRetrofit(TaskControlDetailsActivity.this);
        retrofit.create(TaskService.class).completeTask(String.valueOf(task.getId()))
                .enqueue(new Callback<StatusPOJO>() {
                    @Override
                    public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                        Log.d("TASK_COMPLETE_RES: ", response.message());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<StatusPOJO> call, Throwable t) {
                        Log.d("TASK_COMPLETE_ERR: ", t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_control_details_back_btn: {
                super.onBackPressed();
                break;
            }

            case R.id.task_control_details_submit_btn: {
                completeTask();
                break;
            }
        }
    }
}