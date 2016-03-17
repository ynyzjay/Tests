package com.bjrhjt.fangchan.tests;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/1/16.
 */
public class OkhttpUtil {
    private static OkhttpUtil instance;
   private OkHttpClient okHttpClient;
    private Context mContext;
    private String url;
    private MediaType  mediaType = MediaType.parse("application/json; charset=utf-8");
    private OkhttpUtil() {
        Cache cache=new Cache(mContext.getCacheDir(),10*1024*1024);
        okHttpClient=new OkHttpClient.Builder().cache(cache).connectTimeout(10, TimeUnit.SECONDS).build();
    }

    public static OkhttpUtil getInstance() {
        if (instance == null) {
            synchronized (OkhttpUtil.class) {
                instance = new OkhttpUtil();
            }
        }
        return instance;
    }
    /**
     * 发送POST请求
     *
     * @param json 提交参数格式为JSON字符串
     */
    public void sendPost(Context context, String url, String json) {
        this.url = url;
        mContext = context;
        RequestBody jsonBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder().tag(context).url(url).post(jsonBody).build();
        doHttp(request);
    }
    /**
     * 发送异步的HTTP请求
     *
     * @param request 请求对象
     */
    private void doHttp(final Request request) {


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });



    }

}
