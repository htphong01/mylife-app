package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.ImageActivity;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatImageAdapter extends RecyclerView.Adapter<ChatImageAdapter.ViewHolder> {

    private ArrayList<String> images = new ArrayList<String>();
    private Context context;

    public ChatImageAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_image, parent, false);
        return new ChatImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatImageAdapter.ViewHolder holder, int position) {
        String link = images.get(position);
        Picasso.get().load(Constant.DOMAIN + link).resize(400, 350).centerCrop().into(holder.albumImage);
        holder.albumImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("url", link);
            context.startActivity(intent);
        });
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
