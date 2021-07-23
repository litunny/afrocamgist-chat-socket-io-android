package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

class NetworkChangeReceiver extends BroadcastReceiver {

    private WeakReference<NetworkChangeListener> mNetworkChangeListenerWeakReference;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkChangeListener networkChangeListener = mNetworkChangeListenerWeakReference.get();
        if (networkChangeListener != null) {
            networkChangeListener.onNetworkChange(isNetworkConnected(context));
        }
    }

    void setNetworkChangeListener(NetworkChangeListener networkChangeListener) {
        mNetworkChangeListenerWeakReference = new WeakReference<>(networkChangeListener);
    }

    void removeNetworkChangeListener() {
        if (mNetworkChangeListenerWeakReference != null) {
            mNetworkChangeListenerWeakReference.clear();
        }
    }

    boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isAvailable() && netInfo.isConnected();

    }

    interface NetworkChangeListener {
        void onNetworkChange(boolean isNetworkAvailable);
    }
}
