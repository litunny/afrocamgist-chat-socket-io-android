package com.app.hmp.cognitive.afrocamgistchat.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hmp.cognitive.afrocamgistchat.R;
import com.app.hmp.cognitive.afrocamgistchat.activity.UrlEndpoints;
import com.app.hmp.cognitive.afrocamgistchat.activity.Utils;
import com.app.hmp.cognitive.afrocamgistchat.model.Conversation;
import com.app.hmp.cognitive.afrocamgistchat.model.User;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class AfroChatAdapter extends RecyclerView.Adapter<AfroChatAdapter.DataObjectHolder> {

    private ArrayList<Conversation> conversations;
    private OnChatItemClickListener listener;

    public AfroChatAdapter(ArrayList<Conversation> conversations, OnChatItemClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_chat, viewGroup, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {

        Conversation conversation = conversations.get(position);

        Glide.with(holder.profileImage)
                .load(UrlEndpoints.MEDIA_BASE_URL +  conversation.getProfileImageUrl())
                .into(holder.profileImage);

        if(conversation.getOnlineStatus()!=null){
            if (conversation.getOnlineStatus()) {
                holder.online.setVisibility(View.VISIBLE);
                holder.offline.setVisibility(View.GONE);
            } else {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            }
        }

        String fullName = conversation.getFirstName() + " " + conversation.getLastName();
        holder.name.setText(fullName);

        String message = "";

        if (conversation.getLastMessage()==null) {
            message = "Start a conversation";
            holder.time.setVisibility(View.GONE);
            holder.unreadMessageCount.setVisibility(View.GONE);
        } else {

            if ("".equals(conversation.getLastMessage())) {
                message = "Attachment";
            } else {
                message = conversation.getLastMessage();
            }
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(Utils.getFormattedDate(conversation.getLastMessageTime()));

            if (conversation.getUnreadCount() == 0)
                holder.unreadMessageCount.setVisibility(View.GONE);
            else {
                holder.unreadMessageCount.setVisibility(View.VISIBLE);
                holder.unreadMessageCount.setText(String.valueOf(conversation.getUnreadCount()));

                //set user to store data in local
                User user = new User();
                user.setFirstName(conversation.getFirstName());
                user.setLastName(conversation.getLastName());
                user.setUserId(conversation.getUserId());
                user.setProfileImageUrl(conversation.getProfileImageUrl());
                user.setBlockedByMe(conversation.getBlockedByMe());

                listener.onMessagReceived(user);
            }
        }

        holder.lastMessage.setText(message);

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();

                user.setFirstName(conversation.getFirstName());
                user.setLastName(conversation.getLastName());
                user.setUserId(conversation.getUserId());
                user.setProfileImageUrl(conversation.getProfileImageUrl());
                user.setBlockedByMe(conversation.getBlockedByMe());

                boolean loading=false;
                if(conversation.getUnreadCount()!= null && conversation.getUnreadCount()>0){
                    loading = true;
                }

                listener.onChatItem(user, position, loading);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (conversations ==null)
            return 0;
        else
            return conversations.size();
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        View online, offline;
        TextView name, time, lastMessage, unreadMessageCount;
        CardView chat;

        DataObjectHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            lastMessage = itemView.findViewById(R.id.last_message);
            unreadMessageCount = itemView.findViewById(R.id.unread_message_count);
            chat = itemView.findViewById(R.id.chat);
        }
    }

    public interface OnChatItemClickListener {
        void onChatItem(User user, int position, boolean loading);
        void onMessagReceived(User user);
    }

    public void notifydata(ArrayList<Conversation> conversations){
        this.conversations = conversations;
        notifyDataSetChanged();
    }
}
