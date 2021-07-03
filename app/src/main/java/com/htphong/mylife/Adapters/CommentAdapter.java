package com.htphong.mylife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Activities.HomeActivity;
import com.htphong.mylife.Activities.ProfileActivity;
import com.htphong.mylife.Fragments.AccountFragment;
import com.htphong.mylife.Utils.Constant;
import com.htphong.mylife.Models.Comment;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> list = new ArrayList<>();
    private Context context;

    public CommentAdapter(ArrayList<Comment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = (Comment)list.get(position);
        Picasso.get().load(Constant.DOMAIN + comment.getUser().getPhoto()).resize(350,350).centerCrop().into(holder.commentAuthorAvatar);
        holder.txtAuthorName.setText(comment.getUser().getUserName());
        if(comment.getType().equals("text")) {
            holder.txtComment.setText(comment.getComment());
            holder.imgCommentContent.setVisibility(View.GONE);
        } else {
            holder.txtComment.setVisibility(View.GONE);
            Picasso.get().load(Constant.DOMAIN + comment.getComment()).resize(350,350).centerCrop().into(holder.imgCommentContent);
        }
        holder.txtCommentTime.setText(Helper.timeDifferent(comment.getCreatedAt()));
        holder.txtAuthorName.setOnClickListener(v -> gotoProfile(String.valueOf(comment.getUserId())));
    }

    private void gotoProfile(String user_id) {
        Intent intent;
        SharedPreferences userTargetPref = context.getApplicationContext().getSharedPreferences("user_target", context.MODE_PRIVATE);
        SharedPreferences userPref = context.getApplicationContext().getSharedPreferences("user", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userTargetPref.edit();
        editor.putString("id", user_id);
        editor.apply();
        if(user_id.equals(userPref.getString("id", "1"))) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView commentAuthorAvatar;
        private TextView txtAuthorName, txtComment, txtCommentTime, txtCommentReply, txtCommentLike;
        private ImageButton btnReport;
        private ImageView imgCommentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthorAvatar = itemView.findViewById(R.id.img_comment_avatar);
            txtAuthorName = itemView.findViewById(R.id.txt_comment_author_name);
            txtComment = itemView.findViewById(R.id.txt_comment_content);
            txtCommentTime = itemView.findViewById(R.id.txt_comment_time);
            txtCommentReply = itemView.findViewById(R.id.txt_comment_reply_btn);
            txtCommentLike = itemView.findViewById(R.id.txt_comment_like_btn);
            btnReport = itemView.findViewById(R.id.comment_report_btn);
            imgCommentContent = itemView.findViewById(R.id.img_comment_content);
        }
    }
}
