package com.htphong.mylife.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htphong.mylife.Constant;
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
