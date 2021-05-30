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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Adapters.ImageAdapter;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.PostPOJO;
import com.htphong.mylife.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountAlbumFragment extends Fragment {

    private View view;
    private Context context;
    private ArrayList<String> imageLinks = new ArrayList<>();
    private RecyclerView recyclerAccountPost;
    private RecyclerView.Adapter postAdapter;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account_post, container, false);
        context = container.getContext();
        init();
        return view;
    }

    private void init() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerAccountPost = view.findViewById(R.id.recycler_account_posts);
//        recyclerAccountPost.setHasFixedSize(true);
        recyclerAccountPost.setNestedScrollingEnabled(true);
        recyclerAccountPost.setLayoutManager(new GridLayoutManager(getContext(), 3));
        postAdapter = new ImageAdapter(imageLinks);
        recyclerAccountPost.setAdapter(postAdapter);
        getPosts();
    }

    private void getPosts() {
        imageLinks.clear();
        Retrofit retrofit = new Client().getRetrofit(getActivity().getApplicationContext());;

        PostService postService = retrofit.create(PostService.class);

        Call<PostPOJO> call = postService.getPost(sharedPreferences.getString("id", "all"));
        call.enqueue(new Callback<PostPOJO>() {
            @Override
            public void onResponse(Call<PostPOJO> call, Response<PostPOJO> response) {
                if(response.isSuccessful()) {
                    List<Post> post = response.body().getPosts();
                    for(int i = 0; i < post.size(); i++) {
                        imageLinks.add(post.get(i).getPhoto());
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostPOJO> call, Throwable t) {
                Log.d("HomeFragment: ", t.getMessage());
            }
        });

    }
}
