package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import com.app.hmp.cognitive.afrocamgistchat.AfrocamgistApplication;
import com.app.hmp.cognitive.afrocamgistchat.model.Post;
import com.app.hmp.cognitive.afrocamgistchat.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LocalStorage {

    public static void setToken(String token) {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PrefKeys.KEY_TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefKeys.KEY_TOKEN, "");
    }

    public static void setIsUserLoggedIn(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PrefKeys.KEY_USER_LOGIN, isLoggedIn);
        editor.apply();
    }

    public static boolean getIsUserLoggedIn() {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefKeys.KEY_USER_LOGIN, false);
    }

    public static void setIsUserNotRegisterd(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PrefKeys.KEY_USER_NOT_REGISTERED, isLoggedIn);
        editor.apply();
    }

    public static boolean getIsUserNotRegisterd() {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefKeys.KEY_USER_NOT_REGISTERED, false);
    }

    public static void setUserPass(String pass) {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PrefKeys.KEY_USER_PASS, pass);
        editor.apply();
    }

    public static String getUserPass() {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefKeys.KEY_USER_PASS, "");
    }

    public static void setProfileImage(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PrefKeys.KEY_USER_PROFILE, isLoggedIn);
        editor.apply();
    }

    public static boolean getProfileImage() {
        SharedPreferences sharedPreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_LOGIN_PREF,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefKeys.KEY_USER_PROFILE, false);
    }

    public static void saveLoginToken(String value) {
        SharedPreferences sharedpreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_LOGIN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PrefKeys.KEY_LOGIN, value);
        editor.apply();
    }

    public static String getLoginToken() {
        SharedPreferences sharedpreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_LOGIN_PREF, Context.MODE_PRIVATE);
        //return sharedpreferences.getString(PrefKeys.KEY_LOGIN, "");
        return "b5c6e6deb588bf850add8f29b1a4bd1ad801be03faafdebd7ce74f7bab312a3e";
    }

    public static void saveIntroduce(String value) {
        SharedPreferences sharedpreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_LOGIN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PrefKeys.KEY_INTRODUCE, value);
        editor.apply();
    }

    public static String getIntroduce() {
        SharedPreferences sharedpreferences = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_LOGIN_PREF, Context.MODE_PRIVATE);
        return sharedpreferences.getString(PrefKeys.KEY_INTRODUCE, "");
    }

    public static void saveUserDetails(User user) {
        SharedPreferences prefs = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(PrefKeys.KEY_USER, json);
        editor.apply();
    }

    public static User getUserDetails() {
        SharedPreferences prefs = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_USER_PREF,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(PrefKeys.KEY_USER, null);
        Type type = new TypeToken<User>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void savePopularPostsImage(ArrayList<String> images) {

        SharedPreferences prefs = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_POPULAR_POST_IMAGES_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        Gson gson = new Gson();
        String json = gson.toJson(images);
        editor.putString(PrefKeys.KEY_POPULAR_POST_IMAGES, json);
        editor.apply();
    }

    public static ArrayList<String> getPopularPostsImage() {
        SharedPreferences prefs = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_POPULAR_POST_IMAGES_PREF,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(PrefKeys.KEY_POPULAR_POST_IMAGES, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void savePopularPosts(ArrayList<Post> images) {

        SharedPreferences prefs = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_POPULAR_POST_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        Gson gson = new Gson();
        String json = gson.toJson(images);
        editor.putString(PrefKeys.KEY_POPULAR_POST, json);
        editor.apply();
    }

    public static ArrayList<Post> getPopularPosts() {
        SharedPreferences prefs = AfrocamgistApplication.getInstance().getSharedPreferences(PrefKeys.KEY_POPULAR_POST_PREF,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(PrefKeys.KEY_POPULAR_POST, null);
        Type type = new TypeToken<ArrayList<Post>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
