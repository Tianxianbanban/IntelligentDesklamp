package com.tz.intelligentdesklamp.util.use;

/**
 * 获取图片资源byte数组
 * 展示进度条
 * 设置等级星标显示
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.TodoListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    /**
     * 获取图片资源图片
     */
    public static byte[] getBitmap(String url,String token){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("Authorization",token)
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

    /**
     * 等级星标设置
     */
    public static void setGradeShow(int grade, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5){
        if (grade>10){//如果后端数据出错
            grade=10;
        }else if (grade<0){
            return;
        }
        List<ImageView> stars=new ArrayList<>();
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);
        for (int i=0;i<stars.size();i++){//等级显示归零
            stars.get(i).setImageResource(R.drawable.zero_star);
        }
        int fullBright=grade/2;
        int halfBright=grade-fullBright*2;
        for (int i=0;i<fullBright;i++){//重新设置等级显示
            stars.get(i).setImageResource(R.drawable.full_start);
        }
        if (halfBright!=0){
            stars.get(fullBright).setImageResource(R.drawable.half_star);
        }
    }

}
