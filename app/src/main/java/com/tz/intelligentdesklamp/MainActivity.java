package com.tz.intelligentdesklamp;

/**
 * 主活动
 */


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.activity.datafragment.BaseDataActivity;
import com.tz.intelligentdesklamp.activity.datafragment.DataPosture;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.bean.GetSittingPostureData;
import com.tz.intelligentdesklamp.fragment.HomeFragment;
import com.tz.intelligentdesklamp.fragment.PersonFragment;
import com.tz.intelligentdesklamp.fragment.PlanFragment;
import com.tz.intelligentdesklamp.fragment.SettingFragment;
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

public class MainActivity extends BaseDataActivity {
    String TAG="MainActivity";

    private Fragment mContent;
    private int position;
    private List<BaseFragment> mBaseFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BaseFragment to;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    position=0;
                    to = getFragment();
                    switchFragment(mContent,to);
                    return true;
                case R.id.navigation_setting:
                    position=1;
                    to = getFragment();
                    switchFragment(mContent,to);
                    return true;
                case R.id.navigation_plan:
                    position=2;
                    to = getFragment();
                    switchFragment(mContent,to);
                    return true;
                case R.id.navigation_person:
                    position=3;
                    to = getFragment();
                    switchFragment(mContent,to);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HomeFragment());
        mBaseFragment.add(new SettingFragment());
        mBaseFragment.add(new PlanFragment());
        mBaseFragment.add(new PersonFragment());
    }

    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to){
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //判断有没有添加
            if(!to.isAdded()){
                if (from != null){
                    ft.hide(from);
                }
                if (to != null){
                    ft.add(R.id.fl_content,to).commit();
                }

            }else{
                if (from != null){
                    ft.hide(from);
                }
                if (to != null){
                    ft.show(to).commit();
                }

            }
        }
    }


    /**
     * 按键两次退出
     */
    private long clickTime = 0; // 第一次点击的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }







//    //根据当前日期发起网络请求
//    // 获取当前坐姿数据统计数据
//    private void postureInitdata(){
//
//        final RequestBody requestBody=new FormBody.Builder()
//                .add("date",getDate())
//                .build();
//
//        HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.sittingPostureDataUrl,getContext(), requestBody, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(),"服务器故障！",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                Log.d(TAG, "坐姿数据----onResponse: " + responseData);
//                try {
//                    Gson gson = new Gson();
//                    GetSittingPostureData getSittingPostureData = gson.fromJson(responseData, new TypeToken<GetSittingPostureData>() {
//                    }.getType());
//                    if (getSittingPostureData.getCode() == 0) {
//                        final float accuracy = getSittingPostureData.getData().getSittingPostureData().getAccuracy();
//                        final float average = getSittingPostureData.getData().getSittingPostureData().getAverage();
//                        final int grade = getSittingPostureData.getData().getSittingPostureData().getGrade();
//                        final int a = getSittingPostureData.getData().getSittingPostureData().getPostures().getA();
//                        final int b = getSittingPostureData.getData().getSittingPostureData().getPostures().getB();
//                        final int c = getSittingPostureData.getData().getSittingPostureData().getPostures().getC();
//                        final int d = getSittingPostureData.getData().getSittingPostureData().getPostures().getD();
//                        final int e = getSittingPostureData.getData().getSittingPostureData().getPostures().getE();
//                        final int f = getSittingPostureData.getData().getSittingPostureData().getPostures().getF();
//                        final int g = getSittingPostureData.getData().getSittingPostureData().getPostures().getG();
//                        Log.d(TAG, "坐姿数据=" + "accuracy " + accuracy + " average " + average + " grade " + grade + "" + " a " + a + " b " + b + " c " + c + " d " + d + " e " + e + " f " + f + " g " + g);
//                        Toast.makeText(getContext(),String.valueOf(accuracy),Toast.LENGTH_SHORT).show();
//                        //存储
//                        SharedPreferences.Editor editor=getContext().getSharedPreferences("posture_data",MODE_PRIVATE).edit();
//                        editor.putFloat("accuracy",accuracy);
//                        editor.putFloat("average",average);
//                        editor.putInt("grade",grade);
//                        editor.putInt("a",a);
//                        editor.putInt("b",b);
//                        editor.putInt("c",c);
//                        editor.putInt("d",d);
//                        editor.putInt("e",e);
//                        editor.putInt("f",f);
//                        editor.putInt("g",g);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//





}
