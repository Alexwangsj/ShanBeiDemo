package com.shanbei.shanbeidemo.http;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ALEX on 2016/12/28.
 */

public class OkHttpUtil {

    private final String TAG = "OkHttpUtil";
    private final int RetryNum = 3;

    private static final long cacheSize = 1024 * 1024 * 20;// 缓存文件最大限制大小20M
    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/okttpcaches"; // 设置缓存文件路径
    private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);  //

    private OkHttpClient mClient;


    private static OkHttpUtil mInstance;

    public static OkHttpUtil getInstance() {
        if (null == mInstance) {
            synchronized (OkHttpUtil.class) {
                if (null == mInstance) {
                    mInstance = new OkHttpUtil();
                }
            }
        }
        return mInstance;
    }

    private OkHttpUtil() {
        mClient = new OkHttpClient().newBuilder()
                .addInterceptor(new RetryIntercepter(2))
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build();
    }


    public void getAsync(String url, Callback responseCallback) {

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();

            Call call = mClient.newCall(request);
            call.enqueue(responseCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAsync(String url, String resourceName) {

        int i = 0;

        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();

            Call call = mClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (null != response && response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Response getSync(String url) {
        //同步过程
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();

            Call call = mClient.newCall(request);
            Response response = call.execute();
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
