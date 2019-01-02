package com.tz.intelligentdesklamp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.fragment.HomeFragment;
import com.tz.intelligentdesklamp.fragment.PersonFragment;
import com.tz.intelligentdesklamp.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
                case R.id.navigation_person:
                    position=2;
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
}
