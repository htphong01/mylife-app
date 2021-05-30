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

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Adapters.PostsAdapter;
import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.PostPOJO;
import com.htphong.mylife.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private View view;
    public static RecyclerView recyclerView;
    public static ArrayList<Post> postArrayList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        view = inflater.inflate(R.layout.layout_home, container, false);
        init();
        mContext = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getPosts();
    }

    private void init() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        postsAdapter = new PostsAdapter((HomeActivity)getContext(), postArrayList);
        postsAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postsAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeHome);
        toolbar = view.findViewById(R.id.toolbarHome);
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });

    }

    private void getPosts() {
        postArrayList.clear();
        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit = new Client().getRetrofit(getActivity().getApplicationContext());;

        PostService postService = retrofit.create(PostService.class);

        Call<PostPOJO> call = postService.getPost("all");
        call.enqueue(new Callback<PostPOJO>() {
            @Override
            public void onResponse(Call<PostPOJO> call, Response<PostPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    List<Post> post = response.body().getPosts();
                    for(int i = 0; i < post.size(); i++) {
                        postArrayList.add(post.get(i));
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostPOJO> call, Throwable t) {
                Log.d("HomeFragment: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
