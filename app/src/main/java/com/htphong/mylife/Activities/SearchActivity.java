package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Adapters.SearchAdapter;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.UserPOJO;
import com.htphong.mylife.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack, btnRemove;
    private EditText edtSearch;
    private RecyclerView recyclerSearch;
    private RecyclerView.Adapter adapter;
    private ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    public void init() {
        btnBack = findViewById(R.id.btn_search_back);
        btnRemove = findViewById(R.id.btn_search_remove);
        edtSearch = findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtSearch.getText().toString().isEmpty()) {
                    search();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnBack.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        recyclerSearch = findViewById(R.id.search_recycler);
        recyclerSearch.setNestedScrollingEnabled(true);
        recyclerSearch.setHasFixedSize(true);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter = new SearchAdapter(getApplicationContext(), userList);
        recyclerSearch.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_back: {
                btnBack.setBackgroundResource(R.drawable.button_pressed);
                super.onBackPressed();
                break;
            }
            case R.id.btn_search_remove: {
                edtSearch.setText("");
                break;
            }
        }
    }

    public void search() {
        userList.clear();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        Retrofit retrofit = new Client().getRetrofit(getApplicationContext());;

        UserService userService = retrofit.create(UserService.class);

        Call<UserPOJO> call = userService.getUser(edtSearch.getText().toString());
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    List<User> list = response.body().getUsers();
                    userList.addAll(list);
                    adapter.notifyDataSetChanged();
                    Log.d("SearchSuccess: ", "success");
                }
            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Log.d("Search onFailure: ", t.getMessage());
            }
        });

    }
}