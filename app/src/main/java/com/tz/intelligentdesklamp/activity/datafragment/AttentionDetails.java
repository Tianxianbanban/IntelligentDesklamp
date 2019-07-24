package com.tz.intelligentdesklamp.activity.datafragment;

/**
 * 专注情况
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
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.GetFocusData;
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

public class AttentionDetails extends BaseDataActivity implements View.OnClickListener {
    String TAG="AttentionDetails";

    private TextView tx_data_attention_back;
    private TextView tx_data_attention_date;
    private Button bt_data_attention_date_cut;
    private Button bt_data_attention_date_add;
    private LineChart linechart_data_attention;
    private TextView tx_data_attention_average;
    private TextView tx_data_attention_variance;
    private ImageView star31,star32,star33,star34,star35;
    private ImageView image_attention_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_details);

        tx_data_attention_back=(TextView)findViewById(R.id.tx_data_attention_back);
        tx_data_attention_date=(TextView)findViewById(R.id.tx_data_attention_date);
        bt_data_attention_date_cut=(Button)findViewById(R.id.bt_data_attention_date_cut);
        bt_data_attention_date_add=(Button)findViewById(R.id.bt_data_attention_date_add);
        linechart_data_attention=(LineChart)findViewById(R.id.linechart_data_attention);
        tx_data_attention_average=(TextView)findViewById(R.id.tx_data_attention_average);
        tx_data_attention_variance=(TextView)findViewById(R.id.tx_data_attention_variance);
        star31=(ImageView)findViewById(R.id.star11);
        star32=(ImageView)findViewById(R.id.star12);
        star33=(ImageView)findViewById(R.id.star13);
        star34=(ImageView)findViewById(R.id.star14);
        star35=(ImageView)findViewById(R.id.star15);
        image_attention_nodata=(ImageView)findViewById(R.id.image_attention_nodata);

        tx_data_attention_date.setText(getDate());
        attentionInitdata(InfoSave.getGetFocusDataUrl(),getContext(),getDate());

        tx_data_attention_back.setOnClickListener(this);
        bt_data_attention_date_cut.setOnClickListener(this);
        bt_data_attention_date_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String requestDateText=null;
        switch (view.getId()){
            case R.id.tx_data_attention_back:
                finish();
                break;
            case R.id.bt_data_attention_date_cut:
                requestDateText=getCutRequestDateText(tx_data_attention_date.getText().toString());
                //发出请求,继续查询上一周,并且更新UI
                attentionInitdata(InfoSave.getGetFocusDataUrl(),getContext(),requestDateText);
                //处理数据弹出反馈
                tx_data_attention_date.setText(requestDateText);
                Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_data_attention_date_add:
                if (tx_data_attention_date.getText().toString().equals(getDate())){
                    Toast.makeText(getContext(),"无法查询当前的下一周。",Toast.LENGTH_SHORT).show();
                }else {
                    requestDateText=getAddRequestDateText(tx_data_attention_date.getText().toString());//获取请求日期
                    //发出请求,继续查询下一周，并且更新UI
                    attentionInitdata(InfoSave.getGetFocusDataUrl(),getContext(),requestDateText);
                    //处理数据弹出反馈
                    tx_data_attention_date.setText(requestDateText);
                    Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //根据当前日期发起网络请求获取当前日期的统计数据
    public void attentionInitdata(String url, final Context context, String dateText){

        final RequestBody requestBody=new FormBody.Builder()
                .add("date",dateText)
                .build();

        HttpUtil.sendOkHttpRequestWithTokenAndBody(url,getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showServiceInfo(getContext(),"服务器故障");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.d(TAG, "onResponse: "+responseData);
                if (response.code()==200){//如果正常返回开始解析
                    Gson gson=new Gson();
                    GetFocusData getFocusData=gson.fromJson(responseData, new TypeToken<GetFocusData>(){}.getType());
                    if (getFocusData.getCode()==0){
                        final List<Float> focusDatas=getFocusData.getData().getFocusData().getFocusData();
                        final Float average=getFocusData.getData().getFocusData().getAverage();
                        final Float variance=getFocusData.getData().getFocusData().getVariance();
                        final int grade=getFocusData.getData().getFocusData().getGrade();
                        Log.d(TAG, "onResponse: GetStudyTimeData-----"+" times.size():"+focusDatas.size()+" average:"+average+" variance:"+variance+" grade:"+grade);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean is=false;
                                for (Float data:focusDatas) {
                                    if (data>0){
                                        is=true;
                                    }
                                }
                                if (is){
                                    linechart_data_attention.setVisibility(View.VISIBLE);
                                    showAlone(focusDatas);//折线图
                                }else{
                                    linechart_data_attention.setVisibility(View.GONE);
                                    Glide.with(AttentionDetails.this).load(R.drawable.nodata).into(image_attention_nodata);
                                }

                                tx_data_attention_variance.setText(String.valueOf(variance));//方差
                                tx_data_attention_average.setText(String.valueOf(average));//平均值
                                Utils.setGradeShow(grade,star31,star32,star33,star34,star35);//等级设置

                            }
                        });
                    }
                }else{
                    showServiceInfo(getContext(),"服务器故障");
                }
            }
        });
    }

    //展示折线图
    private void showAlone(List<Float> data) {
        LineChartManager lineChartManager=new LineChartManager(linechart_data_attention);

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

}
