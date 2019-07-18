package com.tz.intelligentdesklamp.util.network;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.content.Context.MODE_PRIVATE;

/**
 * 关于网络请求
 * 文件上传
 * 与用户提示
 * 根据url获取图片资源
 */

public class HttpUtil {

    /**
     * 携带token的网络请求
     */
    public static void sendOkHttpRequestWithTokenAndBody(String url,Context context,RequestBody requestBody, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",HttpUtil.getToken(context))
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static String getToken (Context context){
        //取出token数据
        String token =context.getSharedPreferences("login_data", MODE_PRIVATE)
                .getString("token", "data_null");
        return token;
    }

    /**
     *不携带token的网络请求
     */
    public static void sendOkHttpRequestWithBody(String url,RequestBody requestBody, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 不携带body的网络请求
     */
    public static void sendOkHttpRequestWithToken(String url,Context context,Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",getToken(context))
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 携带token的文件上传 修正
     */
    public static void sendOkHttpRequestWithTokenAndHead(String url,Context context,File file,Callback callback){
        //关于requestbody
        MultipartBody.Builder requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body=RequestBody.create(MediaType.parse("image/*"),file);//MediaType.parse()里面是上传的文件类型
        requestBody.addFormDataPart("photo",file.getName(),body);//请求键值要注意

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",getToken(context))
                .url(url)
                .post(requestBody.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 携带token的文件上传
     */
    public static String sendFileRequestWithOkHttpAndToken(String url,Context context, File file){

        MultipartBody.Builder requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body=RequestBody.create(MediaType.parse("image/*"),file);//MediaType.parse()里面是上传的文件类型
        requestBody.addFormDataPart("background",file.getName(),body);//请求键值background要注意

        OkHttpClient client=new OkHttpClient();
        Request request=null;
        if (requestBody!=null){
            request=new Request.Builder()
                    .addHeader("Authorization",getToken(context))
                    .url(url)
                    .post(requestBody.build())
                    .build();
        }else{
            request=new Request.Builder()
                    .addHeader("Authorization",getToken(context))
                    .url(url)
                    .build();
        }
        /**
         * 返回数据
         */
        Response response= null;
        try {
            response = client.newCall(request).execute();
            String responseData=response.body().string();
            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
            return "发生错误！";
        }
    }


    /**
     * 向用户发出的信息
     */
    public static void showWrong(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
    public static void showSuccess(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    /**
     *获取图片byte数组
     */
    //获取图片资源图片
    public static byte[] getBitmap(String url,Context context){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",getToken(context))
                .url(url)
                .build();
        try {
            Response response=client.newCall(request).execute();
            if (response.code()==200){
                //将资源转换类型
                byte[] pictureByte=response.body().bytes();
                return pictureByte;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }








    //返回最简单格式json的请求
    public static String sendOkHttpRequestWithNull(String url,String token){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",token)
                .url(url)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String responseData=response.body().string();
            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //返回最简单格式json的请求
    public static String sendOkHttpRequest(String url, RequestBody requestBody){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String responseData=response.body().string();
            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendOkHttpRequestWithTokenAndBody(String url,String token,RequestBody requestBody){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",token)
                .post(requestBody)
                .url(url)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String responseData=response.body().string();
            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





}
