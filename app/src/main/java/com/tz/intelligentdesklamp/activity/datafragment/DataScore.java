package com.tz.intelligentdesklamp.activity.datafragment;

/**
 * 评分数据
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
import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.GetMarkData;
import com.tz.intelligentdesklamp.util.chart.BarChartManager;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;
import com.tz.intelligentdesklamp.util.use.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataScore extends BaseDataActivity implements View.OnClickListener{
    String TAG="DataScore";
    private ImageView image_data_score_back;
    private Button bt_data_score_date_cut;
    private Button bt_data_score_date_add;
    private TextView tx_data_score_date;
    private ImageView image_score_nodata;
    BarChart barchart_data_score;//柱状图
    private TextView tx_data_score_average;//期望
    private TextView tx_data_score_variance;//方差
    private ImageView star21,star22,star23,star24,star25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_score);

        image_data_score_back=(ImageView) findViewById(R.id.image_data_score_back);
        bt_data_score_date_cut=(Button)findViewById(R.id.bt_data_score_date_cut);
        bt_data_score_date_add=(Button)findViewById(R.id.bt_data_score_date_add);
        tx_data_score_date=(TextView)findViewById(R.id.tx_data_score_date);
        barchart_data_score=(BarChart)findViewById(R.id.barchart_data_score);//柱状图
        image_score_nodata=(ImageView)findViewById(R.id.image_score_nodata);//
        tx_data_score_average=(TextView)findViewById(R.id.tx_data_score_average);//期望
        tx_data_score_variance=(TextView)findViewById(R.id.tx_data_score_variance);//方差
        star21=(ImageView)findViewById(R.id.star1);//等级
        star22=(ImageView)findViewById(R.id.star2);
        star23=(ImageView)findViewById(R.id.star3);
        star24=(ImageView)findViewById(R.id.star4);
        star25=(ImageView)findViewById(R.id.star5);

        /**
         * 界面内容初始布置
         */
        tx_data_score_date.setText(getDate());//当前日期
        markInitdata(getDate());//所有数据


        image_data_score_back.setOnClickListener(this);
        bt_data_score_date_cut.setOnClickListener(this);
        bt_data_score_date_add.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        String requestDateText=null;
        switch (view.getId()){
            case R.id.image_data_score_back:
                finish();
                break;
            case R.id.bt_data_score_date_cut:
                requestDateText=getCutRequestDateText(tx_data_score_date.getText().toString());
                markInitdata(requestDateText);
                tx_data_score_date.setText(requestDateText);
                Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_data_score_date_add:
                if (tx_data_score_date.getText().toString().equals(getDate())){
                    Toast.makeText(getContext(),"无法查询当前的下一周。",Toast.LENGTH_SHORT).show();
                }else {
                    requestDateText=getAddRequestDateText(tx_data_score_date.getText().toString());//获取请求日期
                    markInitdata(requestDateText);
                    tx_data_score_date.setText(requestDateText);
                    Toast.makeText(getContext(), "数据始于"+requestDateText, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //展示柱状图
    private void showBarChart(List<Float> data) {
        // 横轴
        List<Float> xyvals = new ArrayList<>();
        xyvals.add((float) 1);
        xyvals.add((float) 2);
        xyvals.add((float) 3);
        xyvals.add((float) 4);
        xyvals.add((float) 5);
        xyvals.add((float) 6);
        xyvals.add((float) 7);


        //纵轴
        List<Float> yyvals = new ArrayList<>();

        for (int i=0;i<data.size();i++){
            yyvals.add(data.get(i));
        }
        //颜色
        Integer color=Color.parseColor("#11C2EE");
        BarChartManager barChartManager=new BarChartManager(barchart_data_score);
        barChartManager.showBarChartA(xyvals,yyvals,"",color);
    }


    //根据当前日期发起网络请求获取当前日期的统计数据
    public void markInitdata(final String dateText){

        final RequestBody requestBody=new FormBody.Builder()
                .add("date",dateText)
                .build();

        HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getMarkDataUrl,getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showServiceInfo(getContext(),"服务器故障");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.d(TAG, "请求评分数据------onResponse: "+responseData);
                try{
                    if (response.code()==200){//如果正常返回开始解析
                        Gson gson=new Gson();
                        GetMarkData getMarkData=gson.fromJson(responseData, new TypeToken<GetMarkData>(){}.getType());
                        if (getMarkData.getCode()==0){
                            final List<Float> scores=getMarkData.getData().getMarkData().getScore();
                            final float average=getMarkData.getData().getMarkData().getAverage();
                            final float variance=getMarkData.getData().getMarkData().getVariance();
                            final int grade=getMarkData.getData().getMarkData().getGrade();
                            Log.d(TAG, "评分数据onResponse: GetStudyTimeData："+" dateText="+dateText+" 柱状图数据scores.size()="+scores.size()+" average="+average+" variance="+variance+" grade="+grade);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean is=false;
                                    for (Float data:scores) {
                                        if (data>0){
                                            is=true;
                                        }
                                    }
                                    if (is){
                                        barchart_data_score.setVisibility(View.VISIBLE);
                                        image_score_nodata.setVisibility(View.GONE);
                                        showBarChart(scores);//柱状图
                                    }else {
                                        barchart_data_score.setVisibility(View.GONE);
                                        image_score_nodata.setVisibility(View.VISIBLE);
                                        Glide.with(DataScore.this).load(R.drawable.nodata).into(image_score_nodata);
                                    }

                                    tx_data_score_average.setText(String.valueOf(average));//总时长
                                    tx_data_score_variance.setText(String.valueOf(variance));//平均值
                                    Utils.setGradeShow(grade,star21,star22,star23,star24,star25);//等级设置
                                }
                            });
                        }
                    }else{
                        showServiceInfo(getContext(),"服务器故障");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


}
