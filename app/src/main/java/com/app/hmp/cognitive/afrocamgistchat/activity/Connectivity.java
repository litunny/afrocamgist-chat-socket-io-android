package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.content.Context;
import android.content.IntentFilter;

import com.app.hmp.cognitive.afrocamgistchat.connection.ConnectivityListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Connectivity implements NetworkChangeReceiver.NetworkChangeListener {

    private static final Object LOCK = new Object();
    private static volatile Connectivity sInstance;

    private WeakReference<Context> mContextWeakReference;
    private List<WeakReference<ConnectivityListener>> mInternetConnectivityListenersWeakReferences;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private boolean mIsNetworkChangeRegistered = false;
    private boolean mIsInternetConnected = false;

    private TaskFinished<Boolean> mCheckConnectivityCallback;

    private static final String CONNECTIVITY_CHANGE_INTENT_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private Connectivity(Context context) {
        Context appContext = context.getApplicationContext();
        mContextWeakReference = new WeakReference<>(appContext);
        mInternetConnectivityListenersWeakReferences = new ArrayList<>();
    }

    public static Connectivity init(Context context) {
        if (context == null) {
            throw new NullPointerException("context can not be null");
        }

        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new Connectivity(context);
                }
            }
        }
        return sInstance;
    }

    public static Connectivity getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("call init(Context) in your application class before calling getInstance()");
        }
        return sInstance;
    }

    public void addInternetConnectivityListener(ConnectivityListener connectivityListener) {
        if (connectivityListener == null) {
            return;
        }
        mInternetConnectivityListenersWeakReferences.add(new WeakReference<>(connectivityListener));
        if (mInternetConnectivityListenersWeakReferences.size() == 1) {
            registerNetworkChangeReceiver();
            return;
        }
        publishInternetAvailabilityStatus(mIsInternetConnected);
    }

    public void removeInternetConnectivityChangeListener(ConnectivityListener connectivityListener) {
        if (connectivityListener == null) {
            return;
        }

        if (mInternetConnectivityListenersWeakReferences == null) {
            return;
        }

        Iterator<WeakReference<ConnectivityListener>> iterator = mInternetConnectivityListenersWeakReferences.iterator();
        while (iterator.hasNext()) {

            //if weak reference is null then remove it from iterator
            WeakReference<ConnectivityListener> reference = iterator.next();
            if (reference == null) {
                iterator.remove();
                continue;
            }

            //if listener referenced by this weak reference is garbage collected then remove it from iterator
            ConnectivityListener listener = reference.get();
            if (listener == null) {
                reference.clear();
                iterator.remove();
                continue;
            }

            //if listener to be removed is found then remove it
            if (listener == connectivityListener) {
                reference.clear();
                iterator.remove();
                break;
            }
        }

        //if all listeners are removed then unregister NetworkChangeReceiver
        if (mInternetConnectivityListenersWeakReferences.size() == 0) {
            unregisterNetworkChangeReceiver();
        }
    }

    public void removeAllInternetConnectivityChangeListeners() {
        if (mInternetConnectivityListenersWeakReferences == null) {
            return;
        }

        Iterator<WeakReference<ConnectivityListener>> iterator = mInternetConnectivityListenersWeakReferences.iterator();
        while (iterator.hasNext()) {
            WeakReference<ConnectivityListener> reference = iterator.next();
            if (reference != null) {
                reference.clear();
            }
            iterator.remove();
        }
        unregisterNetworkChangeReceiver();
    }

    private void registerNetworkChangeReceiver() {
        Context context = mContextWeakReference.get();
        if (context != null && !mIsNetworkChangeRegistered) {
            mNetworkChangeReceiver = new NetworkChangeReceiver();
            mNetworkChangeReceiver.setNetworkChangeListener(this);
            context.registerReceiver(mNetworkChangeReceiver, new IntentFilter(CONNECTIVITY_CHANGE_INTENT_ACTION));
            mIsNetworkChangeRegistered = true;
        }
    }

    private void unregisterNetworkChangeReceiver() {
        Context context = mContextWeakReference.get();
        if (context != null && mNetworkChangeReceiver != null && mIsNetworkChangeRegistered) {
            try {
                context.unregisterReceiver(mNetworkChangeReceiver);
            } catch (IllegalArgumentException exception) {
                //consume this exception
            }
            mNetworkChangeReceiver.removeNetworkChangeListener();
        }
        mNetworkChangeReceiver = null;
        mIsNetworkChangeRegistered = false;
        mCheckConnectivityCallback = null;
    }

    @Override
    public void onNetworkChange(boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            mCheckConnectivityCallback = new TaskFinished<Boolean>() {
                @Override
                public void onTaskFinished(Boolean isInternetAvailable) {
                    mCheckConnectivityCallback = null;
                    publishInternetAvailabilityStatus(isInternetAvailable);
                }
            };
            new CheckInternetTask(mCheckConnectivityCallback).execute();
        } else {
            publishInternetAvailabilityStatus(false);
        }
    }

    private void publishInternetAvailabilityStatus(boolean isInternetAvailable) {
        mIsInternetConnected = isInternetAvailable;
        if (mInternetConnectivityListenersWeakReferences == null) {
            return;
        }

        Iterator<WeakReference<ConnectivityListener>> iterator = mInternetConnectivityListenersWeakReferences.iterator();
        while (iterator.hasNext()) {
            WeakReference<ConnectivityListener> reference = iterator.next();

            if (reference == null) {
                iterator.remove();
                continue;
            }

            ConnectivityListener listener = reference.get();
            if (listener == null) {
                iterator.remove();
                continue;
            }

            listener.onInternetConnectivityChanged(isInternetAvailable);
        }

        if (mInternetConnectivityListenersWeakReferences.size() == 0) {
            unregisterNetworkChangeReceiver();
        }
    }
}
