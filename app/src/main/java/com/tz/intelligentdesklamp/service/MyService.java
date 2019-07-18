package com.tz.intelligentdesklamp.service;

/**
 * 向实时数据展示传递数据
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.bean.GetBaseData;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyService extends Service {

    //待传递数据
    double score;//评分
    int grade;//等级
    int totalTime;//学习时长
    double accuracy;//坐姿正确率
    //关于各种坐姿
    int a;//正确
    int b;//左手错误放置
    int c;//右手错误放置
    int d;//头左偏
    int e;//头右偏
    int f;//身体倾斜
    int g;//趴下
//    GetBaseData getBaseData;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//在里面开启一个线程执行获取实时数据的操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //网络请求
                HttpUtil.sendOkHttpRequestWithToken(InfoSave.getGetBaseDataUrl(), getApplicationContext(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData=response.body().string();
                        //解析
                        Gson gson=new Gson();
                        GetBaseData getBaseData=gson.fromJson(responseData,new TypeToken<GetBaseData>(){}.getType());
                        int code=getBaseData.getCode();
                        if (code==0){
                            score=getBaseData.getData().getBaseDataViewObject().getScore();//评分
                            grade=getBaseData.getData().getBaseDataViewObject().getGrade();//等级
                            totalTime=getBaseData.getData().getBaseDataViewObject().getTotalTime();//学习时长
                            accuracy=getBaseData.getData().getBaseDataViewObject().getAccuracy();//坐姿正确率
                            //关于各种坐姿
                            a=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getA();//正确
                            b=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getB();//左手错误放置
                            c=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getC();//右手错误放置
                            d=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getD();//头左偏
                            e=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getE();//头右偏
                            f=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getF();//身体倾斜
                            g=getBaseData.getData().getBaseDataViewObject().getSittingPostureStatistics().getG();//趴下

                        }
                    }
                });
                //接收处理数据
                //发出广播
                Intent intentOfStartNowData=new Intent("com.tz.intelligentdesklamp.NOW_DATA");
                int value=111;
//                int abc=getBaseData.getData().getBaseDataViewObject().getGrade();
                intentOfStartNowData.putExtra("value",value);
                intentOfStartNowData.putExtra("score",String.valueOf(score));//double记得转换
                intentOfStartNowData.putExtra("grade",grade);
                intentOfStartNowData.putExtra("totalTime",totalTime);
                intentOfStartNowData.putExtra("accuracy",String.valueOf(accuracy));
                intentOfStartNowData.putExtra("a",a);
                intentOfStartNowData.putExtra("b",b);
                intentOfStartNowData.putExtra("c",c);
                intentOfStartNowData.putExtra("d",d);
                intentOfStartNowData.putExtra("e",e);
                intentOfStartNowData.putExtra("f",f);
                intentOfStartNowData.putExtra("g",g);
                Log.d("MyService", "run 待传递数据："+score+" "+grade+" "+totalTime+" "+accuracy+" "+a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g);
                sendBroadcast(intentOfStartNowData);
            }
        }).start();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int halfAnHour=60*60*1000;//假设一小时获取一次
//        int halfAnHour=2*1000;//假设两秒获取一次
        long triggerAtTime=SystemClock.elapsedRealtime()+halfAnHour;
        Intent i=new Intent(this,MyService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }
}
