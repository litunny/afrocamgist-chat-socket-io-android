
package com.app.hmp.cognitive.afrocamgistchat.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conversation implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("last_active")
    @Expose
    private String lastActive;
    @SerializedName("blocked_by_me")
    @Expose
    private Boolean blockedByMe;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("last_message")
    @Expose
    private String lastMessage;
    @SerializedName("last_message_image")
    @Expose
    private String lastMessageImage;
    @SerializedName("last_message_time")
    @Expose
    private String lastMessageTime;
    @SerializedName("unread_count")
    @Expose
    private Integer unreadCount;
    @SerializedName("online_status")
    @Expose
    private Boolean onlineStatus;
    private final static long serialVersionUID = -3760214323076190089L;

    public Boolean getBlockedByMe() {
        return blockedByMe;
    }

    public void setBlockedByMe(Boolean blockedByMe) {
        this.blockedByMe = blockedByMe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageImage() {
        return lastMessageImage;
    }

    public void setLastMessageImage(String lastMessageImage) {
        this.lastMessageImage = lastMessageImage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

}
