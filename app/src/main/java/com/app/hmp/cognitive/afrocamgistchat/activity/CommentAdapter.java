package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.app.hmp.cognitive.afrocamgistchat.R;
import com.app.hmp.cognitive.afrocamgistchat.adapters.MenuAdapter;
import com.app.hmp.cognitive.afrocamgistchat.adapters.SubCommentAdapter;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.DataObjectHolder>
        implements SubCommentAdapter.OnCommentClickListener, MenuAdapter.OnMenuItemClickListener {

    private Activity context;
    private ArrayList<Comment> comments;
    private OnCommentClickListener listener;
    private PopupWindow popup;

    public CommentAdapter(Activity context, ArrayList<Comment> comments, OnCommentClickListener listener) {
        this.context = context;
        this.comments = comments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment, viewGroup, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {

        Comment comment = comments.get(position);

        if(comment.getCommentedBy().getUser_name()!=null && !comment.getCommentedBy().getUser_name().isEmpty()){
            holder.name.setText(comment.getCommentedBy().getUser_name());
        }else {
            String name = comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName();
            holder.name.setText(name);
        }

        Glide.with(holder.profilePicture)
                .load(UrlEndpoints.MEDIA_BASE_URL + comment.getCommentedBy().getProfileImageUrl())
                .into(holder.profilePicture);

        holder.userComment.setText(comment.getCommentText());

        holder.like.setChecked(comment.getLiked());

        if (comment.getLiked()) {
            //holder.liked.setText("You Liked");
            if(comment.getComment_likes() != 0){
                holder.liked.setText(comment.getComment_likes() + " like");
            }
            holder.liked.setTextColor(Color.parseColor("#FF7400"));
        } else {
            //holder.liked.setText("Like");
            if(comment.getComment_likes() != 0){
                holder.liked.setText(comment.getComment_likes() + " like");
            }
            holder.liked.setTextColor(Color.parseColor("#9F9F9F"));
        }

        if (LocalStorage.getUserDetails().getUserId().equals(comment.getCommentedBy().getUserId()))
            holder.menu.setVisibility(View.VISIBLE);
        else
            holder.menu.setVisibility(View.GONE);

        setClickListener(holder);
        setSubCommentRecyclerView(holder);

    }

    private void setClickListener(DataObjectHolder holder) {

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (LocalStorage.getUserDetails().getUserId().equals(comments.get(holder.getAdapterPosition()).getCommentedBy().getUserId()))
//                    context.startActivity(new Intent(context, MyProfileActivity.class));
//                else
//                    context.startActivity(new Intent(context, ProfileActivity.class)
//                            .putExtra("userId", comments.get(holder.getAdapterPosition()).getCommentedBy().getUserId()));
            }
        });

        holder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        //holder.liked.setText("You Liked");
                        holder.liked.setTextColor(Color.parseColor("#FF7400"));
                        holder.liked.setText((comments.get(holder.getAdapterPosition()).getComment_likes() + 1) + " like");
                        //comments.get(holder.getAdapterPosition()).setLiked(true);
                        comments.get(holder.getAdapterPosition()).setComment_likes(comments.get(holder.getAdapterPosition()).getComment_likes() + 1);
                    } else {

                        //holder.liked.setText("Like");
                        holder.liked.setTextColor(Color.parseColor("#9F9F9F"));
                        if(comments.get(holder.getAdapterPosition()).getComment_likes() == 0){
                            holder.liked.setText("Like");
                        }else {
                            holder.liked.setText((comments.get(holder.getAdapterPosition()).getComment_likes() - 1) + " like");
                        }
                        //comments.get(holder.getAdapterPosition()).setLiked(false);
                        comments.get(holder.getAdapterPosition()).setComment_likes(comments.get(holder.getAdapterPosition()).getComment_likes() - 1);
                    }
                }

                if (buttonView.isPressed()) {
                    comments.get(holder.getAdapterPosition()).setLiked(isChecked);
                    listener.onLikeCommentClicked(comments.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReplyClicked(holder.getAdapterPosition(),comments.get(holder.getAdapterPosition()),null);
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(holder.menu,holder.getAdapterPosition());
            }
        });
    }

    private void setSubCommentRecyclerView(DataObjectHolder holder) {

        int position = holder.getAdapterPosition();

        if (comments.get(position).getSubComments()!=null && comments.get(position).getSubComments().size() > 0) {
            holder.subCommentList.setVisibility(View.VISIBLE);
            holder.subCommentList.setLayoutManager(new LinearLayoutManager(context));
            holder.subCommentList.setAdapter(new SubCommentAdapter(context, position, comments.get(position),
                    comments.get(position).getSubComments(), this));
        } else {
            holder.subCommentList.setVisibility(View.GONE);
        }
    }

    private void showMenu(View menu, int position) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int popupWidth = 450;
        int popupHeight = LinearLayout.LayoutParams.WRAP_CONTENT;

        LinearLayout viewGroup = context.findViewById(R.id.menu_layout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.drop_down_menu, viewGroup);
        popupMenu(layout, position);

        popup = new PopupWindow(layout, popupWidth, popupHeight);
        popup.setFocusable(true);
        popup.setOutsideTouchable(false);
        /*if (position == (posts.size()-1))
            popup.showAsDropDown(menu,0,-300, Gravity.TOP);
        else*/
            popup.showAsDropDown(menu);
    }

    private void popupMenu(View layout, int position) {

        RecyclerView menu = layout.findViewById(R.id.menuList);
        menu.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        menu.setLayoutManager(mLayoutManager);
        MenuAdapter adapter = new MenuAdapter(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.post_menu))), position,this);
        menu.setAdapter(adapter);
    }

    @Override
    public void onMenuItemClicked(String item, int commentPosition) {

        if (popup!=null && popup.isShowing())
            popup.dismiss();

        switch (item.toLowerCase()) {

            case "edit":
                listener.onEditCommentClicked(commentPosition,comments.get(commentPosition),-1, null);
                break;
            case "delete":
                listener.onDeleteCommentClicked(commentPosition,comments.get(commentPosition),-1, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLikeCommentClicked(Comment comment) {
        listener.onLikeCommentClicked(comment);
    }

    @Override
    public void onReplyClicked(int position,Comment parentComment, Comment comment) {
        listener.onReplyClicked(position,parentComment, comment);
    }

    @Override
    public void onDeleteCommentClicked(int parentPosition, Comment parentComment, int position, Comment comment) {
        listener.onDeleteCommentClicked(parentPosition, parentComment, position, comment);
    }

    @Override
    public void onEditCommentClicked(int parentPosition, Comment parentComment, int position, Comment comment) {
        listener.onEditCommentClicked(parentPosition, parentComment, position, comment);
    }

    @Override
    public int getItemCount() {

        if (comments == null)
            return 0;
        else
            return comments.size();
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePicture;
        TextView name, userComment, liked, reply;
        ToggleButton like;
        RecyclerView subCommentList;
        ImageView menu;

        DataObjectHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            name = itemView.findViewById(R.id.name);
            userComment = itemView.findViewById(R.id.user_comment);
            liked = itemView.findViewById(R.id.liked);
            reply = itemView.findViewById(R.id.reply);
            like = itemView.findViewById(R.id.like);
            menu = itemView.findViewById(R.id.menu);
            subCommentList = itemView.findViewById(R.id.sub_comment_list);
        }
    }

    public interface OnCommentClickListener {
        void onLikeCommentClicked(Comment comment);
        void onReplyClicked(int position, Comment parentComment, Comment subComment);
        void onDeleteCommentClicked(int parentPosition, Comment parentComment, int position, Comment comment);
        void onEditCommentClicked(int parentPosition, Comment parentComment, int position, Comment comment);
    }
}
