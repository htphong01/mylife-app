package com.htphong.mylife.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.htphong.mylife.API.ChattingService;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.TaskService;
import com.htphong.mylife.Adapters.TaskReceiverAdapter;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.POJO.UserPOJO;
import com.htphong.mylife.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GivingTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtTaskDeadline;
    private ImageButton backBtn;
    private Button submitBtn;
    private EditText edtName, edtDesc;
    public RecyclerView recyclerReceiver;
    private RecyclerView.Adapter receiverAdapter;
    private ArrayList<User> receiverList = new ArrayList<>();
    private ArrayList<String> receivers = new ArrayList<>();
    private Retrofit retrofit;
    private SharedPreferences roomSharedPreferences;
    private String dateString;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giving_task);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(GivingTaskActivity.this);
        dialog.setCancelable(false);

        roomSharedPreferences = getApplicationContext().getSharedPreferences("chat_room", MODE_PRIVATE);
        retrofit = new Client().getRetrofit(getApplicationContext());
        txtTaskDeadline = findViewById(R.id.txt_giving_task_deadline);
        backBtn = findViewById(R.id.giving_task_back_btn);
        submitBtn = findViewById(R.id.giving_task_submit_btn);
        backBtn = findViewById(R.id.giving_task_back_btn);
        edtName = findViewById(R.id.edt_giving_task_name);
        edtDesc = findViewById(R.id.edt_giving_task_desc);

        recyclerReceiver = findViewById(R.id.giving_task_receiver_recycler);
        recyclerReceiver.setHasFixedSize(true);
        recyclerReceiver.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerReceiver.setItemViewCacheSize(10);
        recyclerReceiver.setDrawingCacheEnabled(true);
        receiverAdapter = new TaskReceiverAdapter(getApplicationContext(), receiverList);
        recyclerReceiver.setAdapter(receiverAdapter);
        getReceivers();

        txtTaskDeadline.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

    }

    private void getReceivers() {
        ChattingService chattingService = retrofit.create(ChattingService.class);
        Call<UserPOJO> call = chattingService.getRoomUsers(roomSharedPreferences.getString("room_id", "12"));
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    receiverList.addAll(response.body().getUsers());
                    receiverAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_giving_task_deadline: {
                showDateTimePicker();
                break;
            }

            case R.id.giving_task_back_btn: {
                super.onBackPressed();
                break;
            }

            case R.id.giving_task_submit_btn: {
                createNewTask();
                break;
            }
        }
    }

    private void createNewTask() {

        receivers.clear();
        for(int i = 0 ;i < receiverList.size(); i++) {
            receivers.add(receiverList.get(i).getId());
        }
        String title = edtName.getText().toString();
        String desc = edtDesc.getText().toString();
        String deadline = txtTaskDeadline.getText().toString();

        if(!(title.isEmpty() || desc.isEmpty() || deadline.isEmpty())) {
            dialog.setMessage("Đang thêm công việc");
            dialog.show();
            retrofit.create(TaskService.class).
                    createTask(receivers, roomSharedPreferences.getString("room_id", "12"), title, desc, convertToDatetime(deadline))
                    .enqueue(new Callback<StatusPOJO>() {
                        @Override
                        public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                            if(response.isSuccessful() && response.body().getSuccess()) {
                                edtName.setText("");
                                edtDesc.setText("");
                                txtTaskDeadline.setText("Không giới hạn");
                                Toast.makeText(GivingTaskActivity.this, "Thêm công việc thành công", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<StatusPOJO> call, Throwable t) {
                            Log.d("AddTasKErr:", t.getMessage());
                            dialog.dismiss();

                        }
                    });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(GivingTaskActivity.this);
            builder.setTitle("Tiêu đề/ Mô tả không được để trống");
            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.setCancelable(true);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    public static String convertToDatetime(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    Calendar date;
    public void showDateTimePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GivingTaskActivity.this);
        final View view = LayoutInflater.from(GivingTaskActivity.this).inflate(R.layout.layout_datetime_picker, null);
        DatePicker datePicker = view.findViewById(R.id.layout_dt_date_picker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.layout_dt_time_picker);
        builder.setView(view);
        builder.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dateString = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
                String deadline = dateString + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                txtTaskDeadline.setText(deadline);
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}