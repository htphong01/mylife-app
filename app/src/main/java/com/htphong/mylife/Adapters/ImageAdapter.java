package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.PostActivity;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Post> images = new ArrayList<Post>();
    private Context context;

    public ImageAdapter(ArrayList<Post> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = images.get(position);
        Picasso.get().load(Constant.DOMAIN + post.getPhoto()).resize(400, 350).centerCrop().into(holder.albumImage);
        holder.albumImage.setOnClickListener(v -> goToPost(post));
    }

    private void goToPost(Post post) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("post_id", post.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.layout_account_album_image);
        }
    }
}
