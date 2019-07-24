package com.tz.intelligentdesklamp.activity.datafragment;

/**
 * 坐姿数据
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.GetSittingPostureData;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;
import com.tz.intelligentdesklamp.util.chart.PieChartManagger;
import com.tz.intelligentdesklamp.util.use.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataPosture extends BaseDataActivity implements View.OnClickListener {
    String TAG="DataPosture";

    private ImageView image_data_posture_back;//返回
    private Button bt_data_posture_date_cut;//日期后退
    private Button bt_data_posture_date_add;//日期前进
    private TextView tx_data_posture_date;//显示日期
    private PieChart pie_chat_data_posture;//饼状图
    private TextView tx_data_posture_accuracy;//正确率
    private TextView tx_data_posture_average;//平均分
    private ImageView star11,star12,star13,star14,star15;
    ImageView image_posture_nodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_posture);

        image_data_posture_back=(ImageView) findViewById(R.id.image_data_posture_back);//返回
        bt_data_posture_date_cut=(Button) findViewById(R.id.bt_data_posture_date_cut);//日期加减
        bt_data_posture_date_add=(Button) findViewById(R.id.bt_data_posture_date_add);
        tx_data_posture_date=(TextView) findViewById(R.id.tx_data_posture_date);//日期显示
        pie_chat_data_posture=(PieChart)findViewById(R.id.pie_chat_data_posture);//饼状图
        tx_data_posture_accuracy=(TextView)findViewById(R.id.tx_data_posture_accuracy);//三项指标
        tx_data_posture_average=(TextView)findViewById(R.id.tx_data_posture_average);
        star11=(ImageView)findViewById(R.id.star11);
        star12=(ImageView)findViewById(R.id.star12);
        star13=(ImageView)findViewById(R.id.star13);
        star14=(ImageView)findViewById(R.id.star14);
        star15=(ImageView)findViewById(R.id.star15);
        image_posture_nodata=(ImageView)findViewById(R.id.image_posture_nodata);

        /**
         * 界面内容初始布置
         */
        tx_data_posture_date.setText(getDate());//当前日期
        postureInitdata(InfoSave.getGetSittingPostureDataUrl(),getContext(),getDate());//所有数据

        /**
         * 根据时间发起网络请求
         * 再布置内容
         */
        //日期加减按钮点击事件
        image_data_posture_back.setOnClickListener(this);
        bt_data_posture_date_cut.setOnClickListener(this);
        bt_data_posture_date_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {//手动调整日期，点击发送网络请求
        String requestDateText=null;
        switch (view.getId()){
            case R.id.image_data_posture_back:
                finish();
                break;
            case R.id.bt_data_posture_date_cut://日期减
                requestDateText=getCutRequestDateText(tx_data_posture_date.getText().toString());
                //发出请求,继续查询上一周,并且更新UI
                postureInitdata(InfoSave.getGetSittingPostureDataUrl(),getContext(),requestDateText);
                //处理数据弹出反馈
                tx_data_posture_date.setText(requestDateText);
                Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_data_posture_date_add://不能超过当天日期
                if (tx_data_posture_date.getText().toString().equals(getDate())){
                    Toast.makeText(getContext(),"无法查询当前的下一周。",Toast.LENGTH_SHORT).show();
                }else {
                    requestDateText=getAddRequestDateText(tx_data_posture_date.getText().toString());//获取请求日期
                    //发出请求,继续查询下一周，并且更新UI
                    postureInitdata(InfoSave.getGetStudyTimeDataUrl(),getContext(),requestDateText);
                    //处理数据弹出反馈
                    tx_data_posture_date.setText(requestDateText);
                    Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //根据当前日期发起网络请求获取当前日期的统计数据
    public void postureInitdata(String url, final Context context, String dateText){

        final RequestBody requestBody=new FormBody.Builder()
                .add("date",dateText)
                .build();

        HttpUtil.sendOkHttpRequestWithTokenAndBody(url,getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"服务器故障！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.d(TAG, "onResponse: "+responseData);
                if (response.code()==200){//如果正常返回开始解析
                    Gson gson=new Gson();
                    GetSittingPostureData getSittingPostureData=gson.fromJson(responseData, new TypeToken<GetSittingPostureData>(){}.getType());
                    if (getSittingPostureData.getCode()==0){
                        final float accuracy=getSittingPostureData.getData().getSittingPostureData().getAccuracy();
                        final float average=getSittingPostureData.getData().getSittingPostureData().getAverage();
                        final int grade=getSittingPostureData.getData().getSittingPostureData().getGrade();
                        final int a=getSittingPostureData.getData().getSittingPostureData().getPostures().getA();
                        final int b=getSittingPostureData.getData().getSittingPostureData().getPostures().getB();
                        final int c=getSittingPostureData.getData().getSittingPostureData().getPostures().getC();
                        final int d=getSittingPostureData.getData().getSittingPostureData().getPostures().getD();
                        final int e=getSittingPostureData.getData().getSittingPostureData().getPostures().getE();
                        final int f=getSittingPostureData.getData().getSittingPostureData().getPostures().getF();
                        final int g=getSittingPostureData.getData().getSittingPostureData().getPostures().getG();
                        Log.d(TAG, "onResponse: "+"accuracy "+accuracy+" average "+average+" grade "+grade+""+" a "+a+" b "+b+" c "+c+" d "+d+" e "+e+" f "+f+" g "+g);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //正确率与平均分的内容设置
                                tx_data_posture_accuracy.setText(String.valueOf(accuracy));
                                tx_data_posture_average.setText(String.valueOf(average));
                                if (a<=0&&b<=0&&c<=0&&d<=0&&e<=0&&f<=0&&g<=0){
                                    pie_chat_data_posture.setVisibility(View.GONE);
                                    Glide.with(DataPosture.this).load(R.drawable.nodata).into(image_posture_nodata);
                                }else{
                                    pie_chat_data_posture.setVisibility(View.VISIBLE);
                                    showRingPieChart(a,b,c,d,e,f,g);
                                }
                                Utils.setGradeShow(grade,star11,star12,star13,star14,star15);
//                                showRingPieChart(1,4,7,22,7,9,9);
                            }
                        });
                    }

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"服务器故障！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //展示饼状图
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
        colors.add(Color.parseColor("#6785f2"));
        colors.add(Color.parseColor("#F7F709"));
        colors.add(Color.parseColor("#FF8F44"));
        colors.add(Color.parseColor("#FF44FF"));
        colors.add(Color.parseColor("#ED57ED"));
        colors.add(Color.parseColor("#BBBBBB"));
        colors.add(Color.parseColor("#FF9277"));

        PieChartManagger pieChartManagger=new PieChartManagger(pie_chat_data_posture);
        pieChartManagger.showRingPieChart(yvals,colors);
    }

}
