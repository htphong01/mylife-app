package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.EventService;
import com.htphong.mylife.Adapters.EventAdapter;
import com.htphong.mylife.Fragments.TopEventFragment;
import com.htphong.mylife.Models.Event;
import com.htphong.mylife.POJO.EventPOJO;
import com.htphong.mylife.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerEvent;
    private RecyclerView.Adapter adapter;
    private ArrayList<Event> listEvents = new ArrayList<>();
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        init();
    }

    private void init() {
        recyclerEvent = findViewById(R.id.recycler_event);
        recyclerEvent.setHasFixedSize(true);
        recyclerEvent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new EventAdapter(this, listEvents);
        recyclerEvent.setAdapter(adapter);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> onBackPressed());

        findViewById(R.id.my_event_btn).setOnClickListener(v -> {
            startActivity(new Intent(EventActivity.this, MyEventActivity.class));
        });
        getEvents();
    }

    private void getEvents() {
        Retrofit retrofit = new Client().getRetrofit(this);
        retrofit.create(EventService.class).getEvents("top")
                .enqueue(new Callback<EventPOJO>() {
                    @Override
                    public void onResponse(Call<EventPOJO> call, Response<EventPOJO> response) {
                        Log.d("EVENT_RES: ", response.message());
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            listEvents.addAll(response.body().getEvents());
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<EventPOJO> call, Throwable t) {
                        Log.d("EVENT_ERR: ", t.getMessage());
                    }
                });
    };
}