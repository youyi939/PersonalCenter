package com.example.personalcenter.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KenUtils {


    /**
     * 获取网络资源
     * 不需要Token
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendGet(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url(url).method("GET", null).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * 个人中心登陆
     * @param url "http://124.93.196.45:10002/login"
     * @return
     * @throws IOException
     */
    public static String logIn(String url, String user)throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, user);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * 发Get请求，需携带token
     * @param url
     * @param token
     * @return
     * @throws IOException
     */
    public static String senGet_T(String url,String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * 修改用户个人信息,需要Token
     * @param url
     * @return
     */
    public static String ChangeUser(String url,String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * 修改密码 PUT请求，需携带Token和相应的JSON数据
     * @param token
     * @param message
     * @return
     * @throws IOException
     */
    public static String changePassword(String token,String message) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, message);
        Request request = new Request.Builder()
                .url("http://124.93.196.45:10002/system/user/resetPwd")
                .method("PUT", body)
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * 新增意见反馈 POST请求，需携带Token及具体JSON数据
     * @param token
     * @param msg
     * @return
     * @throws IOException
     */
    public static String postFeedBack(String token,String msg) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,msg);
        Request request = new Request.Builder()
                .url("http://124.93.196.45:10002/userinfo/feedback")
                .method("POST", body)
                .addHeader("Authorization",token)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
