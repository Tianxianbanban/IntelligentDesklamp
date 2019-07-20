package com.tz.intelligentdesklamp.activity;

/**
 * 番茄钟界面
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tz.intelligentdesklamp.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.TimeUnit;

public class TodoItemStart extends AppCompatActivity implements View.OnClickListener{
    TextView tx_todo_item_start_back;//返回
    Button tx_todo_item_start_select;//选择番茄钟个数
    TextView tx_todoitemstart_time;
    TextView tx_efficiency_start_task;
    RelativeLayout rl_todo_start;//整个布局
    FrameLayout fl_todo_start_startdoing;//开始任务
    RelativeLayout rl_todo_start_ing;//点击开始
    AVLoadingIndicatorView av_todo_item_start_ing;//进度条
    int limitSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item_start);

        Intent intent=getIntent();
        final String taskAtNow=intent.getStringExtra("task");//取出列表项目当前任务

        rl_todo_start=(RelativeLayout)findViewById(R.id.rl_todo_start);//整个布局
        tx_todo_item_start_back=(TextView)findViewById(R.id.tx_todo_item_start_back);//返回
        tx_todo_item_start_select=(Button) findViewById(R.id.tx_todo_item_start_select);//选择番茄钟个数
        fl_todo_start_startdoing=(FrameLayout)findViewById(R.id.fl_todo_start_startdoing);//开始任务
        rl_todo_start_ing=(RelativeLayout)findViewById(R.id.rl_todo_start_ing);//点击开始
        tx_efficiency_start_task=(TextView)findViewById(R.id.tx_efficiency_start_task) ;//当前任务
        tx_todoitemstart_time=(TextView)findViewById(R.id.tx_todoitemstart_time);
        limitSec=25;

        tx_efficiency_start_task.setText(taskAtNow+"…");
        tx_todoitemstart_time.setText(String.valueOf(limitSec));

        //返回
        tx_todo_item_start_back.setOnClickListener(this);
        //选择番茄钟个数
        tx_todo_item_start_select.setOnClickListener(this);


        //开始执行任务
        fl_todo_start_startdoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跟换界面
                rl_todo_start.setBackgroundColor(Color.parseColor("#493F45"));
                //开始任务即开始计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(limitSec > 0){
                            Log.d("CountDown", "remians "+ --limitSec +" min");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tx_todoitemstart_time.setText(String.valueOf(limitSec));
                                    rl_todo_start_ing.setVisibility(View.GONE);
                                    tx_efficiency_start_task.setTextColor(Color.parseColor("#FFFFFF"));

                                }
                            });
                            try {
                                TimeUnit.SECONDS.sleep(60);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tx_todo_item_start_back:
                finish();
                break;
            case R.id.tx_todo_item_start_select:
                
                break;
        }
    }
}
