package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

    private ImageButton btnBack, btnRemove, btnMicro;
    private EditText edtSearch;
    private TabLayout tabLayoutSearch;
    private RecyclerView recyclerSearch;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.Adapter postAdapter;
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<Post> postList = new ArrayList<>();
    private String searchOption = "people";

    private static final int SPEECH_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    public void init() {
        btnBack = findViewById(R.id.btn_search_back);
        btnRemove = findViewById(R.id.btn_search_remove);
        btnMicro = findViewById(R.id.btn_search_micro);
        edtSearch = findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtSearch.getText().toString().isEmpty()) {
                    btnMicro.setVisibility(View.VISIBLE);
                    btnRemove.setVisibility(View.GONE);
                } else {
                    btnMicro.setVisibility(View.GONE);
                    btnRemove.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnKeyListener((v, keyCode, event) -> {
            if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) ) {
                if(!edtSearch.getText().toString().isEmpty()) {
                    search();
                }
                return true;
            }
            return false;
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
        btnMicro.setOnClickListener(this);

        recyclerSearch = findViewById(R.id.search_recycler);
        recyclerSearch.setNestedScrollingEnabled(true);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
    }

    private void search() {
        if(searchOption.equals("people")) {
            searchUser();
        } else if (searchOption.equals("post")) {
            searchPost();
        }
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

            case R.id.btn_search_micro: {
                displaySpeechRecognizer();
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

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            edtSearch.setText(spokenText);
            search();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}