package com.htphong.mylife.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.TaskService;
import com.htphong.mylife.Adapters.TaskItemAdapter;
import com.htphong.mylife.Models.Task;
import com.htphong.mylife.POJO.TaskPOJO;
import com.htphong.mylife.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskManagementDeadlineFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton taskControlBtn, taskUserBtn;
    private RecyclerView todayTaskRecyclerView, tomorrowTaskRecyclerView, recentTaskRecyclerView;
    private ArrayList<Task> todayTaskList = new ArrayList<>();
    private ArrayList<Task> tomorrowTaskList = new ArrayList<>();
    private ArrayList<Task> recentTaskList = new ArrayList<>();
    private RecyclerView.Adapter todayTaskAdapter, tomorrowTaskAdapter, recentTaskAdapter;
    private SharedPreferences userSharedPreferences, roomSharedPreferences;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_task_management_deadline, container, false);
        context = container.getContext();
        init();
        return view;
    }

    private void init() {
        userSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        roomSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("chat_room", Context.MODE_PRIVATE);

        fragmentManager = getFragmentManager();

        taskUserBtn = view.findViewById(R.id.task_management_deadline_user_btn);
        taskControlBtn = view.findViewById(R.id.task_management_deadline_control_btn);

        taskUserBtn.setOnClickListener(this);
        taskControlBtn.setOnClickListener(this);

        initTodayTask();
        initRecentTask();
        initTomorrowTask();

        swipeRefreshLayout = view.findViewById(R.id.task_management_deadline_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTask();
            }
        });

        getTask();
    }

    private void initTodayTask() {
        todayTaskRecyclerView = view.findViewById(R.id.task_management_deadline_today_recycler);
        todayTaskRecyclerView.setHasFixedSize(true);
        todayTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todayTaskRecyclerView.setItemViewCacheSize(10);
        todayTaskRecyclerView.setDrawingCacheEnabled(true);
        todayTaskRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        todayTaskAdapter = new TaskItemAdapter(todayTaskList, getContext());
        todayTaskRecyclerView.setAdapter(todayTaskAdapter);
    }

    private void initTomorrowTask() {
        tomorrowTaskRecyclerView = view.findViewById(R.id.task_management_deadline_tomorrow_recycler);
        tomorrowTaskRecyclerView.setHasFixedSize(true);
        tomorrowTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tomorrowTaskRecyclerView.setItemViewCacheSize(10);
        tomorrowTaskRecyclerView.setDrawingCacheEnabled(true);
        tomorrowTaskRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        tomorrowTaskAdapter = new TaskItemAdapter(tomorrowTaskList, getContext());
        tomorrowTaskRecyclerView.setAdapter(tomorrowTaskAdapter);
    }

    private void initRecentTask() {
        recentTaskRecyclerView = view.findViewById(R.id.task_management_deadline_recent_recycler);
        recentTaskRecyclerView.setHasFixedSize(true);
        recentTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentTaskRecyclerView.setItemViewCacheSize(10);
        recentTaskRecyclerView.setDrawingCacheEnabled(true);
        recentTaskRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recentTaskAdapter = new TaskItemAdapter(recentTaskList, getContext());
        recentTaskRecyclerView.setAdapter(recentTaskAdapter);
    }

    private void getTask() {
        todayTaskList.clear();
        tomorrowTaskList.clear();
        recentTaskList.clear();
        String user_id = userSharedPreferences.getString("id", "");

        Calendar calendar = Calendar.getInstance();
        String dateNow = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String dateTomorrow = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());

        Retrofit retrofit = new Client().getRetrofit(getContext());
        retrofit.create(TaskService.class).getRoomTask(roomSharedPreferences.getString("room_id", ""))
                .enqueue(new Callback<TaskPOJO>() {
                    @Override
                    public void onResponse(Call<TaskPOJO> call, Response<TaskPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            List<Task> listResponseTask = new ArrayList<>();
                            for(int i = 0; i < response.body().getTasks().size(); i++) {
                                if(String.valueOf(response.body().getTasks().get(i).getReceiverId()).equals(user_id)) {
                                    listResponseTask.add(response.body().getTasks().get(i));
                                }
                            }

                            for(int i = 0 ;i <listResponseTask.size(); i++) {
                                if(formatDeadlineTime(listResponseTask.get(i).getDeadline()).equals(dateNow)) {
                                    todayTaskList.add(listResponseTask.get(i));
                                } else if(formatDeadlineTime(listResponseTask.get(i).getDeadline()).equals(dateTomorrow)) {
                                    tomorrowTaskList.add(listResponseTask.get(i));
                                } else {
                                    recentTaskList.add(listResponseTask.get(i));
                                }
                            }
                            todayTaskAdapter.notifyDataSetChanged();
                            tomorrowTaskAdapter.notifyDataSetChanged();
                            recentTaskAdapter.notifyDataSetChanged();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<TaskPOJO> call, Throwable t) {
                        Log.d("DeadlineTask: ", t.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public static String formatDeadlineTime(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
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
            case R.id.task_management_deadline_user_btn: {
                fragmentManager.beginTransaction().replace(R.id.frameTaskManagementContainer, new TaskManagementUserFragment()).commit();
                break;
            }

            case R.id.task_management_deadline_control_btn: {
                fragmentManager.beginTransaction().replace(R.id.frameTaskManagementContainer, new TaskManagementControlFragment()).commit();
                break;
            }
        }
    }
}
