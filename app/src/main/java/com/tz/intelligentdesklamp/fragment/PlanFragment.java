package com.tz.intelligentdesklamp.fragment;
/**
 * 台灯
 */

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;

public class PlanFragment extends BaseFragment {
    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plan, null);
        return view;
    }


}
