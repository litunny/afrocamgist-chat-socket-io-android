package com.app.hmp.cognitive.afrocamgistchat;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.hmp.cognitive.afrocamgistchat.activity.BaseSocketActivity;
import com.app.hmp.cognitive.afrocamgistchat.service.WebSocketService;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

import static com.app.hmp.cognitive.afrocamgistchat.service.WebSocketService.ACTION_SOCKET_MESSAGE;

public class MainActivity extends BaseSocketActivity {

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(), "onReceived()");
            Toast.makeText(getApplicationContext(), intent.getStringExtra(ACTION_SOCKET_MESSAGE), Toast.LENGTH_LONG).show();
        }
    };

    private Button mButton;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.sendBtn);
        mEditText = findViewById(R.id.editText);

        mButton.setOnClickListener( view -> {

            JSONObject data = new JSONObject();
            try {
                data.put("from_id", 2536);
                data.put("to_id", 2539);
                data.put("message", mEditText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.emit("getsocketid", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(MainActivity.class.getSimpleName(), "getsocketid()");
                }
            });

            mSocket.emit("message", data, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d(MainActivity.class.getSimpleName(), "Ack()");
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(WebSocketService.ACTION_MSG_RECEIVED));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onStop();
    }
}