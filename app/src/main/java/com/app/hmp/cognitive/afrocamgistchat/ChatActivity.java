package com.app.hmp.cognitive.afrocamgistchat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.hmp.cognitive.afrocamgistchat.activity.BaseServices;
import com.app.hmp.cognitive.afrocamgistchat.activity.LocalStorage;
import com.app.hmp.cognitive.afrocamgistchat.activity.Message;
import com.app.hmp.cognitive.afrocamgistchat.activity.MessageDetails;
import com.app.hmp.cognitive.afrocamgistchat.activity.ParameterServices;
import com.app.hmp.cognitive.afrocamgistchat.activity.ToolDotProgress;
import com.app.hmp.cognitive.afrocamgistchat.activity.UrlEndpoints;
import com.app.hmp.cognitive.afrocamgistchat.activity.Utils;
import com.app.hmp.cognitive.afrocamgistchat.adapters.ChatMessageAdapter;
import com.app.hmp.cognitive.afrocamgistchat.adapters.OnTaskCompleted;
import com.app.hmp.cognitive.afrocamgistchat.api.Webservices;
import com.app.hmp.cognitive.afrocamgistchat.constants.Constants;
import com.app.hmp.cognitive.afrocamgistchat.fragment.BottomPopup;
import com.app.hmp.cognitive.afrocamgistchat.activity.BaseSocketActivity;
import com.app.hmp.cognitive.afrocamgistchat.model.User;
import com.app.hmp.cognitive.afrocamgistchat.service.WebSocketService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import net.alhazmy13.mediapicker.Image.ImagePicker;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.hmp.cognitive.afrocamgistchat.service.WebSocketService.ACTION_SOCKET_MESSAGE;

public class ChatActivity extends BaseSocketActivity implements View.OnClickListener, BottomPopup.DialogDismissListener, ChatMessageAdapter.OnMessageClickListener {

    private User user;
    private ImageView more;
    private LinearLayout ll_more,ll_report,ll_cancel;
    private CircleImageView profilePicture;
    private TextView name, tv_null, replyTo, tv_please_wait;
    private EditText message;
    private int messageCount = 0;
    private int lastMessageId = 0;
    private int currentScrollPosition = 0;
    private boolean hasReceivedNewMessages = false;
    private Handler handler;
    private ArrayList<Message> messages = new ArrayList<>();
    public static final int CAMERA_INTENT = 100;
    private boolean isApiCall = false, isLoader;

    private FrameLayout flUserBlockedView;
    private TextView tvButtonUnblock;
    private KProgressHUD hud,hudChat;
    private ToolDotProgress dots_progress;

    protected LocalBroadcastManager localBroadcastManager;
    public static String NOTIFY_COUNTER="notify_counter";

    private boolean isEditMessage = false, isReplyMessage = false;
    private int messageId,toId;

    ChatMessageAdapter adapter =null;
    RecyclerView chatList;

