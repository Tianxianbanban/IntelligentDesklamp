package com.tz.intelligentdesklamp.util.use;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.bean.my_info_tosave.TaskAndTime;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static org.litepal.LitePalApplication.getContext;

/**
 * 与任务列表与番茄钟执行数据变动有关
 */

public class TaskAndTimeUtil {

//    //取出
//    public static List<TaskAndTime> getTaskAndTimes(Context context,String TAG){
//        String items =getContext().getSharedPreferences("todoList_data", MODE_PRIVATE).getString("todoList", "data_null");
//        Log.d(TAG, "getTaskAndTimes: "+items);
//        Gson gson=new Gson();
//        return gson.fromJson(items,new TypeToken<List<TaskAndTime>>(){}.getType());
//    }

    //存储
    public static void putTaskAndTimes(Context context,List<TaskAndTime> taskAndTimes){
        Gson gson=new Gson();
        String itemsToSavea=gson.toJson(taskAndTimes);
        SharedPreferences.Editor editor=context.getSharedPreferences("todoList_data",MODE_PRIVATE).edit();
        editor.putString("todoList",itemsToSavea);
        editor.apply();
    }
}
