package com.app.hmp.cognitive.afrocamgistchat;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.multidex.MultiDex;
import com.app.hmp.cognitive.afrocamgistchat.activity.Connectivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

public class AfrocamgistApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static AfrocamgistApplication instance;
    public static boolean onAppForegrounded = false;
    private static Handler handler = new Handler();
    private static Context mContext;

    //Precache videos
    public static SimpleCache simpleCache = null;
    LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = null;
    ExoDatabaseProvider exoDatabaseProvider = null;
    long exoPlayerCacheSize = 90 * 1024 * 1024;

    public static Context getContext() {
        return mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        //Fabric.with(this, new Crashlytics());
        instance = this;
        MultiDex.install(this);
        Connectivity.init(this);
        Fresco.initialize(this);

        //Precahce video
        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(context().getExternalCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Connectivity.getInstance().removeAllInternetConnectivityChangeListeners();
    }

    public static AfrocamgistApplication getInstance() {
        synchronized (AfrocamgistApplication.class) {
            onAppForegrounded = true;
            return instance;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        onAppForegrounded = true;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        onAppForegrounded = true;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        onAppForegrounded = true;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        onAppForegrounded = true;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        onAppForegrounded = false;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        onAppForegrounded = false;
    }

    @Override
    public void onTrimMemory(int level) {
        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            onAppForegrounded = false;
        }
        super.onTrimMemory(level);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        onAppForegrounded = true;
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        onAppForegrounded = false;
    }
    public static Context context() {
        return instance.getApplicationContext();
    }

}
