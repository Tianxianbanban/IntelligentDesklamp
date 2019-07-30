package com.tz.intelligentdesklamp.util.chart;

/**
 * 关于柱状图设置
 */

import android.graphics.Color;
import android.graphics.Matrix;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;

public class BarChartManager {
    public BarChart barChart;

    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    public BarChartManager(BarChart barChart) {
        this.barChart = barChart;
        leftAxis=barChart.getAxisLeft();
        rightAxis=barChart.getAxisRight();
        xAxis=barChart.getXAxis();
    }
    //设置初始化
    private void initBarChart(){
        //背景颜色
        barChart.setBackgroundColor(Color.parseColor("#00000000"));//设置背景颜色透明
        //网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边界
        barChart.setDrawBorders(false);
        //设置动画效果
        barChart.animateY(1000,Easing.EasingOption.Linear);
//        barChart.animateX(1000,Easing.EasingOption.Linear);
        //图例设置
        Legend legend=barChart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);//图例窗体的形状
        legend.setTextColor(Color.BLUE);//设置图例文字颜色
        legend.setTextSize(20f);//设置图例文字的大小
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); //图例说明文字在图表的相对位置
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); //图例左居中
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例的方向为水平
        legend.setDrawInside(false);//绘制在chart的外侧
        //XY轴的设置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X轴设置显示位置在底部
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签
        xAxis.setTextColor(Color.BLUE);//设置X轴字体颜色
        xAxis.setTextSize(12f);  //设置X轴字体大小
        xAxis.setDrawGridLines(false); //不绘制X轴网格，默认为绘制。
        //y轴设置
        leftAxis.setAxisMinimum(0f); //保证Y轴从0开始，不然会上移一点
        rightAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
//        leftAxis.setDrawAxisLine(false);//去除左边界线


        //将条目缩小 可滑动
        barChart.invalidate();
        Matrix mMatrix = new Matrix();
        mMatrix.postScale(0.1f, 1f);  //X轴缩小为原来的十分之一  竖直方向不变
        barChart.getViewPortHandler().refresh(mMatrix, barChart, false);
        barChart.animateY(800);

    }

    //下面的方法还需要修改
    //展示柱状图单条           //下方疑问
    public void showBarChartA(List<Float> xAxisValues, List<Float> yAxisValues, String label, Integer color) {
        initBarChart(); //首先基本数据的初始化
        List<BarEntry> entries = new ArrayList<>();//将数据源添加到图
        for (int i = 0;i<xAxisValues.size(); i++) {//将横纵坐标放置

            entries.add(new BarEntry(xAxisValues.get(i),yAxisValues.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setColor(color);  //柱状图颜色
        barDataSet.setValueTextSize(12f); //设置数值字体大小
        barDataSet.setFormLineWidth(1f);  //线条宽度
        barDataSet.setFormSize(15f);  ///图例窗体的大小
        ArrayList dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        xAxis.setLabelCount(entries.size() - 1, false);//设置X轴的刻度数
        barChart.setData(data);
    }

    //下面的方法还需要修改
    //多条柱状图
    public void showBarChartB(List<Float> xAxisValues, List<Float> yAxisValues, List<String> labels, List<Integer> colours) {
        initBarChart(); //同理
        BarData data = new BarData();
        for (int i = 0; i < xAxisValues.size(); i++) {
            List<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.size(); j++) {
                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(colours.get(i));
            barDataSet.setValueTextSize(10f); //设置数值字体大小
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            data.addDataSet(barDataSet);
        }
        int amount = yAxisValues.size();
        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        data.setBarWidth(barWidth);
        data.groupBars(0, groupSpace, barSpace);
        barChart.setData(data);
    }


}
