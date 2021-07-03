package com.htphong.mylife.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.EventService;
import com.htphong.mylife.Models.Event;
import com.htphong.mylife.POJO.EventPOJO;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventInfoActivity extends AppCompatActivity {

    private TextView txtLabel, txtTitle, txtTime, txtPlace, txtContent, txtAttender;
    private CircleImageView imgAvatar;
    private Button eventAttendBtn;
    private ImageButton backBtn;
    private static int totalAttender = 0;
    private static boolean isAttended = false;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        init();
    }

    private void init() {
        txtLabel = findViewById(R.id.event_label);
        txtTitle = findViewById(R.id.name_event);
        txtTime = findViewById(R.id.event_time);
        txtContent = findViewById(R.id.event_content);
        txtPlace = findViewById(R.id.event_place);
        txtAttender = findViewById(R.id.event_attender);
        imgAvatar = findViewById(R.id.event_image);
        eventAttendBtn = findViewById(R.id.event_btn);
        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(v -> onBackPressed());

        eventAttendBtn.setOnClickListener(v -> {
            SharedPreferences userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
            if(!String.valueOf(event.getCreaterId()).equals(userSharedPreferences.getString("id", ""))) {
                updateAttender();
            } else {
                openDialogResponse();
            }
        });

        getEventInfo();
    }

    private void getEventInfo() {
        Retrofit retrofit = new Client().getRetrofit(this);
        retrofit.create(EventService.class).getEventInfo(getIntent().getExtras().getInt("event_id"))
                .enqueue(new Callback<EventPOJO>() {
                    @Override
                    public void onResponse(Call<EventPOJO> call, Response<EventPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            event = response.body().getEvents().get(0);
                            txtTitle.setText(event.getTitle());
                            txtLabel.setText(event.getCreaterName() + " đã tổ chức sự kiện");
                            txtTime.setText(Helper.formatDeadlineTime(event.getDateStart()) + "\n" + Helper.formatDeadlineTime(event.getDateEnd()));
                            txtContent.setText(event.getContent());
                            txtPlace.setText(event.getAddress());
                            totalAttender = event.getTotalAttenders();
                            txtAttender.setText(totalAttender + " người sẽ tham gia");
                            Picasso.get().load(Constant.DOMAIN + event.getImage()).resize(400, 400).centerCrop().into(imgAvatar);
                            eventAttendBtn.setText(event.getAttended() ? "Đã tham gia" : "Tham gia");
                            isAttended = event.getAttended();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventPOJO> call, Throwable t) {

                    }
                });
    }

    private void updateAttender() {
        isAttended = !isAttended;
        totalAttender = isAttended ? totalAttender + 1 : totalAttender - 1;
        txtAttender.setText(totalAttender + " người sẽ tham gia");
        eventAttendBtn.setText(isAttended ? "Đã tham gia" : "Tham gia");

        Retrofit retrofit = new Client().getRetrofit(this);
        retrofit.create(EventService.class).updateAttender(getIntent().getExtras().getInt("event_id"), getIntent().getExtras().getInt("event_id"))
                .enqueue(new Callback<StatusPOJO>() {
                    @Override
                    public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {

                    }

                    @Override
                    public void onFailure(Call<StatusPOJO> call, Throwable t) {

                    }
                });
    }

    private void openDialogResponse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventInfoActivity.this);
        builder.setMessage("Sự kiện này do bạn tổ chức. Bạn không thể không tham gia");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}