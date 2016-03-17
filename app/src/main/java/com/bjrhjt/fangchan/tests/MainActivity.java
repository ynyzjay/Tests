package com.bjrhjt.fangchan.tests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
private String url="http://192.168.1.140:8080/fang/a/house/user/app/erhouse/getErHouseList";
    OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String message = "{\"pageNo\":\"" + 1 + "\",\"pageSize\":\"" + 10 + "\",\"deviceType\":\"" + 0 + "\"}";
        Cache cache = new Cache(getExternalCacheDir(), 10 * 1024 * 1024);
        okHttpClient = new OkHttpClient.Builder().cache(cache).connectTimeout(10, TimeUnit.SECONDS).addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR).build();
        RequestBody jsonBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        Request request = new Request.Builder().tag(this).url(url).post(jsonBody).build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("info",e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                try {
//                    Log.i("info",response.body().string());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        d(request);
    }
    private void d(final Request request) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Response response1 = okHttpClient.newCall(request).execute();
                    if (!response1.isSuccessful())
                        throw new IOException("Unexpected code " + response1);

                    String response1Body = response1.body().string();
                    Log.i("info","Response 1 response:          " + response1);
                    Log.i("info","Response 1 cache response:    " + response1.cacheResponse());
                    Log.i("info","Response 1 network response:  " + response1.networkResponse());

                    Response response2 = okHttpClient.newCall(request).execute();
                    if (!response2.isSuccessful())
                        throw new IOException("Unexpected code " + response2);

                    String response2Body = response2.body().string();
                    Log.i("info","Response 2 response:          " + response2);
                    Log.i("info","Response 2 cache response:    " + response2.cacheResponse());
                    Log.i("info","Response 2 network response:  " + response2.networkResponse());

                    Log.i("info", "Response 2 equals Response 1? " + response1Body.equals(response2Body));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", String.format("max-age=%d", 60))
                    .build();
        }
    };
}
