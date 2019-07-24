package com.tz.intelligentdesklamp.fragment.home;

/**
 * 显示统计数据
 */

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.activity.datafragment.AttentionDetails;
import com.tz.intelligentdesklamp.activity.datafragment.DataPosture;
import com.tz.intelligentdesklamp.activity.datafragment.DataScore;
import com.tz.intelligentdesklamp.activity.datafragment.LearningTime;
import com.tz.intelligentdesklamp.base.BaseFragment;


public class DataFragment extends BaseFragment implements View.OnClickListener{
    ImageView image_posture_data_back_flower;


    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_data, null);

        final RelativeLayout posture_data=view.findViewById(R.id.posture_data);
        final RelativeLayout score_data=view.findViewById(R.id.score_data);
        final RelativeLayout learning_time=view.findViewById(R.id.learning_time);
        final RelativeLayout attention_details=view.findViewById(R.id.attention_details);
        final RelativeLayout daily_data=view.findViewById(R.id.daily_data);//查看日报，为链接

        image_posture_data_back_flower=(ImageView)view.findViewById(R.id.image_posture_data_back_flower);//背景框框
        Glide.with(this).load(R.drawable.back_flower).into(image_posture_data_back_flower);

        HTextView htx=view.findViewById(R.id.htx);
        htx.setAnimateType(HTextViewType.EVAPORATE);
//        htx.setAnimateType(HTextViewType.SCALE);
        htx.animateText("坐姿习惯");


        posture_data.setOnClickListener(this);
        score_data.setOnClickListener(this);
        learning_time.setOnClickListener(this);
        attention_details.setOnClickListener(this);
        daily_data.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View view) {//点击选项时候把日期数据传递过去
        switch (view.getId()){
            case R.id.posture_data:
                Intent postureIntent=new Intent(getContext(),DataPosture.class);
                startActivity(postureIntent);
                break;
            case R.id.score_data:
                Intent scoreIntent=new Intent(getContext(),DataScore.class);
                startActivity(scoreIntent);
                break;
            case R.id.learning_time:
                Intent learningIntent=new Intent(getContext(),LearningTime.class);
                startActivity(learningIntent);
                break;
            case R.id.attention_details:
                Intent attentionIntent=new Intent(getContext(),AttentionDetails.class);
                startActivity(attentionIntent);
                break;
            case R.id.daily_data:
                Toast.makeText(getContext(),"查看日报",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
