package com.tz.intelligentdesklamp.fragment.home;

/**
 * 显示实时数据
 * 服务接收数据，实时获取(一个小时更新一次）
 * 广播更新UI
 *
 * 动画切换数字
 * 饼状图默认显示文字提示
 * 数据来源非零显示
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.service.MyService;
import com.tz.intelligentdesklamp.util.chart.PieChartManagger;
import com.tz.intelligentdesklamp.util.use.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NowFragment extends BaseFragment implements View.OnClickListener{

    private IntentFilter intentFilter;//动态注册接收广播
    private NowDataChangedReceiver nowDataChangedReceiver;

    //待接收数据
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

    private TextView tx_now_score;//评分
    private ImageView grade_star_1,grade_star_2,grade_star_3,grade_star_4,grade_star_5;//等级显示星标
    private LinearLayout time;//工作时长
    private TextView tx_now_time;
    private LinearLayout posture;//坐姿正确率
    private TextView tx_now_posture;
    private RelativeLayout details;//坐姿详情

    private PieChart pie_chat_now;

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.fragment_now, null);

        //关于广播
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.tz.intelligentdesklamp.NOW_DATA");
        nowDataChangedReceiver =new NowDataChangedReceiver();
        getContext().registerReceiver(nowDataChangedReceiver,intentFilter);//进行注册，要记得取消注册

        tx_now_score=(TextView)view.findViewById(R.id.tx_now_score);//评分
        grade_star_1=(ImageView)view.findViewById(R.id.grade_star_1);//关于等级的星标
        grade_star_2=(ImageView)view.findViewById(R.id.grade_star_2);
        grade_star_3=(ImageView)view.findViewById(R.id.grade_star_3);
        grade_star_4=(ImageView)view.findViewById(R.id.grade_star_4);
        grade_star_5=(ImageView)view.findViewById(R.id.grade_star_5);
        time=(LinearLayout) view.findViewById(R.id.time);//时长
        tx_now_time=(TextView)view.findViewById(R.id.tx_now_time);
        posture=(LinearLayout)view.findViewById(R.id.posture);//正确率
        tx_now_posture=(TextView) view.findViewById(R.id.tx_now_posture);
        details=(RelativeLayout)view.findViewById(R.id.details);//详情

        pie_chat_now=(PieChart) view.findViewById(R.id.pie_chat_now);//饼状图

        time.setOnClickListener(this);
        posture.setOnClickListener(this);
        details.setOnClickListener(this);


        //启动定时服务
        Intent intentOfInfo=new Intent(getContext(),MyService.class);
        getContext().startService(intentOfInfo);

        SharedPreferences sharedPreferences=getContext().getSharedPreferences("now_data",MODE_PRIVATE);
        int a=sharedPreferences.getInt("a",0);
        int b=sharedPreferences.getInt("b",0);
        int c=sharedPreferences.getInt("c",0);
        int d=sharedPreferences.getInt("d",0);
        int e=sharedPreferences.getInt("e",0);
        int f=sharedPreferences.getInt("f",0);
        int g=sharedPreferences.getInt("g",0);
//        showRingPieChart(a,b,c,d,e,f,g);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.time:
                Toast.makeText(getContext(),"点击工作时长",Toast.LENGTH_SHORT).show();
                break;
            case R.id.posture:
                Toast.makeText(getContext(),"点击坐姿正确率",Toast.LENGTH_SHORT).show();
                break;
            case R.id.details:
                Toast.makeText(getContext(),"查看60mins坐姿情况",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void showRingPieChart(int a,int b,int c,int d,int e,int f,int g) {
        // 设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(a, "正确坐姿"));
        yvals.add(new PieEntry(b, "头左偏"));
        yvals.add(new PieEntry( c, "头右偏"));
        yvals.add(new PieEntry(d, "左手错误放置"));
        yvals.add(new PieEntry(e, "右手错误放置"));
        yvals.add(new PieEntry(f, "趴桌"));
        yvals.add(new PieEntry(g, "身体倾斜"));
        // 设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#A0F8E7"));
        colors.add(Color.parseColor("#99FFAD"));
        colors.add(Color.parseColor("#FFAD99"));
        colors.add(Color.parseColor("#DABFDA"));
        colors.add(Color.parseColor("#CCCCCC"));
        colors.add(Color.parseColor("#FFAD77"));
        colors.add(Color.parseColor("#FFAD77"));

        PieChartManagger pieChartManagger=new PieChartManagger(pie_chat_now);
        pieChartManagger.showRingPieChart(yvals,colors);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除广播接收
        getContext().unregisterReceiver(nowDataChangedReceiver);
    }

    class NowDataChangedReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {//每当服务获取一次数据，就会执行
//            Toast.makeText(getContext(),"接收到广播",Toast.LENGTH_SHORT).show();

            //接收数据
            //待传递数据
            String score=intent.getStringExtra("score");//评分
            int grade=intent.getIntExtra("grade",0);//等级
            int totalTime=intent.getIntExtra("totalTime",0);//学习时长
            String accuracy=intent.getStringExtra("accuracy");//坐姿正确率
            //关于各种坐姿
            int a=intent.getIntExtra("a",0);//正确
            int b=intent.getIntExtra("b",0);//左手错误放置
            int c=intent.getIntExtra("c",0);//右手错误放置
            int d=intent.getIntExtra("d",0);//头左偏
            int e=intent.getIntExtra("e",0);//头右偏
            int f=intent.getIntExtra("f",0);//身体倾斜
            int g=intent.getIntExtra("g",0);//趴下
            Log.d("Now_data:", "onReceive: score"+score+" grade"+grade+" totalTime"+totalTime+
                    " accuracy"+accuracy+" a"+a+" b"+b+" c"+c+" d"+d+" e"+e+" f"+f+" g"+g+" ");

            //数据简单处理
            DecimalFormat decimalFormat=new DecimalFormat("0.00%");//将正确率换成百分数
            String accuracyb=decimalFormat.format(Double.valueOf(accuracy));
            //UI变化
            tx_now_score.setText(score);//分数
            Utils.setGradeShow(grade,grade_star_1,grade_star_2,grade_star_3,grade_star_4,grade_star_5);//等级展示
            tx_now_time.setText(String.valueOf(totalTime));//学习时长
            tx_now_posture.setText(accuracyb);//坐姿正确率
            showRingPieChart(a,b,c,d,e,f,g);//饼状图展示
        }
    }

}
