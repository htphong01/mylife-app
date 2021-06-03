package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.API.UserService;
import com.htphong.mylife.Adapters.PostsAdapter;
import com.htphong.mylife.Adapters.SearchAdapter;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.POJO.PostPOJO;
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
    private TabLayout tabLayoutSearch;
    private RecyclerView recyclerSearch;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.Adapter postAdapter;
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<Post> postList = new ArrayList<>();
    private String searchOption = "people";

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
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) ) {
                    if(!edtSearch.getText().toString().isEmpty()) {
                        if(searchOption.equals("people")) {
                            searchUser();
                        } else if (searchOption.equals("post")) {
                            searchPost();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        tabLayoutSearch = findViewById(R.id.tabLayout_search);
        tabLayoutSearch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    searchOption = "people";
                    if(!edtSearch.getText().toString().isEmpty()) {
                        searchUser();
                    }
                } else if(tab.getPosition() == 1) {
                    searchOption = "post";
                    if(!edtSearch.getText().toString().isEmpty()) {
                        searchPost();
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btnBack.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        recyclerSearch = findViewById(R.id.search_recycler);
        recyclerSearch.setNestedScrollingEnabled(true);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
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

    public void searchUser() {
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
                    userAdapter = new SearchAdapter(getApplicationContext(), userList);
                    recyclerSearch.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                    Log.d("SearchSuccess: ", "success");
                }
            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Log.d("Search onFailure: ", t.getMessage());
            }
        });

    }

    private void searchPost() {
        postList.clear();
        Retrofit retrofit = new Client().getRetrofit(SearchActivity.this);
        retrofit.create(PostService.class)
                .searchPost(edtSearch.getText().toString())
                .enqueue(new Callback<PostPOJO>() {
                    @Override
                    public void onResponse(Call<PostPOJO> call, Response<PostPOJO> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            postList.addAll(response.body().getPosts());
                            postAdapter = new PostsAdapter(getApplicationContext(), postList);
                            recyclerSearch.setAdapter(postAdapter);
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<PostPOJO> call, Throwable t) {
                        Log.d("Search onFailure: ", t.getMessage());
                    }
                });
    }
}