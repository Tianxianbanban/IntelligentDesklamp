package com.tz.intelligentdesklamp.fragment;
/**
 * 数据显示部分
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.fragment.home.DataFragment;
import com.tz.intelligentdesklamp.fragment.home.NowFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private Button bt_home01;
    private Button bt_home02;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        replaceFragment(new NowFragment());
        bt_home01 = view.findViewById(R.id.bt_home01);
        bt_home02 = view.findViewById(R.id.bt_home02);
        bt_home01.setOnClickListener(this);
        bt_home02.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        BaseFragment to;
        switch (view.getId()){
            case R.id.bt_home01:
                replaceFragment(new NowFragment());
                break;
            case R.id.bt_home02:
                replaceFragment(new DataFragment());
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.home_show,fragment);
        transaction.commit();
    }

}
