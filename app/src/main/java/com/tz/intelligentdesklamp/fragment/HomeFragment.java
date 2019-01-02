package com.tz.intelligentdesklamp.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private Button bt_home01;
    private Button bt_home02;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        bt_home01 = view.findViewById(R.id.bt_home01);
        bt_home02 = view.findViewById(R.id.bt_home02);
        bt_home01.setOnClickListener(this);
        bt_home02.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_home01:
                Toast.makeText(getContext(), "b1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_home02:
                Toast.makeText(getContext(), "b2", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
