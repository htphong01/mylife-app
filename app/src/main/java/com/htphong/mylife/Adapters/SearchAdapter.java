package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.User;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<User> list = new ArrayList<>();
    private Context context;

    public SearchAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = (User) list.get(position);
        Picasso.get().load(Constant.DOMAIN + user.getPhoto())
                .resize(350,350)
                .centerCrop()
                .into(holder.imgSearchUser);
        holder.txtSearchUserName.setText(user.getUserName());
        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user_id", String.valueOf(user.getId()));
                SharedPreferences userPref = context.getApplicationContext().getSharedPreferences("user_target", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userPref.edit();
                editor.putString("id", user.getId());
                editor.apply();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgSearchUser;
        private TextView txtSearchUserName;
        private LinearLayout searchItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSearchUser = itemView.findViewById(R.id.img_search_user);
            txtSearchUserName = itemView.findViewById(R.id.txt_search_username);
            searchItem = itemView.findViewById(R.id.search_item);
        }
    }

}
