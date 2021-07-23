package com.app.hmp.cognitive.afrocamgistchat.api;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;
import com.app.hmp.cognitive.afrocamgistchat.activity.UrlEndpoints;
import com.app.hmp.cognitive.afrocamgistchat.adapters.OnTaskCompleted;
import org.json.JSONObject;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class Webservices {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(7, TimeUnit.MINUTES)
            .writeTimeout(7, TimeUnit.MINUTES)
            .readTimeout(7, TimeUnit.MINUTES)
            .build();

    private static String makeHttpRequestPutOrPost(String url, ArrayList<Params> params, Map<String, String> headers, Webservices.Method requestType) {


        String json;
        try {

            for (int i = 0; i < params.size(); i++) {

                params.get(i).setValue(params.get(i).getValue().replace("\"", "\\\"").replace("\'", "\\\'"));
            }
            json = execute(url, params, headers, requestType);

        } catch (ConnectException e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Oops something went wrong....";
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Internet not available. Please try again later";
        }


        return json;

    }

    private static String makeHttpRequestPutOrPost(String url, JSONObject params, Map<String, String> headers, Webservices.Method requestType) {

        String json;
        try {
            json = execute(url, params, headers, requestType);
        } catch (ConnectException e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Oops something went wrong....";
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Internet not available. Please try again later";
        }

        return json;

    }

    private static String makeHttpRequestGet(String url, Map<String, String> headers) throws IOException {
        Request request = new Request.Builder()
                .headers(Headers.of(headers))
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String makeHttpRequestDelete(String url, ArrayList<Params> params, Map<String, String> headers, Webservices.Method requestType) {

        String json;
        try {

            for (int i = 0; i < params.size(); i++) {

                params.get(i).setValue(params.get(i).getValue().replace("\"", "\\\"").replace("\'", "\\\'"));
            }
            json = execute(url, params, headers, requestType);

        } catch (ConnectException e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Oops something went wrong....";
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Internet not available. Please try again later";
        }


        return json;

    }

    private static String execute(String url, ArrayList<Params> params, Map<String, String> headers, Webservices.Method requestType) throws IOException {

        FormBody.Builder formBody = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            formBody.add(params.get(i).getName(), params.get(i).getValue());
        }

        Log.d("jsonURL", url);
        Log.d("jsonURL PARAM", params.toString());
        Request request;

        if (Method.PUT == requestType) {
            request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .put(formBody.build())
                    .build();
        } else if (Method.DELETE == requestType) {
            request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .delete()
                    .build();
        } else {
           request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .post(formBody.build())
                    .build();
        }

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String execute(String url, JSONObject params, Map<String, String> headers, Webservices.Method requestType) throws IOException {

        final MediaType MEDIA_TYPE = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(MEDIA_TYPE, params.toString());

        Log.d("jsonURL", url);
        Log.d("jsonURL PARAM", params.toString());
        Request request;

        if (Method.PUT == requestType) {
            request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .put(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .post(body)
                    .build();
        }

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public static void getData(Webservices.Method requestType, ArrayList<Params> parameters, Map<String, String> headers, String url, OnTaskCompleted onTaskCompleted) {
        new GetData(onTaskCompleted, parameters, headers, url, requestType).execute();
    }

    public static void getData(Webservices.Method requestType, JSONObject parameters, Map<String, String> headers, String url, OnTaskCompleted onTaskCompleted) {
        new GetData(onTaskCompleted, parameters, headers, url, requestType).execute();
    }

    public static void getData(Activity activity, Webservices.Method requestType, MultipartBody.Builder builder, Map<String, String> headers, String url, OnTaskCompleted onTaskCompleted) {
        execute(activity, requestType, builder, headers, url,onTaskCompleted);
    }

    public static void getData(Activity activity, Webservices.Method requestType, MultipartBody.Builder builder, Map<String, String> headers, String url, OnTaskCompleted onTaskCompleted, ProgressListener progressListener) {
        execute(activity, requestType, builder, headers, url,onTaskCompleted, progressListener);
    }

    private static void execute(final Activity activity, Webservices.Method requestType, MultipartBody.Builder builder, Map<String, String> headers, String url, final OnTaskCompleted onTaskCompleted) {
        RequestBody requestBody = builder.build();
        Request request;

        if (Method.PUT == requestType) {
            request = new Request.Builder()
                    .url(UrlEndpoints.BASE_URL + url)
                    .headers(Headers.of(headers))
                    .put(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(UrlEndpoints.BASE_URL + url)
                    .headers(Headers.of(headers))
                    .post(requestBody)
                    .build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTaskCompleted.onResponse("");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("LLLL_Res1: ",response.toString());
                        if (response.body()!=null) {
                            try {
                                onTaskCompleted.onResponse(response.body().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                                onTaskCompleted.onResponse("");
                            }
                        }
                        else {
                            onTaskCompleted.onResponse("");
                        }
                    }
                });
            }
        });

    }

    private static void execute(final Activity activity, Webservices.Method requestType, MultipartBody.Builder builder, Map<String, String> headers, String url, final OnTaskCompleted onTaskCompleted, ProgressListener progressListener) {
        RequestBody requestBody = builder.build();
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody,progressListener);
        Request request;

        if (Method.PUT == requestType) {
            request = new Request.Builder()
                    .url(UrlEndpoints.BASE_URL + url)
                    .headers(Headers.of(headers))
                    .put(countingRequestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(UrlEndpoints.BASE_URL + url)
                    .headers(Headers.of(headers))
                    .post(countingRequestBody)
                    .build();
        }

        /*OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body(), progressListener))
                            .build();
                })
                .build();*/

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(() -> onTaskCompleted.onResponse(""));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("LLLL_Res2: ",response.toString());
                        if (response.body()!=null) {
                            try {
                                onTaskCompleted.onResponse(response.body().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                                onTaskCompleted.onResponse("");
                            }
                        }
                        else {
                            onTaskCompleted.onResponse("");
                        }
                    }
                });
            }
        });

    }

    public static class CountingRequestBody extends RequestBody {

        protected RequestBody delegate;
        protected ProgressListener listener;

        protected CountingSink countingSink;

        public CountingRequestBody(RequestBody delegate, ProgressListener listener) {
            this.delegate = delegate;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            BufferedSink bufferedSink;
            countingSink = new CountingSink(sink);
            bufferedSink = Okio.buffer(countingSink);
            delegate.writeTo(bufferedSink);
            bufferedSink.flush();
        }

        protected final class CountingSink extends ForwardingSink {
            private long bytesWritten = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                bytesWritten += byteCount;
                listener.update(bytesWritten, contentLength());
            }

        }

    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength()/*, bytesRead == -1*/);
                    return bytesRead;
                }
            };
        }
    }

    public interface ProgressListener {
        void update(long bytesRead, long contentLength/*, boolean done*/);
    }

    private static class GetData extends AsyncTask<Void, Void, String> {

        ArrayList<Params> parameters;
        JSONObject params;
        Map<String, String> headers;
        String url;
        Webservices.Method requestType;
        OnTaskCompleted onTaskCompleted;


        GetData(OnTaskCompleted onTaskCompleted, ArrayList<Params> parameters, Map<String,String> headers, String url, Webservices.Method requestType) {
            this.onTaskCompleted = onTaskCompleted;
            this.parameters = parameters;
            this.headers = headers;
            this.url = url;
            this.requestType = requestType;
        }

        GetData(OnTaskCompleted onTaskCompleted, JSONObject parameters, Map<String,String> headers, String url, Webservices.Method requestType) {
            this.onTaskCompleted = onTaskCompleted;
            this.params = parameters;
            this.headers = headers;
            this.url = url;
            this.requestType = requestType;
        }


        @Override
        protected String doInBackground(Void... voids) {

            try {
                String result;

                Log.d("jsonUrl", UrlEndpoints.BASE_URL + url);
                if (Method.GET == requestType) {
                    result = makeHttpRequestGet(UrlEndpoints.BASE_URL + url, headers);
                } else if(Method.DELETE == requestType){
                    result = makeHttpRequestDelete(UrlEndpoints.BASE_URL + url, parameters, headers, requestType);
                } else {
                    if (parameters == null) {
                        result = makeHttpRequestPutOrPost(UrlEndpoints.BASE_URL + url, params, headers, requestType);
                    } else {
                        result = makeHttpRequestPutOrPost(UrlEndpoints.BASE_URL + url, parameters, headers, requestType);
                    }
                }
                Log.d("jsonResponse", result);
                return result;
            } catch (ConnectException e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
                return "Oops something went wrong....";
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
                return "Internet not available. Please try again later";
            }

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            onTaskCompleted.onResponse(response);
        }
    }

    public enum Method {
        POST,
        PUT,
        GET,
        DELETE
    }
}
