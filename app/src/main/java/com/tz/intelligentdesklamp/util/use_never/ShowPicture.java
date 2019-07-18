package com.tz.intelligentdesklamp.util.use_never;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowPicture {
    //获取图片资源图片
    public static byte[] getBitmap(String path,String token,String queryBackgroundsUrl){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",token)
                .url(path)
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
}

