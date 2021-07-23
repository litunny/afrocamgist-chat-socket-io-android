package com.app.hmp.cognitive.afrocamgistchat.activity;

public class UrlEndpoints {
    //Live URLs
    public static String BASE_URL = "https://manager.afrocamgist.com/api/";
    public static String MEDIA_BASE_URL = "https://cdn.afrocamgist.com/";

    //Staging URLs
    //public static String BASE_URL = "https://manager.staging.afrocamgist.com/api/";
    //public static String MEDIA_BASE_URL = "https://cdn.staging.afrocamgist.com/";

    // New Production server Url
    //public static String BASE_URL = "http://143.110.163.183:3000/api/";
    //public static String MEDIA_BASE_URL = "https://cdntest.afrocamgist.com/";


    //public static String BASE_URL = "http://143.110.163.183:3000/api/";
    //public static String MEDIA_BASE_URL = "https://cdn.afrocamgist.com/";

    public static String BASE_SHARE_POST_URL = "https://afrocamgist.com/post/";
    public static String SIGN_UP = "users/register";
    public static String SIGN_UP_APP = "users/register-app";
    public static String VERIFY_EMAIL = "users/verify-email-otp";
    public static String VERIFY_PHONE = "users/verify-phone";
    public static String RESEND_OTP = "users/resendOTP";
    public static String CHECK_USERNAME = "users/check-username";
    public static String CHAT_REPORT = "users/";
    public static String LOGIN = "users/login";
    public static String LOGOUT = "users/logout";
    public static String DEACTIVATE_ACCOUNT = "users/inactivemyaccount";
    public static String REQUEST_PASSWORD_RESET = "users/request-password-reset";
    public static String VERIFY_PASSWORD_RESET = "users/verify-password-reset";
    public static String SETTINGS = "profile/settings";
    public static String PROFILE = "profile";
    public static String PHOTOS = "profile/photos";
    public static String UPDATE_PROFILE_PICTURE = "profile/update-profile-picture";
    public static String UPDATE_PROFILE_COVER = "profile/update-profile-cover";
    public static String AFROSWAGGER_POST = "posts/afroswagger";
    public static String AFROTALENT_POST = "posts/afrotalent";
    public static String FOLLOWING = "profile/followings";
    public static String FOLLOWERS = "profile/followers";
    public static String ROLE_MODELS = "profile/friends";
    public static String FOLLOW_REQUESTS = "profile/follow-requests";
    public static String PHOTO_COLLECTION = "profile/photos";
    public static String PHOTO_DEL = "user-photos/";
    public static String FIND_USERS = "profile/find-friends?search=";
    public static String MOST_POPULAR = "posts/most-popular";
    public static String SUGGESTED_PEOPLE = "profile/suggested-people";
    public static String MY_GROUPS = "groups/my-groups";
    public static String GROUP_MEMBER_LIST = "groups/";
    public static String JOINED_GROUPS = "groups/my-joined-groups";
    public static String INVITED_GROUPS = "groups/my-invited-groups";
    public static String GROUP = "groups";
    public static String UPDATE_GROUP_IMAGE = "update-group-picture";
    public static String UPDATE_GROUP_COVER = "update-group-cover";
    public static String NOTIFICATIONS = "notifications/v2";
    public static String NOTIFICATION_COUNTER = "notifications/counter";
    public static String MARK_ALL_NOTIFICATIONS_READ = "notifications/markallasread";
    public static String HASHTAGS = "hashtags";
    public static String HASHTAG = "posts/hashtag";
    public static String VIDEO_COUNT = "posts/";
    public static String ENTERTAINMENT = "posts/entertainment";
    public static String FOLLOW_HASHTAG = "hashtags/follow";
    public static String UNFOLLOW_HASHTAG = "hashtags/unfollow";
    public static String FOLLOWING_HASHTAGS = "hashtags/my-hashtags";
    public static String TRENDING_HASHTAGS = "hashtags/trending";
    public static String FIND_GROUPS = "groups/find?search=";
    public static String JOIN_GROUP = "groups/join/";
    public static String LEAVE_GROUP = "groups/leave/";
    public static String ACCEPT_GROUP = "groups/accept/";
    public static String DELETE_GROUP = "groups/";
    public static String GROUP_POSTS = "groups/posts/";
    public static String LIKE_UNLIKE = "likes";
    public static String STORY_LIKE_UNLIKE = "likes/story";
    public static String MY_MEMBERSHIP_GROUPS = "groups/my-membership-groups";
    public static String POSTS = "posts";
    public static String HIDE = "/hide";
    public static String BACKGROUND_IMAGES = "posts/post-backgrounds";
    public static String ALL_HASHTAGS = "hashtags/all";
    public static String COMMENTS = "comments";
    public static String STORY_COMMENTS = "comments/story";
    public static String STORY_VIEW = "storyline/view";
    public static String ANNOUNCEMENT = "announcement";
    public static String UPLOAD_CRASH_FILE = "test2/upload";
    public static String USER_BY_USERNAME = "users/userbyusername";

    public static String MESSAGES = "messages";
    public static String CONVERSATION = "messages/conversation/";
    public static String ADS = "adverts/mobile-ads";
    public static String TNCURL = "https://afrocamgist.com/terms-and-privacy";

    //storyline
    public static String STORYLINE = "storyline";

    //Watermarked video
    public static String WATERMARK_VIDEO = "videoprocess/addwatermark";
    public static String DELETE_WATERMARK_VIDEO = "videoprocess/deleteVideo";

    //Open account watermark video
    public static String OPEN_WATERMARK_VIDEO = "open/addwatermark";
    public static String OPEN_DELETE_WATERMARK_VIDEO = "open/deleteVideo";

    //Non-Registered uer apis
    public static String CREATE_ID = "open/createId";
    public static String OPEN_REGISTER_ACCOUNT = "open/register";
    public static String GET_POPULAR_POST = "open/most-popular";
    public static String LIKE_POST = "open/like";
    public static String COMMENT_ON_POST = "open/comment";
}
