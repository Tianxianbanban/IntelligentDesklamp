package com.tz.intelligentdesklamp.fragment;

import android.view.View;

import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;

public class PersonFragment extends BaseFragment {
    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_person, null);
        return view;
    }
}
