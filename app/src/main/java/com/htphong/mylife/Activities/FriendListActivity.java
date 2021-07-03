package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.FriendService;
import com.htphong.mylife.Adapters.FriendAdapter;
import com.htphong.mylife.Models.Friend;
import com.htphong.mylife.POJO.FriendPOJO;
import com.htphong.mylife.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FriendListActivity extends AppCompatActivity {

    private TextView txtFriendCount;
    private RecyclerView recyclerFriendList;
    private EditText edtSearch;
    private ImageButton backBtn;
    private FriendAdapter friendAdapter;

    private ArrayList<Friend> friendList = new ArrayList<>();
    private ArrayList<Friend> friendListTemp = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        init();
    }

    private void init() {
        txtFriendCount = findViewById(R.id.txt_friend_count);
        edtSearch = findViewById(R.id.edt_search_friend);
        backBtn = findViewById(R.id.btn_back);
        recyclerFriendList = findViewById(R.id.recycler_friend_list);
        recyclerFriendList.setHasFixedSize(true);
        recyclerFriendList.setLayoutManager(new LinearLayoutManager(FriendListActivity.this, RecyclerView.VERTICAL, false));
        friendAdapter = new FriendAdapter(friendList, FriendListActivity.this);
        recyclerFriendList.setAdapter(friendAdapter);

        backBtn.setOnClickListener(v -> onBackPressed());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                friendList.clear();
                if(!edtSearch.getText().toString().isEmpty()) {
                    for(int i = 0; i < friendListTemp.size(); i++) {
                        if(friendListTemp.get(i).getFriendName().indexOf(edtSearch.getText().toString()) != -1) {
                            friendList.add(friendListTemp.get(i));
                        }
                    }
                } else {
                    friendList.addAll(friendListTemp);
                }
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getFriends();
    }

    private void getFriends() {
        friendList.clear();
        Retrofit retrofit = new Client().getRetrofit(FriendListActivity.this);
        retrofit.create(FriendService.class)
                .getFriendOfUser()
                .enqueue(new Callback<FriendPOJO>() {
                    @Override
                    public void onResponse(Call<FriendPOJO> call, Response<FriendPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            friendList.addAll(response.body().getFriends());
                            friendListTemp.addAll(response.body().getFriends());
                        }
                        friendAdapter.notifyDataSetChanged();
                        txtFriendCount.setText(friendList.size() + " người bạn");
                    }

                    @Override
                    public void onFailure(Call<FriendPOJO> call, Throwable t) {

                    }
                });
    }
}