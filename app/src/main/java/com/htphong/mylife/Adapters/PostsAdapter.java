package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Fragments.AccountFragment;
import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.Activities.PostActivity;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> {

    private Context context;
    private ArrayList<Post> list = new ArrayList<Post>();

    public PostsAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Post post = (Post) list.get(position);
        Picasso.get().load(Constant.DOMAIN + post.getUser().getPhoto()).resize(350,350).centerCrop().into(holder.imgProfile);
        Picasso.get().load(Constant.DOMAIN + post.getPhoto()).resize(350,600).centerInside().into(holder.imgPost);
        holder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(post);
            }
        });
        holder.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost(post);
            }
        });
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(post);
            }
        });
        holder.txtName.setText(post.getUser().getUserName());
        holder.txtDesc.setText(post.getDescription());
        holder.txtComments.setText("Xem tât cả " + post.getCommentsCount() + " bình luận");
        holder.txtLikes.setText(post.getLikesCount() + " lượt thích");
        holder.txtTime.setText(Helper.timeDifferent(post.getCreatedAt()));
        holder.btnLike.setImageResource(post.getSelfLike() ? R.drawable.ic_baseline_favorite_red : R.drawable.ic_baseline_favorite_outline);
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnLike.setImageResource(post.getSelfLike() ? R.drawable.ic_baseline_favorite_outline : R.drawable.ic_baseline_favorite_red);
                likePost(position);
            }
        });
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost(post);
            }
        });
    }

    private void goToPost(Post post) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("post_id", post.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void likePost(Integer position) {
        Post post = list.get(position);
        Retrofit retrofit = new Client().getRetrofit(context);
        PostService postService = retrofit.create(PostService.class);
        Call<StatusPOJO> call = postService.likePost(String.valueOf(post.getId()));
        call.enqueue(new Callback<StatusPOJO>() {
            @Override
            public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                if(response.isSuccessful() && response.body().getSuccess()) {
                    post.setSelfLike(!post.getSelfLike());
                    post.setLikesCount(post.getSelfLike() ? (post.getLikesCount() + 1) : (post.getLikesCount() - 1));
                    list.set(position, post);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<StatusPOJO> call, Throwable t) {
                Log.d("PostLike: ", t.getMessage());
            }
        });
    }

    private void goToProfile(Post post) {
        Intent intent;
        SharedPreferences userTargetPref = context.getApplicationContext().getSharedPreferences("user_target", context.MODE_PRIVATE);
        SharedPreferences userPref = context.getApplicationContext().getSharedPreferences("user", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userTargetPref.edit();
        editor.putString("id", post.getUser().getId());
        editor.apply();
        if(String.valueOf(post.getUser().getId()).equals(userPref.getString("id", "1"))) {
            HomeActivity homeActivity = (HomeActivity)context;
            homeActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer, new AccountFragment()).commit();
        } else {
            intent = new Intent(context, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    class PostsHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtTime,  txtDesc, txtLikes, txtComments;
        private CircleImageView imgProfile;
        private ImageView imgPost;
        private ImageButton btnPostOption, btnLike, btnComment;

        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPostName);
            txtTime = itemView.findViewById(R.id.txtPostTime);
            txtDesc = itemView.findViewById(R.id.txtPostDesc);
            txtLikes = itemView.findViewById(R.id.txtPostLikes);
            txtComments = itemView.findViewById(R.id.txtPostComments);
            imgProfile = itemView.findViewById(R.id.imgPostProfile);
            imgPost = itemView.findViewById(R.id.imgPostPhoto);
            btnPostOption = itemView.findViewById(R.id.btnPostOption);
            btnLike = itemView.findViewById(R.id.btnPostLike);
            btnComment = itemView.findViewById(R.id.btnPostComment);

        }
    }
}
