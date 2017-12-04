package com.clb.school.opencv__ndk.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/11/25.
 */
public class HttpUtil {

    //使用OkHttp进行网络通信
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();//发起Http请求
        client.newCall(request).enqueue(callback);//回调
    }

}