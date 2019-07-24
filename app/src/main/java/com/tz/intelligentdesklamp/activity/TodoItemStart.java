package com.tz.intelligentdesklamp.activity;

/**
 * 番茄钟与任务执行界面
 */

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.GetBegin;
import com.tz.intelligentdesklamp.bean.GetEnd;
import com.tz.intelligentdesklamp.bean.my_info_tosave.TaskAndTime;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;
import com.tz.intelligentdesklamp.util.use.TaskAndTimeUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TodoItemStart extends AppCompatActivity implements View.OnClickListener{
    String TAG="TodoItemStart";
    String[] posture={"正确坐姿","左手错误放置","右手错误放置","头左偏","头右偏","身体倾斜","趴下"};

    //关于数据刷新
    String items;
    List<TaskAndTime> taskAndTimes;
    Gson gson=new Gson();
    int time_cut=-1;

    int wrongCount;//错误次数
    float effectiveness;//效率
//    float effectiveTime;//有效工作时间
    int effectiveTime;//有效工作时间 需要的是整数
    int frequent;//最频繁的错误姿势

    TextView tx_todo_item_start_back;//返回
    Button tx_todo_item_start_select;//选择番茄钟个数
    TextView tx_todoitemstart_time;
    TextView tx_efficiency_start_task;
    RelativeLayout rl_todo_start;//整个布局
    FrameLayout fl_todo_start_startdoing;//开始任务
    RelativeLayout rl_todo_start_ing;//点击开始
    AVLoadingIndicatorView av_todo_item_start_ing;//进度条
    int timeInit=0;//预估时间
    int position;//位置
    static int limitSec;
    static int limitSecMili;//剩余秒数
    static int hour;
    static int min;
    static int seconds;
    Thread timeThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(limitSecMili>=0){
                if (timeThread.isInterrupted()){
                    break;
                }
                seconds=limitSecMili%60;//显示的秒数
                min=(limitSecMili/60)%60;//显示分钟数
                hour=limitSecMili/60/60;//显示小时数

                Log.d(TAG, "倒计时 CountDown remians "+ hour+":"+min+":"+seconds);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tx_todoitemstart_time.setText(
                                (String.valueOf(hour).length()>1?String.valueOf(hour):("0"+String.valueOf(hour)))+ ":"
                                        +((String.valueOf(min).length()>1)?String.valueOf(min):("0"+String.valueOf(min)))+":"
                                        +((String.valueOf(seconds).length()>1)?String.valueOf(seconds):("0"+String.valueOf(seconds))));
                        rl_todo_start_ing.setVisibility(View.GONE);
                        tx_efficiency_start_task.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                });
                try {
//                    TimeUnit.SECONDS.sleep(1);
                    Thread.sleep(1000);
                    limitSecMili--;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            /**
             * 倒计时结束后，
             * 网络请求获取结束时的数据
             * 弹出对话框
             * 并且询问用户是否增加执行任务的时长
             */

            //番茄钟结束网络请求
            String msg=getSharedPreferences("todoitem_start_data", MODE_PRIVATE).getString("key",null);//取出key
            final RequestBody requestBody=new FormBody.Builder().add("msg",msg).build();
            HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getEndUrl(), TodoItemStart.this, requestBody,new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData=response.body().string();
                    Log.d(TAG, "onResponse: 番茄钟结束返回："+responseData);
                    /**
                     * 这个时候返回的是后台对这段时间学习情况的评估
                     */
                    SharedPreferences.Editor editor=getSharedPreferences("getend_json",MODE_PRIVATE).edit();
                    editor.putString("getend",responseData);
                    editor.apply();
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showCenterMenu();
                }
            });


        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item_start);

        initViewAndData();//初始化界面与数值

        tx_todo_item_start_back.setOnClickListener(this);//返回
        tx_todo_item_start_select.setOnClickListener(this);//选择番茄钟个数


        /**
         * 开始执行任务
         * 变换界面
         * 网络请求开始任务，获取key值
         * 倒计时
         */
        fl_todo_start_startdoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跟换界面
                rl_todo_start.setBackgroundColor(Color.parseColor("#493F45"));
                tx_todo_item_start_back.setVisibility(View.GONE);//隐藏返回键
                tx_todo_item_start_select.setVisibility(View.GONE);//隐藏番茄钟选择项
                //开始任务即开始计时
                //!!!!注意强行退出时要关闭线程
                timeThread.start();
                //网络请求
                HttpUtil.sendOkHttpRequestWithToken(InfoSave.getBeginUrl(), TodoItemStart.this, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData=response.body().string();
                        Log.d(TAG, "onResponse: 番茄钟开始返回："+responseData);
                        try{
                            Gson gson=new Gson();
                            GetBegin getBegin=gson.fromJson(responseData,GetBegin.class);
                            Log.d(TAG, "onResponse: 番茄钟返回key="+getBegin.getData().getKey());
                            SharedPreferences.Editor editor=getSharedPreferences("todoitem_start_data",MODE_PRIVATE).edit();
                            editor.putString("key",getBegin.getData().getKey());
                            editor.apply();
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: json解析异常");
                        }
                    }
                });
            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tx_todo_item_start_back:
                finish();//直接返回了
                break;
            case R.id.tx_todo_item_start_select:
                //弹出顶部弹框
                show();
                break;
            case R.id.tx_top_menu_add://加一个番茄钟
                limitSec+=25;
                if (limitSec>250){
                    String[] tips={"要注意劳逸结合呀小宝贝！","不能再加番茄钟了！","休息休息吧~~~","不行！会累坏的！"};
                    Toast.makeText(this,tips[(new Random()).nextInt(tips.length)],Toast.LENGTH_SHORT).show();
                    limitSec-=25;
                    break;
                }else{
                    dealLimitSec(limitSec);
                }
                break;
            case R.id.tx_top_menu_cut:
                //先获取当前番茄钟时长
                limitSec-=25;
                if (limitSec<0){
                    limitSec+=25;
                    break;
                }else{
                    dealLimitSec(limitSec);
                }
                break;
            case R.id.tx_center_menu_finishconfirm://确认累计一次有效工作时间
                Toast.makeText(TodoItemStart.this,"点击有效",Toast.LENGTH_SHORT).show();
                //根据位置进行修改
                time_cut=timeInit-effectiveTime;
                taskAndTimes.get(position).setTime(String.valueOf(time_cut<=0?0:time_cut));
                TaskAndTimeUtil.putTaskAndTimes(TodoItemStart.this,taskAndTimes);//更改一次数据
                finish();
                break;
            case R.id.tx_center_menu_continue://累计实际工作时间
                Toast.makeText(TodoItemStart.this,"点击标准",Toast.LENGTH_SHORT).show();
                time_cut=timeInit-limitSec;
                taskAndTimes.get(position).setTime(String.valueOf(time_cut<=0?0:time_cut));
                TaskAndTimeUtil.putTaskAndTimes(TodoItemStart.this,taskAndTimes);
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        timeThread.interrupt();//停止倒计时多线程
        super.onDestroy();
    }




    //番茄钟个数增减对话框
    private void show(){
        View view = LayoutInflater.from(TodoItemStart.this).inflate(R.layout.top_menu_select, null);
        Dialog dialog = new Dialog(TodoItemStart.this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setContentView(view);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();

        //关于选项
        TextView tx_top_menu_add=view.findViewById(R.id.tx_top_menu_add);
        TextView tx_top_menu_cut=view.findViewById(R.id.tx_top_menu_cut);
        tx_top_menu_add.setOnClickListener(this);
        tx_top_menu_cut.setOnClickListener(this);

    }

    //每次倒计时结束后的弹框，未锁频息屏时
    private void showCenterMenu(){
        View view = LayoutInflater.from(TodoItemStart.this).inflate(R.layout.center_menu_confirm, null);
        Dialog dialog = new Dialog(TodoItemStart.this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setContentView(view);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
//        dialog.show();

        TextView tx_center_menu_info=view.findViewById(R.id.tx_center_menu_info);//反馈信息
        TextView tx_center_menu_continue=view.findViewById(R.id.tx_center_menu_continue);//延长时间继续
        TextView tx_center_menu_finishconfirm=view.findViewById(R.id.tx_center_menu_finishconfirm);//确认完成任务
        tx_center_menu_continue.setOnClickListener(this);
        tx_center_menu_finishconfirm.setOnClickListener(this);

        String info_content;//反馈信息
        String getend =getSharedPreferences("getend_json", MODE_PRIVATE)
                .getString("getend", "data_null");
        try{
            Gson gson=new Gson();
            GetEnd getEnd=gson.fromJson(getend,GetEnd.class);
            wrongCount=getEnd.getData().getFeedback().getWrongCount();//错误次数
            effectiveness=getEnd.getData().getFeedback().getEffectiveness();//效率
            effectiveTime=(int) getEnd.getData().getFeedback().getEffectiveTime();//有效工作时间
            frequent=getEnd.getData().getFeedback().getFrequent();//最频繁的错误姿势
            info_content="你已经学习25分钟啦：\n\n\t\t在这段时间里，你出现了__"+ wrongCount+ "__次不正确的坐姿哦！" +
                    "你最频繁的错误坐姿是__"+posture[frequent]+"__。" +
                    " 后台根据你的坐姿变化情况分析得出你的工作效率为__"+effectiveness+"__," +
                    "折合有效时间是__"+effectiveTime+"__分钟。" +
                    "要累计任务时间吗?";
            tx_center_menu_info.setText(info_content);


        }catch (Exception e){

        }

        dialog.show();


    }

    //正确显示剩余时间
    private void dealLimitSec(int limitSec){
        String textDealLimitSec=null;
        limitSecMili=limitSec*60;
        seconds=limitSecMili%60;//显示的秒数
        min=(limitSecMili/60)%60;//显示分钟数
        hour=limitSecMili/60/60;//显示小时数
        tx_todoitemstart_time.setText((String.valueOf(hour).length()>1?String.valueOf(hour):("0"+String.valueOf(hour)))+ ":"
                +((String.valueOf(min).length()>1)?String.valueOf(min):("0"+String.valueOf(min)))+":"
                +((String.valueOf(seconds).length()>1)?String.valueOf(seconds):("0"+String.valueOf(seconds))));
    }

    private void initViewAndData(){
        Intent intent=getIntent();
        final String taskAtNow=intent.getStringExtra("task");//取出列表项目当前任务
        String time=intent.getStringExtra("time");//取出预估时间
        timeInit=Integer.parseInt(time);//预估时间
        position=intent.getIntExtra("position",-1);


        rl_todo_start=(RelativeLayout)findViewById(R.id.rl_todo_start);//整个布局
        tx_todo_item_start_back=(TextView)findViewById(R.id.tx_todo_item_start_back);//返回
        tx_todo_item_start_select=(Button) findViewById(R.id.tx_todo_item_start_select);//选择番茄钟个数
        fl_todo_start_startdoing=(FrameLayout)findViewById(R.id.fl_todo_start_startdoing);//开始任务
        rl_todo_start_ing=(RelativeLayout)findViewById(R.id.rl_todo_start_ing);//点击开始
        tx_efficiency_start_task=(TextView)findViewById(R.id.tx_efficiency_start_task) ;//当前任务
        tx_todoitemstart_time=(TextView)findViewById(R.id.tx_todoitemstart_time);
        //初始番茄钟时间
        limitSec=1;
        limitSecMili=limitSec*60;
        seconds=limitSecMili%60;//显示的秒数
        min=(limitSecMili/60)%60;//显示分钟数
        hour=limitSecMili/60/60;//显示小时数

        tx_efficiency_start_task.setText(taskAtNow+"…");
        tx_todoitemstart_time.setText((String.valueOf(hour).length()>1?String.valueOf(hour):("0"+String.valueOf(hour)))+ ":"
                +((String.valueOf(min).length()>1)?String.valueOf(min):("0"+String.valueOf(min)))+":"
                +((String.valueOf(seconds).length()>1)?String.valueOf(seconds):("0"+String.valueOf(seconds))));

        //将存储任务取出
        items =getSharedPreferences("todoList_data", MODE_PRIVATE).getString("todoList", "data_null");
        taskAndTimes=gson.fromJson(items,new TypeToken<List<TaskAndTime>>(){}.getType());

    }
}
