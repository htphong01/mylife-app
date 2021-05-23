package com.htphong.mylife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Adapters.SearchAdapter;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.SearchPOJO;

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
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        btnBack.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        recyclerSearch = findViewById(R.id.search_recycler);
        recyclerSearch.setNestedScrollingEnabled(true);
//        recyclerSearch.setHasFixedSize(true);
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

        Call<SearchPOJO> call = userService.getUser(edtSearch.getText().toString());
        call.enqueue(new Callback<SearchPOJO>() {
            @Override
            public void onResponse(Call<SearchPOJO> call, Response<SearchPOJO> response) {
                if(response.isSuccessful()) {
                    List<User> list = response.body().getUsers();
                    for(int i = 0; i < list.size(); i++) {
                        User user = new User();
                        user.setId(list.get(i).getId());
                        user.setUserName(list.get(i).getUserName());
                        user.setPhoto(list.get(i).getPhoto());
                        userList.add(user);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchPOJO> call, Throwable t) {
                Log.d("Search onFailure: ", t.getMessage());
            }
        });

    }
}