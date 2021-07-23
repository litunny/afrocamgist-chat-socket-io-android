package com.app.hmp.cognitive.afrocamgistchat.activity;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseServices {

    //Live Url
    public static final String BASE_URL = "https://manager.afrocamgist.com/api/";

    //Staging Url
    //public static final String BASE_URL = "https://manager.staging.afrocamgist.com/api/";

    //New Production Server Url
    //public static final String BASE_URL = "http://143.110.163.183:3000/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getAPI() {
        if (retrofit==null) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor(){
                @NotNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", " Bearer " + LocalStorage.getLoginToken())
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
