package com.tz.intelligentdesklamp;

/**
 * 主活动
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.tz.intelligentdesklamp.activity.datafragment.BaseDataActivity;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.fragment.HomeFragment;
import com.tz.intelligentdesklamp.fragment.PersonFragment;
import com.tz.intelligentdesklamp.fragment.PlanFragment;
import com.tz.intelligentdesklamp.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;


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


}
