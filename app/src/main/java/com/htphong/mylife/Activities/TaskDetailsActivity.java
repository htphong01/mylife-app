package com.htphong.mylife.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imgTaskPhoto;
    private TextView txtTaskCreater, txtTaskTitle, txtTaskDesc, txtTaskDeadline;
    private ImageButton backBtn, uploadFileBtn, uploadImageBtn;
    private EditText edtNote;
    private ImageView imgFile;
    private Button submitBtn;
    private ProgressDialog dialog;
    private Bitmap bitmap = null;
    private static final int PHOTO_UPLOAD_TASK = 6;
    private static String task_id = "11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(TaskDetailsActivity.this);
        dialog.setCancelable(false);

        imgTaskPhoto = findViewById(R.id.task_details_avatar);
        txtTaskCreater = findViewById(R.id.task_details_creater_name);
        txtTaskTitle = findViewById(R.id.task_details_title);
        txtTaskDesc = findViewById(R.id.task_details_desc);
        txtTaskDeadline = findViewById(R.id.task_details_deadline);
        edtNote = findViewById(R.id.task_details_note);
        imgFile = findViewById(R.id.task_details_file);
        uploadFileBtn = findViewById(R.id.task_details_upload_file_btn);
        uploadImageBtn = findViewById(R.id.task_details_upload_image_btn);
        backBtn = findViewById(R.id.task_details_back_btn);
        submitBtn = findViewById(R.id.task_details_submit_btn);

        backBtn.setOnClickListener(this);
        uploadImageBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        getTask();
    }

    private void getTask() {
        Retrofit retrofit = new Client().getRetrofit(TaskDetailsActivity.this);
        retrofit.create(TaskService.class)
                .getTask(String.valueOf(getIntent().getExtras().getInt("task_id")))
                .enqueue(new Callback<TaskPOJO>() {
                    @Override
                    public void onResponse(Call<TaskPOJO> call, Response<TaskPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            Task task = response.body().getTasks().get(0);
                            Picasso.get().load(Constant.DOMAIN + task.getCreaterAvatar()).resize(350,350).centerCrop().into(imgTaskPhoto);
                            txtTaskCreater.setText(task.getCreaterName() + " đã giao cho bạn");
                            txtTaskTitle.setText(task.getTitle());
                            txtTaskDesc.setText(task.getContent());
                            txtTaskDeadline.setText(formatDeadlineTime(task.getDeadline()));
                            edtNote.setText(task.getNote());
                            Picasso.get().load(Constant.DOMAIN + task.getFile()).into(imgFile);
                            task_id = String.valueOf(task.getId());
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskPOJO> call, Throwable t) {

                    }
                });
    }

    public static String formatDeadlineTime(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_details_back_btn: {
                super.onBackPressed();
                break;
            }

            case R.id.task_details_upload_image_btn: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_UPLOAD_TASK);
                break;
            }

            case R.id.task_details_submit_btn: {
                if(!(edtNote.getText().toString().isEmpty() && Helper.bitmapToString(bitmap).isEmpty())) {
                    submitTask(edtNote.getText().toString(), Helper.bitmapToString(bitmap));
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PHOTO_UPLOAD_TASK) && (resultCode == RESULT_OK)){
            Uri imgUri = data.getData();
            imgFile.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitTask(String note, String bitmap) {
        dialog.setMessage("Đang thực hiện");
        dialog.show();
        Retrofit retrofit = new Client().getRetrofit(TaskDetailsActivity.this);
        retrofit.create(TaskService.class)
                .submitTask(task_id, note, bitmap)
                .enqueue(new Callback<StatusPOJO>() {
                    @Override
                    public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                        Log.d("TASK_SUBMIT_RES: ", response.message());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<StatusPOJO> call, Throwable t) {
                        Log.d("TASK_SUBMIT_ERR: ", t.getMessage());
                        dialog.dismiss();
                    }
                });
    }
}