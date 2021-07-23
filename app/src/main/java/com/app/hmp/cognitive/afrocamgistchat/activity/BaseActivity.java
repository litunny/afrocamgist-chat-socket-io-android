package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.app.hmp.cognitive.afrocamgistchat.constants.Constants;
import com.app.hmp.cognitive.afrocamgistchat.model.Post;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import id.zelory.compressor.Compressor;

public class BaseActivity extends AppCompatActivity {
    public static final long DISCONNECT_TIMEOUT = 300000; // 5 min = 5 * 60 * 1000 ms
    private static int count = 0;
    private static Handler handler = new Handler();
    public static boolean isBackground = false;

    private BroadcastReceiver mLangaugeChangedReceiver;


    private static Runnable runnable;
    private static Handler disconnectHandler = new Handler(msg -> {
        // todo
        return true;
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        ErrorReporter errReporter = new ErrorReporter();
        errReporter.Init(BaseActivity.this);
        errReporter.CheckErrorAndSendMail(BaseActivity.this);

        mLangaugeChangedReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, final Intent intent) {
                startActivity(getIntent());
                finish();
            }
        };

        // Register receiver
        registerReceiver(mLangaugeChangedReceiver, new IntentFilter("Language.changed"));

    }

    private static Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
//            Logger.e("LLLL_MyApp", "3600min or up " + count);
            // Perform any required operation on disconnect
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onUserInteraction() {
//        Logger.e("LLLL_MyApp", "for "+isBackground);
        if (isBackground) {
            resetDisconnectTimer();
            if (runnable!=null) {
                handler.removeCallbacks(runnable);
//                Logger.e("LLLL_MyApp", "back");
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        count = count + 1;
//                        Logger.e("LLLL_MyApp", String.valueOf(count));
                        handler.postDelayed(this, 60000);
                    }
                };
            } else {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        count = count + 1;
//                        Logger.e("LLLL_MyApp", String.valueOf(count));
                        handler.postDelayed(this, 60000);
                    }
                };
            }
            handler.postDelayed(runnable, 60000);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            isBackground = true;
        }
        super.onTrimMemory(level);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if (count != 0) {
            if (count >= 5) {
                if (LocalStorage.getIsUserLoggedIn())
                    getAfroPopularPosts();
                else{
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
//                Logger.e("LLLL_MyApp", "5min or less " + count);
            }
            count = 0;
        }*/

       //======= Working code of 5min refresh logic ========
//        if (count != 0) {
//            if (count >= 5 && !isNotification) {
//                if (LocalStorage.getIsUserLoggedIn())
//                    getAfroPopularPosts();
//                else{
//                    Logger.e("LLLL_MyApp", "5min or more + false " + count);
//                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            } else if(isNotification){
//                Logger.e("LLLL_MyApp", "5min or less or more + true" + count);
//            }
//            count = 0;
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister receiver
        if (mLangaugeChangedReceiver != null) {
            try {
                unregisterReceiver(mLangaugeChangedReceiver);
                mLangaugeChangedReceiver = null;
            } catch (final Exception e) {}
        }

    }

//    private void getAfroPopularPosts() {
//        Logger.e("LLLL_Bare: ",LocalStorage.getLoginToken());
//        Map<String, String> headers = new HashMap<>();
//        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());
//
//        Webservices.getData(Webservices.Method.GET, new ArrayList<>(), headers, UrlEndpoints.MOST_POPULAR, response -> {
//            if ("".equals(response)) {
//                Utils.showAlert(BaseActivity.this, getString(R.string.opps_something_went_wrong));
//            } else {
//                try {
//                    JSONObject object = new JSONObject(response);
//
//                    if (object.has(Constants.MESSAGE)) {
//                        Utils.showAlert(BaseActivity.this, object.getString(Constants.MESSAGE));
//                    } else {
//                        PostDetails postDetails = new Gson().fromJson(response, PostDetails.class);
//
//                        //Intent intent = new Intent(BaseActivity.this, BubbleActivity.class);
//                        Intent intent = new Intent(BaseActivity.this, DashboardActivity.class);
//
//                        if (postDetails.getPosts() != null && postDetails.getPosts().size() != 0) {
//                            LocalStorage.savePopularPosts(postDetails.getPosts());
//                            getAndStorePopularPostsImages(postDetails.getPosts());
//                            intent.putExtra("posts_bubble",postDetails.getPosts());
//                        }
//
//                        startActivity(intent);
//                        finish();
//                    }
//
//                } catch (Exception e) {
////                    Logger.e("LLLL_Data: ",e.getMessage());
//                    e.printStackTrace();
//                    Utils.showAlert(BaseActivity.this, getString(R.string.opps_something_went_wrong));
//                }
//            }
//
//        });
//    }

    private void getAndStorePopularPostsImages(ArrayList<Post> posts) {
        try {
            new GetAndStoreImage(posts).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetAndStoreImage extends AsyncTask<Void, Void, ArrayList<String>> {

        private ArrayList<Post> posts;

        GetAndStoreImage(ArrayList<Post> posts) {
            this.posts = posts;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> images) {
            super.onPostExecute(images);
            LocalStorage.savePopularPostsImage(images);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            ArrayList<String> drawables = new ArrayList<>();

            for (Post post : posts) {

                String src;

                if ("video".equals(post.getPostType())) {
                    src = UrlEndpoints.MEDIA_BASE_URL + post.getThumbnail() + "?width=200&height=200";
                    BitmapDrawable drawable = getBitmapFromURL(src);
                    if (drawable!=null)
                        drawables.add(convertDrawableToString(drawable));
                    else
                        drawables.add(null);
                } else if ("image".equals(post.getPostType())) {
                    src = UrlEndpoints.MEDIA_BASE_URL + post.getPostImage().get(0) + "?width=200&height=200";
                    BitmapDrawable drawable = getBitmapFromURL(src);
                    if (drawable!=null)
                        drawables.add(convertDrawableToString(drawable));
                    else
                        drawables.add(null);
                } else {
                    drawables.add(null);
                }
            }

            return drawables;
        }

        private String convertDrawableToString(BitmapDrawable drawable) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            drawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream); //bm is the bitmap object
            byte[] b = outputStream.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public BitmapDrawable getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return new BitmapDrawable(Resources.getSystem(), getCompressedBitmap(BitmapFactory.decodeStream(input)));
        } catch (Exception e) {
            return null;
        }
    }

    private Bitmap getCompressedBitmap(Bitmap image) {

        File imageFile = new File(Environment.getExternalStorageDirectory(), Calendar.getInstance().getTimeInMillis() + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();

            Bitmap file =  new Compressor(BaseActivity.this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(60)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    /*.setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())*/
                    .compressToBitmap(imageFile);

            imageFile.delete();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return image;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.ALLOW_OPEN_PROFILE = false;
        Log.d("swipeHelper9onBack",Constants.ALLOW_OPEN_PROFILE+"");
    }
}
