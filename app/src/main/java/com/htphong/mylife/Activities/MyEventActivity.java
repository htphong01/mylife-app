package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.EventService;
import com.htphong.mylife.Adapters.MyEventAdapter;
import com.htphong.mylife.Models.Event;
import com.htphong.mylife.POJO.EventPOJO;
import com.htphong.mylife.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyEventActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCalendarView calendarView;
    private Button hostBtn, attendingBtn;
    private TextView eventLabel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton addEventBtn;

    private RecyclerView recyclerEvent;
    private RecyclerView.Adapter eventAdapter;
    private ArrayList<Event> hostEventLists = new ArrayList<>();
    private ArrayList<Event> attendingEventLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        init();
    }

    private void init() {
        calendarView = findViewById(R.id.calendarView);
        hostBtn = findViewById(R.id.my_event_host_btn);
        attendingBtn = findViewById(R.id.my_event_attending_btn);
        eventLabel = findViewById(R.id.my_event_label);
        swipeRefreshLayout = findViewById(R.id.swipe_event);
        addEventBtn = findViewById(R.id.add_event_btn);

        recyclerEvent = findViewById(R.id.recycler_event);
        recyclerEvent.setHasFixedSize(true);
        recyclerEvent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        eventAdapter = new MyEventAdapter(this, hostEventLists);
        recyclerEvent.setAdapter(eventAdapter);

        recyclerEvent.addItemDecoration(null);
        while (recyclerEvent.getItemDecorationCount() > 0) {
            recyclerEvent.removeItemDecorationAt(0);
        }

        calendarView.setTopbarVisible(false);
        calendarView.setSelectedDate(CalendarDay.today());

        hostBtn.setOnClickListener(this);
        attendingBtn.setOnClickListener(this);
        addEventBtn.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(() -> getEvents());
        getEvents();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_event_host_btn: {
                hostBtn.setBackgroundResource(R.drawable.btn_round_black);
                hostBtn.setTextColor(Color.WHITE);
                attendingBtn.setBackgroundResource(R.drawable.btn_round_event_gray);
                attendingBtn.setTextColor(Color.BLACK);
                eventLabel.setText("SỰ KIỆN TỔ CHỨC");

                eventAdapter = new MyEventAdapter(this, hostEventLists);
                recyclerEvent.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.my_event_attending_btn: {
                attendingBtn.setBackgroundResource(R.drawable.btn_round_black);
                attendingBtn.setTextColor(Color.WHITE);
                hostBtn.setBackgroundResource(R.drawable.btn_round_event_gray);
                hostBtn.setTextColor(Color.BLACK);
                eventLabel.setText("SỰ KIỆN THAM GIA");

                eventAdapter = new MyEventAdapter(this, attendingEventLists);
                recyclerEvent.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
                break;
            }

            case R.id.add_event_btn: {
                startActivity(new Intent(MyEventActivity.this, AddEventActivity.class));
                break;
            }
        }
    }

    private void getEvents() {
        hostEventLists.clear();
        attendingEventLists.clear();
        SharedPreferences useSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        Retrofit retrofit = new Client().getRetrofit(this);
        retrofit.create(EventService.class).getEvents("attending")
                .enqueue(new Callback<EventPOJO>() {
                    @Override
                    public void onResponse(Call<EventPOJO> call, Response<EventPOJO> response) {
                        Log.d("MY_EVENT", response.message());
                        Log.d("MY_EVENT", "abc: " + response.body().getEvents().size());
                        if(response.isSuccessful() &&  response.body().getSuccess()) {
                            attendingEventLists.addAll(response.body().getEvents());
                            for(int i = 0; i < attendingEventLists.size(); i++) {
                                if(String.valueOf(attendingEventLists.get(i).getCreaterId()).equals(useSharedPreferences.getString("id", ""))) {
                                    hostEventLists.add(attendingEventLists.get(i));
                                }
                            }
                            eventAdapter.notifyDataSetChanged();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<EventPOJO> call, Throwable t) {
                        Log.d("MY_EVENT_ERR", t.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}