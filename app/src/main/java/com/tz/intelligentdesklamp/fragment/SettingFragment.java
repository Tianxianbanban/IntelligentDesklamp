package com.tz.intelligentdesklamp.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private Button bt_setting01;
    private Button bt_setting02;
    private Button bt_setting03;
    private Button bt_setting04;
    private Button bt_setting05;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_setting, null);
        bt_setting01 = view.findViewById(R.id.bt_setting01);
        bt_setting02 = view.findViewById(R.id.bt_setting02);
        bt_setting03 = view.findViewById(R.id.bt_setting03);
        bt_setting04 = view.findViewById(R.id.bt_setting04);
        bt_setting05 = view.findViewById(R.id.bt_setting05);
        bt_setting01.setOnClickListener(this);
        bt_setting02.setOnClickListener(this);
        bt_setting03.setOnClickListener(this);
        bt_setting04.setOnClickListener(this);
        bt_setting05.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_setting01:
                Toast.makeText(getContext(), "bt_setting01", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_setting02:
                Toast.makeText(getContext(), "bt_setting02", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_setting03:
                Toast.makeText(getContext(), "bt_setting03", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_setting04:
                Toast.makeText(getContext(), "bt_setting04", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_setting05:
                Toast.makeText(getContext(), "bt_setting05", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
