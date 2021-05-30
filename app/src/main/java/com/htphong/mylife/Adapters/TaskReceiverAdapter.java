package com.htphong.mylife.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Models.User;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskReceiverAdapter extends RecyclerView.Adapter<TaskReceiverAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> list = new ArrayList<>();

    public TaskReceiverAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_receiver, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = (User) list.get(position);
        Picasso.get().load(Constant.DOMAIN + user.getPhoto()).resize(100, 100).centerCrop().into(holder.imgAvatar);
        holder.txtName.setText(user.getUserName());
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imgAvatar;
        private TextView txtName;
        private ImageButton removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_layout_task_receiver_avatar);
            txtName = itemView.findViewById(R.id.txt_layout_task_receiver_name);
            removeBtn = itemView.findViewById(R.id.layout_task_receiver_remove_btn);

        }
    }
}
