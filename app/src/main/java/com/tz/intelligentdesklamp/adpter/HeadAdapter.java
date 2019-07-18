package com.tz.intelligentdesklamp.adpter;

/**
 * 个人资料点击结果头像部分适配器
 */
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tz.intelligentdesklamp.R;
//import com.tz.intelligentdesklamp.adpter.about.HeadOfPersonalInfo;
//
//import java.util.List;
//
//
//public class HeadAdapter extends ArrayAdapter<HeadOfPersonalInfo> {
//
//    private int resourceId;
//
//    public HeadAdapter(Context context, int textViewResourceId, List<HeadOfPersonalInfo> objects){
//        super(context,textViewResourceId,objects);
//        resourceId=textViewResourceId;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        HeadOfPersonalInfo headOfPersonalInfo=getItem(position);
//        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//
//        TextView head=(TextView)view.findViewById(R.id.tx_personalInfo_item);
//        ImageView headImage=(ImageView) view.findViewById(R.id.civ_personalInfo_headimage);
//        ImageView image=(ImageView) view.findViewById(R.id.image_personlInfo_right);
//
//        head.setText(headOfPersonalInfo.getHead());
//        headImage.setImageResource(headOfPersonalInfo.getHeadImageId());
//        image.setImageResource(headOfPersonalInfo.getImageId());
//
//        return view;
//    }
//}
