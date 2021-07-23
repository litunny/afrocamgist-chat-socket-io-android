package com.app.hmp.cognitive.afrocamgistchat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.hmp.cognitive.afrocamgistchat.ChatActivity;
import com.app.hmp.cognitive.afrocamgistchat.R;
import com.app.hmp.cognitive.afrocamgistchat.activity.BaseServices;
import com.app.hmp.cognitive.afrocamgistchat.activity.ConversationDetails;
import com.app.hmp.cognitive.afrocamgistchat.activity.LocalStorage;
import com.app.hmp.cognitive.afrocamgistchat.activity.ParameterServices;
import com.app.hmp.cognitive.afrocamgistchat.activity.UrlEndpoints;
import com.app.hmp.cognitive.afrocamgistchat.activity.Utils;
import com.app.hmp.cognitive.afrocamgistchat.adapters.AfroChatAdapter;
import com.app.hmp.cognitive.afrocamgistchat.api.Webservices;
import com.app.hmp.cognitive.afrocamgistchat.connection.ConnectivityListener;
import com.app.hmp.cognitive.afrocamgistchat.constants.Constants;
import com.app.hmp.cognitive.afrocamgistchat.model.Conversation;
import com.app.hmp.cognitive.afrocamgistchat.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AfroChatFragment extends Fragment implements AfroChatAdapter.OnChatItemClickListener, ConnectivityListener {

    private KProgressHUD hud;
    private RecyclerView conversationList;
    private SwipeRefreshLayout swipeContainer;
    private Handler handler;
    private AfroChatAdapter afroChatAdapter;
    protected LocalBroadcastManager localBroadcastManager;
    private User user;
    private ArrayList<Conversation> conversations = new ArrayList<>();

    public static String NOTIFY_TAB_COUNTER="notify_tab_counter";

    public AfroChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_afro_chat, container, false);

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(mNotifyCounter,
                new IntentFilter(ChatActivity.NOTIFY_COUNTER));

        initView(view);
        initSwipeRefresh();

        if (Utils.isConnected())
            getConversationListNew();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isConnected()) {
            //getConversationListNew();
            setNotificationCounter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {

        swipeContainer = view.findViewById(R.id.swipeContainer);
        conversationList = view.findViewById(R.id.conversation_list);
    }

    private BroadcastReceiver mNotifyCounter = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getConversationListNew();
        }
    };

    private void initSwipeRefresh() {

        swipeContainer.setOnRefreshListener(() -> {
            if (Utils.isConnected())
                getConversationList();
            else {
                swipeContainer.setRefreshing(false);
                Utils.showAlert(getActivity(), "Please check your internet connection");
            }
        });
    }

    private void getConversationList() {

        /*hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setCancellable(true)
                .show();*/

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        Webservices.getData(Webservices.Method.GET, new ArrayList<>(), headers, UrlEndpoints.MESSAGES, response -> {
            swipeContainer.setRefreshing(false);
//            hud.dismiss();
            if ("".equals(response)) {
                Utils.showAlert(getActivity(), "Oops something went wrong....");
            } else {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has(Constants.MESSAGE)) {
                        Utils.showAlert(getActivity(), object.getString(Constants.MESSAGE));
                    } else {
                        ConversationDetails conversationDetails = new Gson().fromJson(response, ConversationDetails.class);
                        if (conversationDetails.getConversationList().size() > 0) {
                            conversations = new ArrayList<>();
                            conversations.addAll(conversationDetails.getConversationList());
                            setConversationList();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setConversationList() {
        if(afroChatAdapter==null){
            conversationList.setLayoutManager(new LinearLayoutManager(getActivity()));
            afroChatAdapter = new AfroChatAdapter(conversations, this);
            conversationList.setAdapter(afroChatAdapter);
        }else{
            afroChatAdapter.notifydata(conversations);
        }
    }

    private void setNotificationCounter() {
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("ChatConversation", "Called");
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (afroChatAdapter!=null) {
                            getConversationListNew();
                        }
                        // Code to run on UI thread
                    }
                });
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    public void onChatItem(User user, int position, boolean loading) {
        startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("user",user).putExtra("loading",loading));
    }

    @Override
    public void onMessagReceived(User user) {
        this.user = user;
        //getConversation();
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected)
            setNotificationCounter();
    }

    private void getConversationListNew(){

        Call<ConversationDetails> call = BaseServices.getAPI().create(ParameterServices.class).getMessagesList();
        call.enqueue(new Callback<ConversationDetails>() {
            @Override
            public void onResponse(@NotNull Call<ConversationDetails> call, @NotNull Response<ConversationDetails> response) {

                Log.d("message123", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                ConversationDetails conversationDetails = response.body();
                if(response.isSuccessful()){
                    if(conversationDetails!=null){
                        if (conversationDetails.getConversationList().size() > 0) {
                            conversations = new ArrayList<>();
                            conversations.addAll(conversationDetails.getConversationList());
                            setConversationList();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ConversationDetails> call, @NotNull Throwable t) {
                Log.d("failure1",t.getLocalizedMessage()+"");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (handler != null)
            handler.removeCallbacks(runnable);*/
    }
}
