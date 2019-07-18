package com.tz.intelligentdesklamp.activity.datafragment;

/**
 * 与统计数据有关
 * 坐姿
 * 评分
 * 学习
 * 专注
 *
 * 获取作为请求参数的日期
 * 在主线程中根据服务对用户做出反馈
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tz.intelligentdesklamp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BaseDataActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将状态栏与标题栏融合
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public Context getContext(){
        return this;
    }

    //获取当前日期
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        return date;
    }
    //获取当前日期星期几
    public int getWeekDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return w;
    }
    //获取当前日期星期几
    public int getRequestWeekDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return w;
    }
    //日期减x日
    public String cutDate(String nowtimetext,int cutDay){
        //获取现有的日期文本
        //再次确定日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //文本转日期
        try {
            Date dateFromText=simpleDateFormat.parse(nowtimetext);
            //日期后退
            Calendar specialDateCalenda = Calendar.getInstance();
            specialDateCalenda.setTime(dateFromText); //注意在此处将 specialDate 的值设置为特定日期，即当前文本转化过来的日期
            specialDateCalenda.add(Calendar.DAY_OF_YEAR, -cutDay); //所谓特定时间的cutDay天前
            Date thenDate=specialDateCalenda.getTime();
            String datetext=simpleDateFormat.format(thenDate);
            return datetext;
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this,"发生错误！",Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    //日期加x日
    public String addDate(String nowtimetext,int addDay){
        //获取现有的日期文本
        //再次确定日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //文本转日期
        try {
            Date dateFromText=simpleDateFormat.parse(nowtimetext);
            //日期后退
            Calendar specialDateCalenda = Calendar.getInstance();
            specialDateCalenda.setTime(dateFromText); //注意在此处将 specialDate 的值设置为特定日期，即当前文本转化过来的日期
            specialDateCalenda.add(Calendar.DAY_OF_YEAR, addDay); //所谓特定时间的addDay天后
            Date thenDate=specialDateCalenda.getTime();
            String datetext=simpleDateFormat.format(thenDate);
            return datetext;
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this,"发生错误！",Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    //获取查询上周请求参数日期
    public String getCutRequestDateText(String nowDateText){
        String requestDateText=null;
        //获取当前图标显示对应的日期
//        String nowDateTextCut=tx_data_learning_date.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //获取请求参数日期
            Date nowDate=simpleDateFormat.parse(nowDateText);
            int weekDay=getRequestWeekDay(nowDate);
            if (weekDay==0){
                requestDateText=cutDate(nowDateText,7);//如果本周已经是周日，直接减7天
            }else{
                requestDateText=cutDate(nowDateText,weekDay);
            }

            return  requestDateText;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("BaseDataActivity", " getCutRequestDateText: ");
            return nowDateText;
        }
    }
    //获取查询下周请求参数日期
    public String getAddRequestDateText(String nowDateText){
        String requestDateText=null;
        //获取当前图标显示对应的日期
//        String nowDateTextCut=tx_data_learning_date.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //获取请求参数日期
            Date nowDate=simpleDateFormat.parse(nowDateText);
            int weekDay=getRequestWeekDay(nowDate);
            requestDateText=addDate(nowDateText,7);//直接加上七天得到下周周日
            return  requestDateText;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("BaseDataActivity", " getAddRequestDateText: ");
            return nowDateText;
        }
    }



    /**
     * 在主线程中向用户返回服务信息
     */
    public void showServiceInfo(final Context context, final String content){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
