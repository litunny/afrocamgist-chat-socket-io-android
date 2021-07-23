package com.app.hmp.cognitive.afrocamgistchat.service;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WebSocketService extends Service {

    private Socket mSocket;
    {
        try {
             //mSocket = IO.socket("https://manager.staging.afrocamgist.com");
             mSocket = IO.socket("http://192.168.43.135:3000");
        } catch (URISyntaxException e) {}
    }

    public static final String ACTION_MSG_RECEIVED = "msgReceived";
    public static final String ACTION_NETWORK_STATE_CHANGED = "networkStateChanged";
    public static final String ACTION_SOCKET_MESSAGE = "ActionSocketMessage";
    private static final String TAG = WebSocketService.class.getSimpleName();
    private final IBinder mBinder = new WebSocketsBinder();

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean networkIsOn = intent.getBooleanExtra(ACTION_NETWORK_STATE_CHANGED, false);
            if (networkIsOn) {
                startSocket();
            } else {
                stopSocket();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(WebSocketService.ACTION_NETWORK_STATE_CHANGED));
        startSocket();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        stopSocket();
        return false;
    }

    private void startSocket() {
        mSocket.on("message", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        sendMessageReceivedEvent((String) args[0]);
//                        Log.d("WebSocketService", "Received");
//                        JSONObject data = (JSONObject) args[0];
//                        String from_id, to_id, message_text;
//                        try {
//                            from_id = data.getString("from_id"); to_id = data.getString("to_id"); message_text = data.getString("message");
//                            sendMessageReceivedEvent(message_text);
//                            Toast.makeText(getApplicationContext(), "Response : " +  message_text, Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            Log.d("WebSocketService", e.getMessage());
//                            return;
//                        }
                    }
                });
            }
        });
        mSocket.connect();
    }

    private void stopSocket() {
        mSocket.close();
    }

    public Socket getSocket () {
        return mSocket;
    }

    private void sendMessageReceivedEvent(String message) {
        Intent intent = new Intent(ACTION_MSG_RECEIVED);
        intent.putExtra(ACTION_SOCKET_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public final class WebSocketsBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }
}
