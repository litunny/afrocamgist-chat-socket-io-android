package com.app.hmp.cognitive.afrocamgistchat.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hmp.cognitive.afrocamgistchat.activity.DoubleClickListener;
import com.app.hmp.cognitive.afrocamgistchat.activity.LocalStorage;
import com.app.hmp.cognitive.afrocamgistchat.activity.Message;
import com.app.hmp.cognitive.afrocamgistchat.activity.UrlEndpoints;
import com.app.hmp.cognitive.afrocamgistchat.activity.Utils;
import com.app.hmp.cognitive.afrocamgistchat.fragment.BottomPopup;
import com.app.hmp.cognitive.afrocamgistchat.R;
import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.meg7.widget.SvgImageView;
import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.DataObjectHolder> {

    private ArrayList<Message> messages;
    private static final int SEND = 0, RECEIVE = 1;
    private Context context;
    private OnMessageClickListener listener;
    private String otherUserProfile;

    public ChatMessageAdapter(ArrayList<Message> messages, Context context, OnMessageClickListener listener, String profileImageUrl) {
        this.messages = messages;
        this.context = context;
        this.listener = listener;
        this.otherUserProfile = profileImageUrl;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;

        switch (viewType) {

            case SEND:
                view = inflater.inflate(R.layout.item_message_send,viewGroup, false);
                break;
            case RECEIVE:
                view = inflater.inflate(R.layout.item_message_receive,viewGroup, false);
                break;
            default:
                view = inflater.inflate(R.layout.item_message_send,viewGroup, false);
                break;
        }

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {
        Message message = messages.get(position);

        Log.d("Message11",new GsonBuilder().setPrettyPrinting().create().toJson(message));

        if ("".equals(message.getMessageImage())) {
            holder.imageMessage.setVisibility(View.GONE);
        } else {
            holder.imageMessage.setVisibility(View.VISIBLE);
            Glide.with(holder.imageMessage)
                    .load(UrlEndpoints.MEDIA_BASE_URL + message.getMessageImage())
                    .into(holder.imageMessage);
        }

        if ("".equals(message.getMessageText())) {
            holder.textMessage.setVisibility(View.GONE);
        } else {
            holder.textMessage.setVisibility(View.VISIBLE);
            holder.textMessage.setText(message.getMessageText());
        }

        if("read".equalsIgnoreCase(message.getMessageStatus())){
            holder.tickMark.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else {
            holder.tickMark.setTextColor(context.getResources().getColor(R.color.darkGrey1));
        }

//        holder.time.setText(Utils.getFormattedTimeForChatMessage(message.getCreatedDate()));

        if(message.isLiked()){
            holder.my_like.setVisibility(View.VISIBLE);
        }else {
            holder.my_like.setVisibility(View.GONE);
        }

        if(message.getMessage_reply_id()!=null){
            holder.llReplyMessage.setVisibility(View.VISIBLE);
            holder.textMessage.setText(message.getMessage_reply_text());
            holder.reply_message.setText(message.getMessageText());
        }else {
            holder.llReplyMessage.setVisibility(View.GONE);
        }


        if(message.getLike_count()!=null){
            if(message.isLiked() && message.getLike_count() == 2){
                holder.llLikeMessage.setVisibility(View.VISIBLE);
                if(message.getLikedBy()!=null && !message.getLikedBy().isEmpty()){
                    holder.my_like.setVisibility(View.VISIBLE);
                    holder.other_user_like.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(UrlEndpoints.MEDIA_BASE_URL + LocalStorage.getUserDetails().getProfileImageUrl())
                            .into(holder.my_like);
                    Glide.with(context)
                            .load(UrlEndpoints.MEDIA_BASE_URL + otherUserProfile)
                            .into(holder.other_user_like);
                }
            }else if(message.isLiked() && message.getLike_count() == 1){
                holder.llLikeMessage.setVisibility(View.VISIBLE);
                holder.my_like.setVisibility(View.VISIBLE);
                holder.other_user_like.setVisibility(View.GONE);
                Glide.with(context)
                        .load(UrlEndpoints.MEDIA_BASE_URL + LocalStorage.getUserDetails().getProfileImageUrl())
                        .into(holder.my_like);
            }else if(!message.isLiked() && message.getLike_count() == 1){
                holder.llLikeMessage.setVisibility(View.VISIBLE);
                holder.other_user_like.setVisibility(View.VISIBLE);
                holder.my_like.setVisibility(View.GONE);
                Glide.with(context)
                        .load(UrlEndpoints.MEDIA_BASE_URL + otherUserProfile)
                        .into(holder.other_user_like);
            }else {
                holder.llLikeMessage.setVisibility(View.GONE);
            }
        }


        holder.cardMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(message.getFrom().equalsIgnoreCase("me")){
                    BottomPopup bottomPopup = new BottomPopup();
                    BottomPopup.newInstance(message).show(((AppCompatActivity) context).getSupportFragmentManager(), bottomPopup.getTag());
                }else {
                    if(listener!=null){
                        listener.onReceiveMessageReply(message.getMessageId(),message.getMessageText(),message.getFromId());
                    }
                }
                return true;
            }
        });

        holder.cardMessage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                if(listener!=null){
                    if(!message.isLiked() && message.getLike_count() == 1){
                        messages.get(holder.getAdapterPosition()).setLiked(true);
                        messages.get(holder.getAdapterPosition()).setLike_count(messages.get(holder.getAdapterPosition()).getLike_count()+1);

                        holder.llLikeMessage.setVisibility(View.VISIBLE);
                        holder.my_like.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(UrlEndpoints.MEDIA_BASE_URL + LocalStorage.getUserDetails().getProfileImageUrl())
                                .into(holder.my_like);
                        listener.onMessageDoubleClick(message.getMessageId());
                    }else if(!message.isLiked() && message.getLike_count() == 0){
                        messages.get(holder.getAdapterPosition()).setLiked(true);
                        messages.get(holder.getAdapterPosition()).setLike_count(messages.get(holder.getAdapterPosition()).getLike_count()+1);

                        holder.llLikeMessage.setVisibility(View.VISIBLE);
                        holder.other_user_like.setVisibility(View.GONE);
                        holder.my_like.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(UrlEndpoints.MEDIA_BASE_URL + LocalStorage.getUserDetails().getProfileImageUrl())
                                .into(holder.my_like);
                        listener.onMessageDoubleClick(message.getMessageId());
                    }

                }
            }

            @Override
            public void onSingleClick() {
                if(listener!=null){
                    if(message.isLiked() && message.getLike_count() == 2){
                        messages.get(holder.getAdapterPosition()).setLiked(false);
                        messages.get(holder.getAdapterPosition()).setLike_count(messages.get(holder.getAdapterPosition()).getLike_count()-1);

                        holder.my_like.setVisibility(View.GONE);
                        holder.other_user_like.setVisibility(View.VISIBLE);
                        listener.onMessageSingleClick(message.getMessageId());
                    }else if(message.isLiked() && message.getLike_count() == 1){
                        messages.get(holder.getAdapterPosition()).setLiked(false);
                        messages.get(holder.getAdapterPosition()).setLike_count(messages.get(holder.getAdapterPosition()).getLike_count()-1);

                        holder.my_like.setVisibility(View.GONE);
                        holder.other_user_like.setVisibility(View.GONE);
                        holder.llLikeMessage.setVisibility(View.GONE);
                        listener.onMessageSingleClick(message.getMessageId());
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        else
            return messages.size();
    }

    public void fetchNewMessages(ArrayList<Message> messages) {
        this.messages = new ArrayList<>();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
//
//        if(LocalStorage.getUserDetails().getUserId().equals(messages.get(position).getFromId()))
//            return SEND;
//        else
//            return RECEIVE;
        return RECEIVE;
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {

        ImageView imageMessage;
        TextView textMessage, time, tickMark, reply_message;
        CardView cardMessage;
        LinearLayout llReplyMessage,llLikeMessage;
        //CircleImageView my_like,other_user_like;
        SvgImageView my_like,other_user_like;

        DataObjectHolder(View itemView) {
            super(itemView);
            imageMessage = itemView.findViewById(R.id.image_message);
            textMessage = itemView.findViewById(R.id.text_message);
            time = itemView.findViewById(R.id.time);
            tickMark = itemView.findViewById(R.id.tickMark);
            cardMessage = itemView.findViewById(R.id.cardMessage);
            other_user_like = itemView.findViewById(R.id.other_user_like);
            my_like = itemView.findViewById(R.id.my_like);
            llLikeMessage = itemView.findViewById(R.id.llLikeMessage);
            llReplyMessage = itemView.findViewById(R.id.ll_reply_message);
            reply_message = itemView.findViewById(R.id.reply_message);

        }
    }

    public interface OnMessageClickListener{
        void onMessageSingleClick(int messageId);
        void onMessageDoubleClick(int messageId);
        void onReceiveMessageReply(Integer messageId, String messageText, Integer fromId);
    }

}
