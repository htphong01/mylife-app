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
import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.Activities.TaskManagementActivity;
import com.htphong.mylife.Adapters.PostsAdapter;
import com.htphong.mylife.Adapters.TaskItemAdapter;
import com.htphong.mylife.Models.Task;
import com.htphong.mylife.POJO.TaskPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskManagementUserFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton taskDeadlineBtn, taskControlBtn;
    private RecyclerView newTaskRecyclerView, deadlineTaskRecyclerView, recentRecyclerView;
    private ArrayList<Task> newTaskList = new ArrayList<>();
    private ArrayList<Task> deadlineTaskList = new ArrayList<>();
    private ArrayList<Task> recentTaskList = new ArrayList<>();
    private RecyclerView.Adapter newTaskAdapter, deadlineTaskAdapter, recentTaskAdapter;
    private SharedPreferences userSharedPreferences, roomSharedPreferences;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_task_management_user, container, false);
        context = container.getContext();
        init();
        return view;
    }

    private void init() {
        userSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        roomSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("chat_room", Context.MODE_PRIVATE);

        fragmentManager = getFragmentManager();

        taskDeadlineBtn = view.findViewById(R.id.task_management_user_deadline_btn);
        taskControlBtn = view.findViewById(R.id.task_management_user_control_btn);

        taskDeadlineBtn.setOnClickListener(this);
        taskControlBtn.setOnClickListener(this);

        initNewTask();
        initDeadlineTask();
        initRecentTask();

        swipeRefreshLayout = view.findViewById(R.id.task_management_user_swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> getTask());
        getTask();
    }

    private void initNewTask() {
        newTaskRecyclerView = view.findViewById(R.id.task_management_user_new_recycler);
        newTaskRecyclerView.setHasFixedSize(true);
        newTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newTaskRecyclerView.setItemViewCacheSize(10);
        newTaskRecyclerView.setDrawingCacheEnabled(true);
        newTaskRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        newTaskAdapter = new TaskItemAdapter(newTaskList, getContext());
        newTaskRecyclerView.setAdapter(newTaskAdapter);
    }

    private void initDeadlineTask() {
        deadlineTaskRecyclerView = view.findViewById(R.id.task_management_user_deadline_recycler);
        deadlineTaskRecyclerView.setHasFixedSize(true);
        deadlineTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        deadlineTaskRecyclerView.setItemViewCacheSize(10);
        deadlineTaskRecyclerView.setDrawingCacheEnabled(true);
        deadlineTaskRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        deadlineTaskAdapter = new TaskItemAdapter(deadlineTaskList, getContext());
        deadlineTaskRecyclerView.setAdapter(deadlineTaskAdapter);
    }

    private void initRecentTask() {
        recentRecyclerView = view.findViewById(R.id.task_management_user_far_recycler);
        recentRecyclerView.setHasFixedSize(true);
        recentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentRecyclerView.setItemViewCacheSize(10);
        recentRecyclerView.setDrawingCacheEnabled(true);
        recentRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recentTaskAdapter = new TaskItemAdapter(recentTaskList, getContext());
        recentRecyclerView.setAdapter(recentTaskAdapter);
    }

    private void getTask() {
        newTaskList.clear();
        deadlineTaskList.clear();
        recentTaskList.clear();
        String user_id = userSharedPreferences.getString("id", "");
        String dateNow = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

        Retrofit retrofit = new Client().getRetrofit(getContext());
        retrofit.create(TaskService.class)
                .getRoomTask(roomSharedPreferences.getString("room_id", ""))
                .enqueue(new Callback<TaskPOJO>() {
                    @Override
                    public void onResponse(Call<TaskPOJO> call, Response<TaskPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            List<Task> listResponseTask = new ArrayList<>();
                            for(int i = 0;i < response.body().getTasks().size(); i++) {
                                if(String.valueOf(response.body().getTasks().get(i).getReceiverId()).equals(user_id)) {
                                    listResponseTask.add(response.body().getTasks().get(i));
                                    if(formatDeadlineTime(response.body().getTasks().get(i).getDeadline()).equals(dateNow) && (i != 0)) {
                                        deadlineTaskList.add(response.body().getTasks().get(i));
                                    } else {
                                        if(i != 0) {
                                            recentTaskList.add(response.body().getTasks().get(i));
                                        }
                                    }
                                }
                            }
                            deadlineTaskAdapter.notifyDataSetChanged();
                            recentTaskAdapter.notifyDataSetChanged();
                           if(listResponseTask.size() > 0) {
                               newTaskList.add(listResponseTask.get(0));
                               newTaskAdapter.notifyDataSetChanged();
                           }
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<TaskPOJO> call, Throwable t) {
                        Log.d("TaskManagementErr: ", t.getMessage());
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
            case R.id.task_management_user_deadline_btn: {
                fragmentManager.beginTransaction().replace(R.id.frameTaskManagementContainer, new TaskManagementDeadlineFragment()).commit();
                break;
            }

            case R.id.task_management_user_control_btn: {
                fragmentManager.beginTransaction().replace(R.id.frameTaskManagementContainer, new TaskManagementControlFragment()).commit();
                break;
            }

        }
    }
}
