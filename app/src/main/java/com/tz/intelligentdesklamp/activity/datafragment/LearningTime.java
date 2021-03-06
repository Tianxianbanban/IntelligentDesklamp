package com.tz.intelligentdesklamp.activity.datafragment;

/**
 * 学习时长
 * 请求参数为每周周日即每周第一天，始于请求日期
 * 查询下周时间无法超过本周，截至当天
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
import com.github.mikephil.charting.charts.LineChart;;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.GetStudyTimeData;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;
import com.tz.intelligentdesklamp.util.chart.LineChartManager;
import com.tz.intelligentdesklamp.util.use.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LearningTime extends BaseDataActivity implements View.OnClickListener{
    String TAG="LearningTime";

    private ImageView image_data_learning_back;//返回
    private Button bt_data_learning_date_cut;//查询上周
    private Button bt_data_learning_date_add;//查询下周
    private TextView tx_data_learning_date;//日期显示
    private LineChart lineChart;//折线图
    private ImageView image_learning_nodata;
    private TextView tx_data_learning_timelength;//总时长
    private TextView tx_data_learning_average;//平均值
    private ImageView star1,star2,star3,star4,star5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_time);


        image_data_learning_back=(ImageView) findViewById(R.id.image_data_learning_back);//返回
        bt_data_learning_date_cut=(Button)findViewById(R.id.bt_data_learning_date_cut);
        bt_data_learning_date_add=(Button)findViewById(R.id.bt_data_learning_date_add);
        tx_data_learning_date=(TextView)findViewById(R.id.tx_data_learning_date);
        lineChart=(LineChart)findViewById(R.id.linechart_data_learning);//折线图
        image_learning_nodata=(ImageView)findViewById(R.id.image_learning_nodata);
        tx_data_learning_timelength=(TextView)findViewById(R.id.tx_data_learning_timelength);//学习时长
        tx_data_learning_average=(TextView)findViewById(R.id.tx_data_learning_average);//平均值
        star1=(ImageView)findViewById(R.id.star1);
        star2=(ImageView)findViewById(R.id.star2);
        star3=(ImageView)findViewById(R.id.star3);
        star4=(ImageView)findViewById(R.id.star4);
        star5=(ImageView)findViewById(R.id.star5);


        tx_data_learning_date.setText(getDate());//显示当天日期
        learningInitdata(getDate());


        image_data_learning_back.setOnClickListener(this);//返回
        bt_data_learning_date_cut.setOnClickListener(this);//查询上周
        bt_data_learning_date_add.setOnClickListener(this);//查询下周
    }

    //展示折线图
    private void showAlone(List<Float> data) {
        LineChartManager lineChartManager=new LineChartManager(lineChart);

        List<Float> xAxisValues=new ArrayList<>();
        List<Float> yAxisValues=new ArrayList<>();
        xAxisValues.add(0.0f);
        xAxisValues.add(1.0f);
        xAxisValues.add(2.0f);
        xAxisValues.add(3.0f);
        xAxisValues.add(4.0f);
        xAxisValues.add(5.0f);
        xAxisValues.add(6.0f);
        for (int i=0;i<data.size();i++){
            yAxisValues.add(data.get(i));
        }
//        lineChartManager.showLineChart(xAxisValues,yAxisValues,"", Color.parseColor("#da6268"));
        lineChartManager.showLineChart(xAxisValues,yAxisValues,"", Color.BLUE);
    }

    @Override
    public void onClick(View view) {
        String requestDateText=null;
        switch (view.getId()){
            case R.id.image_data_learning_back://返回
                finish();
                break;
            case R.id.bt_data_learning_date_cut://查询上周
                requestDateText=getCutRequestDateText(tx_data_learning_date.getText().toString());
                //发出请求,继续查询上一周,并且更新UI
                learningInitdata(requestDateText);
                //处理数据弹出反馈
                tx_data_learning_date.setText(requestDateText);
                Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_data_learning_date_add://查询下周
                if (tx_data_learning_date.getText().toString().equals(getDate())){
                    Toast.makeText(getContext(),"无法查询当前的下一周。",Toast.LENGTH_SHORT).show();
                }else {
                    requestDateText=getAddRequestDateText(tx_data_learning_date.getText().toString());//获取请求日期
                    //发出请求,继续查询下一周，并且更新UI
                    learningInitdata(requestDateText);
                    //处理数据弹出反馈
                    tx_data_learning_date.setText(requestDateText);
                    Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //根据当前日期发起网络请求获取当前日期的统计数据
    public void learningInitdata(final String dateText){

        final RequestBody requestBody=new FormBody.Builder()
                .add("date",dateText)
                .build();

        HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getStudyTimeDataUrl,getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showServiceInfo(getContext(),"服务器故障");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.d(TAG, "请求学习时间onResponse: "+responseData);
                if (response.code()==200){//如果正常返回开始解析
                    try{
                        Gson gson=new Gson();
                        GetStudyTimeData getStudyTimeData=gson.fromJson(responseData, new TypeToken<GetStudyTimeData>(){}.getType());
                        if (getStudyTimeData.getCode()==0){
                            final List<Float> times=getStudyTimeData.getData().getStudyTimeData().getTime();
                            final Float totalTime=getStudyTimeData.getData().getStudyTimeData().getTotalTime();
                            final Float average=getStudyTimeData.getData().getStudyTimeData().getAverage();
                            final int grade=getStudyTimeData.getData().getStudyTimeData().getGrade();
                            Log.d(TAG, "学习时间: GetStudyTimeData-----"+" dateText"+dateText+" times.size()="+times.size()+" totalTime="+totalTime+" average="+average+" grade="+grade);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean is=false;
                                    for (Float data:times) {
                                        if (data>0){
                                            is=true;
                                        }
                                    }
                                    if (is){
                                        lineChart.setVisibility(View.VISIBLE);
                                        image_learning_nodata.setVisibility(View.GONE);
                                        showAlone(times);//折线图
                                    }else{
                                        lineChart.setVisibility(View.GONE);
                                        image_learning_nodata.setVisibility(View.VISIBLE);
                                        Glide.with(LearningTime.this).load(R.drawable.nodata).into(image_learning_nodata);
                                    }

                                    tx_data_learning_timelength.setText(String.valueOf(totalTime));//总时长
                                    tx_data_learning_average.setText(String.valueOf(average));//平均值
                                    Utils.setGradeShow(grade,star1,star2,star3,star4,star5);//等级设置

                                }
                            });
                        }
                    }catch (Exception e){

                    }

                }else{
                    showServiceInfo(getContext(),"服务器故障");
                }
            }
        });
    }

}
