
package com.app.hmp.cognitive.afrocamgistchat.activity;

import java.io.Serializable;
import java.util.ArrayList;

import com.app.hmp.cognitive.afrocamgistchat.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("comment_text")
    @Expose
    private String commentText;
    @SerializedName("comment_likes")
    @Expose
    private int comment_likes;
    @SerializedName("commented_by")
    @Expose
    private User commentedBy;
    @SerializedName("comment_parent_id")
    @Expose
    private Integer commentParentId;
    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    @SerializedName("comment_id")
    @Expose
    private Integer commentId;
    @SerializedName("sub_comments")
    @Expose
    private ArrayList<Comment> subComments = null;
    @SerializedName("my_comment")
    @Expose
    private Boolean myComment;
    @SerializedName("liked")
    @Expose
    private Boolean liked;
    private final static long serialVersionUID = 2088339676297593939L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(User commentedBy) {
        this.commentedBy = commentedBy;
    }

    public Integer getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(Integer commentParentId) {
        this.commentParentId = commentParentId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public ArrayList<Comment> getSubComments() {
        return subComments;
    }

    public void setSubComments(ArrayList<Comment> subComments) {
        this.subComments = subComments;
    }

    public Boolean getMyComment() {
        return myComment;
    }

    public void setMyComment(Boolean myComment) {
        this.myComment = myComment;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public int getComment_likes() {
        return comment_likes;
    }

    public void setComment_likes(int comment_likes) {
        this.comment_likes = comment_likes;
    }
}
