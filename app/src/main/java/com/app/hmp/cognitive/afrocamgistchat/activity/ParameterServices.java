package com.app.hmp.cognitive.afrocamgistchat.activity;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ParameterServices {

    @GET("messages/conversation/{user_id}")
    Call<MessageDetails> getAllMessages(@Path("user_id") int id);

    @GET("messages")
    Call<ConversationDetails> getMessagesList();

//    @GET("notifications/counter")
//    Call<NotificationCounter> getNotificationCounter();
//
//    @GET("users/userbyusername")
//    Call<JsonObject> getUserIdByUsername(@Query("user_name") String user_name);
//
//    //Single Post
//    @GET("posts/{postId}")
//    Call<PostBaseResponse> getPostDetail(@Path("postId") int postId);

    //Video View Count
    @PUT("posts/{postId}/videoplaycount")
    Call<JsonObject> getVideoCount(@Path("postId") int postId);

    //Open account Video View Count
    @PUT("open/{postId}/videoplaycount")
    Call<JsonObject> getOpenVideoCount(@Path("postId") int postId);

    //Post View Count
    @PUT("posts/{postId}/postviewcount")
    Call<JsonObject> getPostViewCount(@Path("postId") int postId);

    //Post View
    @PUT("posts/{postId}/afroswagger")
    Call<JsonObject> getPostView(@Path("postId") int postId);

    //Open account Post View
    @PUT("open/{postId}/postviewcount")
    Call<JsonObject> getOpenPostViewCount(@Path("postId") int postId);

    //Most Popular View Count
    @PUT("posts/{postId}/most-popular-seen")
    Call<JsonObject> getPopularPostViewCount(@Path("postId") int postId);

//    // Get all user for tagging
//    @GET("user-tagged")
//    Call<TagUserModel> getAllUsers();

    @POST("storyline")
    Call<JsonObject> getStorylineUsers(@Body JsonObject requestObject);

//    // Notifications list
//    @GET("notifications/v2")
//    Call<NotificationDetails> getAllNotifications();

    // DELETE open account comment

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "open/comment/{comment_id}", hasBody = true)
    Call<JsonObject> deleteComment(@Path("comment_id") int commentId, @Field("user_id") int userId);

    /*@DELETE("open/comment/{comment_id}")
    Call<JsonObject> deleteComment(@Path("comment_id") int commentId,@Query("user_id") int userId);*/

}
