package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.app.hmp.cognitive.afrocamgistchat.service.WebSocketService;
import io.socket.client.Socket;

public abstract class BaseSocketActivity extends AppCompatActivity {

    private static final String TAG = BaseSocketActivity.class.getSimpleName();

    protected WebSocketService mService;
    public Socket mSocket;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            WebSocketService.WebSocketsBinder binder = (WebSocketService.WebSocketsBinder) service;
            mService = binder.getService();
            mSocket = mService.getSocket();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            mService = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, WebSocketService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(mConnection);
        super.onStop();
    }
}