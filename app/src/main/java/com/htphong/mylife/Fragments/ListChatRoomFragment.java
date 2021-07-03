package com.htphong.mylife.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.htphong.mylife.API.ChattingService;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.Adapters.ChatRoomAdapter;
import com.htphong.mylife.Models.Room;
import com.htphong.mylife.POJO.RoomPOJO;
import com.htphong.mylife.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListChatRoomFragment extends Fragment {

    private View view;
    private Context context;
    public static RecyclerView recyclerView;
    public static ArrayList<Room> roomArrayList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatRoomAdapter chatRoomAdapter;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chat_room_fragment, container, false);
        context = container.getContext();
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getListChatRoom();
    }

    private void init() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recycler_chat_room);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        chatRoomAdapter = new ChatRoomAdapter(roomArrayList, getContext());
        recyclerView.setAdapter(chatRoomAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_list_chat_room);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListChatRoom();
            }
        });

    }

    private void getListChatRoom() {
        roomArrayList.clear();
        Retrofit retrofit = new Client().getRetrofit(getContext());
        ChattingService chattingService = retrofit.create(ChattingService.class);
        Call<RoomPOJO> call = chattingService.getListChatRoom();
        call.enqueue(new Callback<RoomPOJO>() {
            @Override
            public void onResponse(Call<RoomPOJO> call, Response<RoomPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    List<Room> listRoom = response.body().getRooms();
                    Collections.reverse(listRoom);
                    roomArrayList.addAll(listRoom);
                    chatRoomAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RoomPOJO> call, Throwable t) {
                Log.d("ListChatRoomErr:", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
