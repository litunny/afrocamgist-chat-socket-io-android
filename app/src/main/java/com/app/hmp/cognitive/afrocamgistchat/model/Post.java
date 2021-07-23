
package com.app.hmp.cognitive.afrocamgistchat.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.app.hmp.cognitive.afrocamgistchat.activity.Comment;
import com.app.hmp.cognitive.afrocamgistchat.activity.RequestButton;
import com.app.hmp.cognitive.afrocamgistchat.activity.VideoDimension;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("post_text")
    @Expose
    private String postText;
    @SerializedName("post_lat_long")
    @Expose
    private String postLatLng;
    @SerializedName("post_location")
    @Expose
    private String postLocation;
    @SerializedName("post_video")
    @Expose
    private String postVideo;

    @SerializedName("private")
    @Expose
    private Boolean privateAccount;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("request_buttons")
    @Expose
    private ArrayList<RequestButton> requestButtons = null;
    @SerializedName("tagged_id")
    @Expose
    private ArrayList<String> tagged_id = new ArrayList<>();
    @SerializedName("post_image")
    @Expose
    private ArrayList<String> postImage = new ArrayList<>();
    @SerializedName("bg_image")
    @Expose
    private String backgroundImage;
    @SerializedName("bg_image_post")
    @Expose
    private Boolean isBackgroundImagePost;
    @SerializedName("bg_map_post")
    @Expose
    private Boolean isMapPost;
    @SerializedName("posted_by")
    @Expose
    private Integer postedBy;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("posted_for")
    @Expose
    private String postedFor;

    @SerializedName("image_size")
    @Expose
    private String image_size;
    @SerializedName("font_face")
    @Expose
    private String font_face;


    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("post_privacy")
    @Expose
    private String postPrivacy;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> comments = null;
    @SerializedName("liked_by")
    @Expose
    private ArrayList<User> likedBy = null;

    @SerializedName("dimension")
    @Expose
    private ArrayList<VideoDimension> videoDimensions = null;

    @SerializedName("shared_post_data")
    @Expose
    private Post sharedPost = null;
    @SerializedName("liked")
    @Expose
    private Boolean liked;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("video_play_count")
    @Expose
    private Integer video_play_count;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("following")
    @Expose
    private Boolean following;
    private final static long serialVersionUID = -4076511325903262066L;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_size() {
        return image_size;
    }

    public void setImage_size(String image_size) {
        this.image_size = image_size;
    }

    public String getFont_face() {
        return font_face;
    }

    public void setFont_face(String font_face) {
        this.font_face = font_face;
    }

    public ArrayList<RequestButton> getRequestButtons() {
        return requestButtons;
    }

    public void setRequestButtons(ArrayList<RequestButton> requestButtons) {
        this.requestButtons = requestButtons;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostLatLng() {
        return postLatLng;
    }

    public void setPostLatLng(String postLatLng) {
        this.postLatLng = postLatLng;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public Boolean getPrivateAccount() {
        return privateAccount;
    }

    public void setPrivateAccount(Boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }

    public Integer getVideo_play_count() {
        return video_play_count;
    }

    public void setVideo_play_count(Integer video_play_count) {
        this.video_play_count = video_play_count;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<String> getTagged_id() {
        return tagged_id;
    }

    public void setTagged_id(ArrayList<String> tagged_id) {
        this.tagged_id = tagged_id;
    }

    public ArrayList<String> getPostImage() {
        return postImage;
    }

    public void setPostImage(ArrayList<String> postImage) {
        this.postImage = postImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Boolean getBackgroundImagePost() {
        return isBackgroundImagePost;
    }

    public void setBackgroundImagePost(Boolean backgroundImagePost) {
        isBackgroundImagePost = backgroundImagePost;
    }

    public Boolean getMapPost() {
        return isMapPost;
    }

    public void setMapPost(Boolean mapPost) {
        isMapPost = mapPost;
    }

    public Integer getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostedFor() {
        return postedFor;
    }

    public void setPostedFor(String postedFor) {
        this.postedFor = postedFor;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostPrivacy() {
        return postPrivacy;
    }

    public void setPostPrivacy(String postPrivacy) {
        this.postPrivacy = postPrivacy;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<User> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<User> likedBy) {
        this.likedBy = likedBy;
    }

    public Post getSharedPost() {
        return sharedPost;
    }

    public void setSharedPost(Post sharedPost) {
        this.sharedPost = sharedPost;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public ArrayList<VideoDimension> getVideoDimensions() {
        return videoDimensions;
    }

    public void setVideoDimensions(ArrayList<VideoDimension> videoDimensions) {
        this.videoDimensions = videoDimensions;
    }
}
