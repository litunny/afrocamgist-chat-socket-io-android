
package com.app.hmp.cognitive.afrocamgistchat.activity;

import java.io.Serializable;
import java.util.ArrayList;
import com.app.hmp.cognitive.afrocamgistchat.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable
{

    public Message (String messageText) {
        this.messageText = messageText;
        this.fromId = 328903;
    }

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("liked")
    @Expose
    private boolean liked;
    @SerializedName("like_count")
    @Expose
    private Integer like_count;

    @SerializedName("message_reply_id")
    @Expose
    private Integer message_reply_id;
    @SerializedName("message_reply_text")
    @Expose
    private String message_reply_text;

    @SerializedName("liked_by")
    @Expose
    private ArrayList<LikedBy> likedBy = null;

    @SerializedName("message_text")
    @Expose
    private String messageText;
    @SerializedName("message_image")
    @Expose
    private String messageImage;
    @SerializedName("from_id")
    @Expose
    private Integer fromId;
    @SerializedName("to_id")
    @Expose
    private Integer toId;
    @SerializedName("message_status")
    @Expose
    private String messageStatus;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("message_id")
    @Expose
    private Integer messageId;
    @SerializedName("from_user")
    @Expose
    private User fromUser;
    @SerializedName("from")
    @Expose
    private String from;
    private final static long serialVersionUID = -8225538798350166806L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }

    public Integer getMessage_reply_id() {
        return message_reply_id;
    }

    public void setMessage_reply_id(Integer message_reply_id) {
        this.message_reply_id = message_reply_id;
    }

    public String getMessage_reply_text() {
        return message_reply_text;
    }

    public void setMessage_reply_text(String message_reply_text) {
        this.message_reply_text = message_reply_text;
    }

    public ArrayList<LikedBy> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<LikedBy> likedBy) {
        this.likedBy = likedBy;
    }

    private static class LikedBy{
        @SerializedName("user_id")
        @Expose
        private Integer user_id;
        @SerializedName("first_name")
        @Expose
        private String first_name;
        @SerializedName("last_name")
        @Expose
        private String last_name;
        @SerializedName("profile_image_url")
        @Expose
        private String profile_image_url;
        @SerializedName("user_name")
        @Expose
        private String user_name;

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }

}
