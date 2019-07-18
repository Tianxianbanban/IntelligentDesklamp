package com.tz.intelligentdesklamp.util.use_never;

/**
 * 图片上传
 * 图片下载
 */

import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Upload {
    /**
     * 携带token的网络请求
     */
    public static String sendBackRequestWithOkHttpAndToken(String token,File file,String backUrl) throws IOException {

        MultipartBody.Builder requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body=RequestBody.create(MediaType.parse("image/*"),file);//MediaType.parse()里面是上传的文件类型
        requestBody.addFormDataPart("background",file.getName(),body);//请求键值background要注意

        OkHttpClient client=new OkHttpClient();
        Request request=null;
        if (requestBody!=null){
            request=new Request.Builder()
                    .addHeader("Authorization",token)
                    .url(backUrl)
                    .post(requestBody.build())
                    .build();
        }else{
            request=new Request.Builder()
                    .addHeader("Authorization",token)
                    .url(backUrl)
                    .build();
        }
        /**
         * 返回数据
         */
        Response response=client.newCall(request).execute();
        String responseData=response.body().string();
        return responseData;
    }

    //未用到了
    public static String obtainHeadRequestWithOkHttpAndToken(String backQueryUrl,String token) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",token)
                .url(backQueryUrl)
                .build();
        Response response=client.newCall(request).execute();
        String responseData=response.body().string();

        return responseData;
    }



}