    private boolean isLoading = false;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(), "onReceived()");
            String message = intent.getStringExtra(ACTION_SOCKET_MESSAGE);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            ArrayList<Message> messages = new ArrayList<>();
            messages.add(new Message(message));
            saveMessagesToLocal(ChatActivity.this, messages);
            setMessagesView(false);
        }
    };

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        if (getIntent().getSerializableExtra("user") != null)
            user = (User) getIntent().getSerializableExtra("user");

        //TODO: Remove
        user = ChatActivityUserWraper.getInstance().getUser();
        isLoader = getIntent().getBooleanExtra("loading",false);

        Log.d("isLoader",""+isLoader);

        ChatActivityUserWraper.getInstance().setUser(user);

        initView();
        initClickListener();

        setData();

        if (Utils.isConnected()) {
            //getConversation();
            //getConversationNew();
            getConversation(2000);
        } else
            Utils.showAlert(ChatActivity.this, "Please check your internet connection");
    }

    private void initView() {

        replyTo = findViewById(R.id.reply_to);
        more = findViewById(R.id.more);
        ll_more = findViewById(R.id.ll_more);
        ll_report = findViewById(R.id.llReport);
        ll_cancel = findViewById(R.id.ll_cancel);
        dots_progress = findViewById(R.id.dots_progress);

        profilePicture = findViewById(R.id.profile_picture);
        name = findViewById(R.id.name);
        message = findViewById(R.id.message);
        tv_null = findViewById(R.id.tv_null);
        //tv_please_wait = findViewById(R.id.tv_please_wait);

        flUserBlockedView = findViewById(R.id.flUserBlockedView);

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setCancellable(true);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_more.getVisibility() == View.VISIBLE)
                    collapse(ll_more);
                else
                    expand(ll_more);
            }
        });

        ll_report.setOnClickListener(v -> {
            callReport();
            collapse(ll_more);
        });

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(ll_more);
            }
        });

    }

    private void initClickListener() {

        findViewById(R.id.select_image).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.new_messages).setOnClickListener(this);
        findViewById(R.id.name).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.tvButtonUnblock).setOnClickListener(this);

    }

    private void callReport(){

        JSONObject request = new JSONObject();
        try {
            request.put("report_reason","");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.CHAT_REPORT + user.getUserId() + "/report";

        Webservices.getData(Webservices.Method.POST,request, headers, url, response -> {
            if ("".equals(response)) {
                Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
            } else {
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.has(Constants.MESSAGE)){
                        Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                    }else {
                        Toast.makeText(ChatActivity.this,"User reported successfully",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                }
            }
        });
    }

    //set profile data
    private void setData() {

        if (user != null) {

            String fullName = user.getFirstName() + " " + user.getLastName();
            name.setText(fullName);

            Glide.with(this)
                    .load(UrlEndpoints.MEDIA_BASE_URL + user.getProfileImageUrl())
                    .into(profilePicture);

            if(isLoader){
                getConversationNew();
            }else {
                if(getAllMessages(this)!=null && !getAllMessages(this).isEmpty()){
                    setMessagesView(false);
                }else {
                    getConversationNew();
                }
            }
        }
    }

    //get chat messages from server
    private void getConversation() {

        if (user != null) {

            Log.d("userId9",""+user.getUserId());

            Map<String, String> headers = new HashMap<>();
            headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());
            String url = UrlEndpoints.CONVERSATION + user.getUserId();
            Webservices.getData(Webservices.Method.GET, new ArrayList<>(), headers, url, response -> {
                if ("".equals(response)) {
                    Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                } else {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has(Constants.MESSAGE)) {
                            Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                        } else {
                            MessageDetails messageDetails = new Gson().fromJson(response, MessageDetails.class);
                            if (messageDetails.getMessages().size() > 0) {

                                messageCount = messageDetails.getMessages().size();
                                lastMessageId = messageDetails.getMessages().get(messageCount - 1).getMessageId();

                                Log.d("AllMessages",new GsonBuilder().setPrettyPrinting().create().toJson(messageDetails.getMessages()));

                                saveMessagesToLocal(ChatActivity.this,messageDetails.getMessages());
                                setMessagesView(false);

                                //setMessagesView(messageDetails.getMessages());

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                    }
                }
            });

        }
    }

    private void getConversationNew(){

         /*hudChat = KProgressHUD.create(ChatActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setDetailsLabel("Please wait")
                .setCancellable(false)
                .show();*/

        Log.d("userId9",""+user.getUserId());

        dots_progress.setVisibility(View.VISIBLE);
        
        Call<MessageDetails> call = BaseServices.getAPI().create(ParameterServices.class).getAllMessages(user.getUserId());
        call.enqueue(new Callback<MessageDetails>() {
            @Override
            public void onResponse(@NotNull Call<MessageDetails> call, @NotNull Response<MessageDetails> response) {
                /*if(hudChat!=null){
                    hudChat.dismiss();
                }*/

                if(isLoader){
                    notifyCounter();
                }

                dots_progress.setVisibility(View.GONE);
                Log.d("responseChat", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                MessageDetails messageDetails = response.body();
                if(response.isSuccessful()){
                    if(messageDetails!=null){
                        if (messageDetails.getMessages().size() > 0) {

                            messageCount = messageDetails.getMessages().size();
                            lastMessageId = messageDetails.getMessages().get(messageCount - 1).getMessageId();

                            saveMessagesToLocal(ChatActivity.this,messageDetails.getMessages());
                            setMessagesView(false);

                            //setMessagesView(messageDetails.getMessages());
                        }else {

                            tv_null.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageDetails> call, @NotNull Throwable t) {
                /*if(hudChat!=null){
                    hudChat.dismiss();
                }*/
                dots_progress.setVisibility(View.GONE);
                Log.d("failure1",t.getLocalizedMessage()+"");
            }
        });
    }

    public void saveMessagesToLocal(Context context,ArrayList<Message> messages) {
        SharedPreferences mPrefs = context.getSharedPreferences("ChatData", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        prefsEditor.putString(""+user.getUserId(), json);
        prefsEditor.commit();
    }

    public ArrayList<Message> getAllMessages(Context context) {
        ArrayList<Message> savedMessages = new ArrayList<Message>();
        Log.d("userId111",""+user.getUserId());
        SharedPreferences mPrefs = context.getSharedPreferences("ChatData", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(""+user.getUserId(), "");
        if (json.isEmpty()) {
            savedMessages = new ArrayList<Message>();
        } else {
            Type type = new TypeToken<List<Message>>() {
            }.getType();
            savedMessages = gson.fromJson(json, type);
        }

        return savedMessages;
    }

    private void getConversation(int timeInterval) {
        handler = new Handler();
        handler.postDelayed(runnable, timeInterval);
    }

    boolean isRunning = true;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Utils.isConnected()){
                Log.d("handler99","000");
                //refreshConversation();
                refreshConversationNew();
            }
            if(isRunning){
                handler.postDelayed(this, 2000);
            }else {
                handler.removeCallbacks(runnable);
            }

        }
    };



    private void refreshConversation() {

        if (user != null) {

            Map<String, String> headers = new HashMap<>();
            headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

            String url = UrlEndpoints.CONVERSATION + user.getUserId();

            Webservices.getData(Webservices.Method.GET, new ArrayList<>(), headers, url, response -> {
                if ("".equals(response)) {
                    Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                } else {
                    try {
                        JSONObject object = new JSONObject(response);

                        Log.d("messageResponse", new GsonBuilder().setPrettyPrinting().create().toJson(object));

                        if (object.has(Constants.MESSAGE)) {
                            Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                        } else {
                            MessageDetails messageDetails = new Gson().fromJson(response, MessageDetails.class);
                            if (messageDetails.getMessages().size() > 0) {
                                messageCount = messageDetails.getMessages().size();
                                int newLastMessageId = messageDetails.getMessages().get(messageCount - 1).getMessageId();
                                if (messageDetails.getMessages().size() > 0 && lastMessageId != newLastMessageId) {
                                    lastMessageId = newLastMessageId;
                                    findViewById(R.id.new_messages).setVisibility(View.GONE);
                                    Log.d("response===","success");

                                    saveMessagesToLocal(ChatActivity.this,messageDetails.getMessages());
                                    setMessagesView(false);
                                    //setMessagesView(messageDetails.getMessages());


                                    /*if (currentScrollPosition==(messageCount-1)) {
                                        findViewById(R.id.new_messages).setVisibility(View.GONE);
                                        setMessagesView(messageDetails.getMessages());
                                    } else {
                                        findViewById(R.id.new_messages).setVisibility(View.VISIBLE);
                                        messages = messageDetails.getMessages();
                                        hasReceivedNewMessages = true;
                                    }*/
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                    }
                }
            });

        }
    }

    private void refreshConversationNew(){

        Log.d("otherUserId",""+user.getUserId());

        Call<MessageDetails> call = BaseServices.getAPI().create(ParameterServices.class).getAllMessages(user.getUserId());
        call.enqueue(new Callback<MessageDetails>() {
            @Override
            public void onResponse(@NotNull Call<MessageDetails> call, @NotNull Response<MessageDetails> response) {
                Log.d("responseChat", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                MessageDetails messageDetails = response.body();

                if(response.isSuccessful()){
                    if(messageDetails!=null){
                        if (messageDetails.getMessages().size() > 0) {
                            messageCount = messageDetails.getMessages().size();
                            int newLastMessageId = messageDetails.getMessages().get(messageCount - 1).getMessageId();

                            saveMessagesToLocal(ChatActivity.this,messageDetails.getMessages());

                            if (messageDetails.getMessages().size() > 0 && lastMessageId != newLastMessageId) {
                                lastMessageId = newLastMessageId;
                                findViewById(R.id.new_messages).setVisibility(View.GONE);
                                setMessagesView(true);

                            }else {
                                setMessagesView(false);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageDetails> call, @NotNull Throwable t) {
                Log.d("failure1",t.getLocalizedMessage()+"");
            }
        });
    }



    //set chat list view
    private void setMessagesView(boolean isScroll) {
        if(adapter==null) {
            Log.d("NULL11","====111");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            chatList = findViewById(R.id.chat_list);
            chatList.setLayoutManager(linearLayoutManager);
            adapter = new ChatMessageAdapter(getAllMessages(this),this,this,user.getProfileImageUrl());
            chatList.setAdapter(adapter);
            chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (recyclerView.getLayoutManager() != null)
                        currentScrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    if (hasReceivedNewMessages && currentScrollPosition == (messageCount - 2)) {
                        findViewById(R.id.new_messages).setVisibility(View.GONE);
                        hasReceivedNewMessages = false;
                        adapter.fetchNewMessages(ChatActivity.this.messages);
                    }













                    //Call next pagination
                    /*int visibleItemCount = 0, totalItemCount = 0, pastVisibleItem = 0;

                    if (recyclerView.getLayoutManager() != null) {
                        visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                        totalItemCount = recyclerView.getLayoutManager().getItemCount();
                        pastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    }

                    Log.d("recyclerviewScroll","nextpage : "+ nextPage);
                    Log.d("recyclerviewScroll","dy : "+ dy);
                    Log.d("recyclerviewScroll","visibleItemCount : "+ visibleItemCount);
                    Log.d("recyclerviewScroll","pastVisibleItem : "+ pastVisibleItem);
                    Log.d("recyclerviewScroll","totalItemCount : "+ totalItemCount);
                    Log.d("recyclerviewScroll","((visibleItemCount + pastVisibleItem) >= (totalItemCount - 2)) : " +
                            ((visibleItemCount + pastVisibleItem) >= (totalItemCount - 2)));


                    if (!isLoading && dy < 0 && pastVisibleItem == 0) {

                        Log.d("NEXT_PAGE11","called.......");

                        isLoading = true;
                        getConversationNext();
                    }*/











                }
            });
        }else{
            Log.d("NULL11","====222");
            adapter.fetchNewMessages(getAllMessages(this));

            if(isScroll){
                chatList.scrollToPosition(getAllMessages(this).size()-1);
            }
        }

        if (getAllMessages(this).size() == 0) {
            tv_null.setVisibility(View.VISIBLE);
            chatList.setVisibility(View.GONE);
        } else {
            tv_null.setVisibility(View.GONE);
            chatList.setVisibility(View.VISIBLE);
        }

        if(user.getBlockedByMe()) {
            flUserBlockedView.setVisibility(View.VISIBLE);
        } else {
            flUserBlockedView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            Log.d("Images", mPaths.toString());
            showTextAndImage(mPaths);
        }
    }

    private void showTextAndImage(ArrayList<String> mPaths) {

//        startActivity(new Intent(this, SendImageMessageActivity.class)
//                .putExtra("images", mPaths)
//                .putExtra("user", user));
    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions)
                if (ActivityCompat.checkSelfPermission(ChatActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA_INTENT) {
                openImagePicker();
            }
        } else {
            Toast.makeText(ChatActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagePicker() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.select_image:
                String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

                if (!hasPermissions(PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ChatActivity.this, PERMISSIONS, CAMERA_INTENT);
                } else {
                    openImagePicker();
                }
                break;
            case R.id.send:

                if(user.getBlockedByMe())
                    return;

                if (Utils.isConnected()) {
                    if (!"".equals(message.getText().toString())) {
                        if(!isApiCall){

                            if(isEditMessage){
                                editMessage();
                            }else if(isReplyMessage){
                                replyMessage();
                            }else {

                                isApiCall = true;
                                sendMessage();
                            }

                        }

                    }else{
                        Utils.showAlert(ChatActivity.this, "Please Enter Message");
                    }

                } else {
                    Utils.showAlert(ChatActivity.this, "Please check your internet connection");
                }
                break;
            case R.id.new_messages:
                //setMessagesView(messages);
                setMessagesView(false);
                findViewById(R.id.new_messages).setVisibility(View.GONE);
                hasReceivedNewMessages = false;
                messages = new ArrayList<>();
                break;
            case R.id.name:
                //startActivity(new Intent(ChatActivity.this, ProfileActivity.class).putExtra("userId", user.getUserId()));
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.tvButtonUnblock:
                if (Utils.isConnected()) {
                    unBlockUser();
                } else {
                    Utils.showAlert(ChatActivity.this, "Please check your internet connection");
                }
                break;
            case R.id.cancel:
                findViewById(R.id.replying_layout).setVisibility(View.GONE);
                message.setText("");
                isEditMessage = false;
                isReplyMessage = false;
                break;
            default:
                break;
        }
    }

    private void unBlockUser() {
        hud.show();
        String url = "";
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        url = UrlEndpoints.PROFILE + "/" + user.getUserId() + "/unblock";

        Webservices.getData(Webservices.Method.GET, new ArrayList<>(), headers, url, new OnTaskCompleted() {
            @Override
            public void onResponse(String response) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(response);
                    Log.e("LLLLL_Res: ", response);
                    if (object.has(Constants.MESSAGE)) {
                        Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                    }
                    if (object.getString(Constants.MESSAGE).equalsIgnoreCase("Unblock Successful")) {
                        user.setBlockedByMe(false);
                    } else {
                        user.setBlockedByMe(true);
                    }
                    setMessagesView(false);
                    //setMessagesView(messages);

                } catch (Exception e) {
                    Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                    e.printStackTrace();
                }
            }
        });
    }

/*
    @Override
    public void onBackPressed() {

        *//*if(isTaskRoot()){
            Log.d("test111","999");
            isNotification = false;
            startActivity(new Intent(ChatActivity.this, DashboardActivity.class));
            finish();
        }else {
            Log.d("test111","000");
            super.onBackPressed();
        }*//*

        *//*if (isNotification) {
            Log.d("test111","999");
            isNotification = false;
            startActivity(new Intent(ChatActivity.this, DashboardActivity.class));
            finish();
        } else {
            Log.d("test111","000");
            super.onBackPressed();
        }*//*

    }*/

    //send message to user API
    private void sendMessage() {

        JSONObject request = new JSONObject();
        try {
            request.put("message_text", message.getText().toString().trim());
            request.put("message_image", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.CONVERSATION + user.getUserId();


        //TODO: Send message via websocket here start

        JSONObject data = new JSONObject();
        try {
            data.put("from_id", 2536);
            data.put("to_id", 2539);
            data.put("message", message.getText().toString().trim()); //TODO: get message from edittext
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

        //TODO; Send message via websocket end


        Webservices.getData(Webservices.Method.POST, request, headers, url, response -> {
            isApiCall = false;
            if ("".equals(response)) {
                Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
            } else {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has(Constants.MESSAGE)) {
                        Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                    } else {
                        message.setText("");

                        refreshConversationNew();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlert(ChatActivity.this, "Oops something went wrong....");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    /*@Override
    public void onStop() {
        super.onStop();
        isRunning = false;
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        if (handler != null)
            handler.removeCallbacks(runnable);
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isConnected()) {
            //getConversation();
            getConversation(2000);
        }
    }

    @Override
    public void onMessageSingleClick(int messageId) {
        Log.d("MESSAGE_CLICK","SINGLE==="+messageId);
        callMessageLikeDislike(messageId);
    }

    @Override
    public void onMessageDoubleClick(int messageId) {
        Log.d("MESSAGE_CLICK","DOUBLE==="+messageId);
        callMessageLikeDislike(messageId);
    }

    private void callMessageLikeDislike(int messageId){

        JSONObject request = new JSONObject();
        try {
            request.put("message_id",messageId);
            request.put("like_type", "message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.LIKE_UNLIKE + "/" + "message";

        Webservices.getData(Webservices.Method.POST, request ,headers, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    Log.d("deleteMessage11",""+object.toString());
                }

            } catch (Exception e) {
                Log.d("executed111","yes...");
                e.printStackTrace();
            }
        });
    }

    public static class ChatActivityUserWraper
    {
        private static ChatActivityUserWraper instance;
        public static ChatActivityUserWraper getInstance()
        {
            if(instance==null)
                instance = new ChatActivityUserWraper();
            return instance;
        }

        private User user;
        public void setUser(User user) {
            this.user = new User("2536", "John", "Doe", "john.doe", "john.doe@gmail.com", "08060203530", "", 2536);
        }

        public User getUser() {
            return this.user;
        }

        public void setBlockedByMe(Integer userId,boolean blockedByMe)
        {
            Log.e(ChatActivityUserWraper.class.getSimpleName(),"setBlockedByMe userId : "+userId+" blockByMe : "+blockedByMe);
            if(userId.equals(user.getUserId())) {
                Log.e(ChatActivityUserWraper.class.getSimpleName(),"setBlockedByMe value changed ");
                user.setBlockedByMe(blockedByMe);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ChatActivityUserWraper.getInstance().getUser()!=null)
            user = ChatActivityUserWraper.getInstance().getUser();

        if(getAllMessages(this)!=null && !getAllMessages(this).isEmpty()){
            setMessagesView(false);
        }else {
            getConversationNew();
        }

    }

    protected void notifyCounter(){
        Intent intent = new Intent(NOTIFY_COUNTER);
        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDialogDismiss(String action, int messageID, String messageText, int toId) {

        if(action.equalsIgnoreCase("edit")){
            isEditMessage = true;
            isReplyMessage = false;
            this.messageId = messageID;
            this.toId = toId;
            message.setText(messageText);
        }else if(action.equalsIgnoreCase("delete")){
            this.messageId = messageID;
            this.toId = toId;
            deleteMessage();
        }else if(action.equalsIgnoreCase("reply")){
            isReplyMessage = true;
            isEditMessage = false;
            this.messageId = messageID;
            this.toId = toId;
            if(messageText!=null && !messageText.equalsIgnoreCase("")){
                onReplyClicked(messageText);
            }

        }

    }

    @Override
    public void onReceiveMessageReply(Integer messageId, String messageText, Integer fromId) {
        isReplyMessage = true;
        isEditMessage = false;
        this.messageId = messageId;
        this.toId = fromId;
        if(messageText!=null && !messageText.equalsIgnoreCase("")){
            onReplyClicked(messageText);
        }
    }

    private void onReplyClicked(String messageText){
        findViewById(R.id.replying_layout).setVisibility(View.VISIBLE);
        replyTo.setText(messageText);
    }

    private void replyMessage() {
        JSONObject request = new JSONObject();
        try {
            request.put("message_text", message.getText().toString().trim());
            request.put("message_image", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.MESSAGES + "/" +"conversation/" + toId +"/"+ messageId;

        Webservices.getData(Webservices.Method.POST, request,headers, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    isReplyMessage = false;
                    message.setText("");
                    findViewById(R.id.replying_layout).setVisibility(View.GONE);
                    Log.d("replyMessage11",""+object.toString());
                }

            } catch (Exception e) {
                Log.d("executed111","yes...");
                e.printStackTrace();
            }
        });
    }

    private void editMessage() {
        JSONObject request = new JSONObject();
        try {
            request.put("message_text",message.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.MESSAGES + "/" + messageId;

        Webservices.getData(Webservices.Method.PUT, request,headers, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    isEditMessage = false;
                    message.setText("");
                    Log.d("editMessage11",""+object.toString());
                }

            } catch (Exception e) {
                Log.d("executed111","yes...");
                e.printStackTrace();
            }
        });
    }

    private void deleteMessage() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.MESSAGES + "/" + messageId;

        Webservices.getData(Webservices.Method.DELETE, new ArrayList<>(),headers, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(ChatActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    Log.d("deleteMessage11",""+object.toString());
                }

            } catch (Exception e) {
                Log.d("executed111","yes...");
                e.printStackTrace();
            }
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
