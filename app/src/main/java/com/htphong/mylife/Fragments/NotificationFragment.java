package com.htphong.mylife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.NotificationService;
import com.htphong.mylife.Adapters.NotificationAdapter;
import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.Models.Notifications;
import com.htphong.mylife.POJO.NotificationPOJO;
import com.htphong.mylife.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationFragment extends Fragment {
    private View view;
    private Context mContext;
    private RecyclerView notifyRecyclerview;
    private RecyclerView.Adapter notifyAdapter;
    private ArrayList<Notifications> list = new ArrayList<Notifications>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_notification, container, false);
        mContext = container.getContext();
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    public void init() {
        notifyRecyclerview = view.findViewById(R.id.notify_recycler);
//        notifyRecyclerview.setHasFixedSize(true);
        notifyRecyclerview.setNestedScrollingEnabled(true);
        notifyRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        notifyRecyclerview.setItemViewCacheSize(10);
        notifyRecyclerview.setDrawingCacheEnabled(true);
        notifyRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        notifyAdapter = new NotificationAdapter(getContext(), list);
        notifyAdapter.setHasStableIds(true);
        notifyRecyclerview.setAdapter(notifyAdapter);
        getNotification();
    }

    private void getNotification() {
        list.clear();
        Retrofit retrofit = new Client().getRetrofit((HomeActivity)getContext());
        NotificationService notificationService = retrofit.create(NotificationService.class);
        Call<NotificationPOJO> call = notificationService.getNotification();
        call.enqueue(new Callback<NotificationPOJO>() {
            @Override
            public void onResponse(Call<NotificationPOJO> call, Response<NotificationPOJO> response) {
                Log.d("NotificationResponse: ", response.body().getSuccess().toString());
                if(response.isSuccessful() && response.body().getSuccess()) {
                    List<Notifications> listNotifications = response.body().getNotifications();
                    for(int i = 0;i < listNotifications.size(); i++) {
                        list.add(listNotifications.get(i));
                    }
                    notifyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NotificationPOJO> call, Throwable t) {
                Log.d("NotificationError:", t.getMessage());
            }
        });
    }

}
