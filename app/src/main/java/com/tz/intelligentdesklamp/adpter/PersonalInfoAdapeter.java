package com.tz.intelligentdesklamp.adpter;
/**
 * 个人资料点击内容项目列表适配器
 */

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.tz.intelligentdesklamp.R;
        import com.tz.intelligentdesklamp.adpter.about.ItemOfPersonalInfo;

        import java.util.List;

public class PersonalInfoAdapeter extends ArrayAdapter<ItemOfPersonalInfo> {

    private int resourceId;

    public PersonalInfoAdapeter(Context context, int textViewResourceId, List<ItemOfPersonalInfo> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ItemOfPersonalInfo itemOfPersonalInfo=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView item=(TextView)view.findViewById(R.id.tx_personalInfo_item);
        TextView content=(TextView)view.findViewById(R.id.tx_personalInfo_content);
        ImageView right=(ImageView) view.findViewById(R.id.image_personlInfo_right);

        item.setText(itemOfPersonalInfo.getItem());
        content.setText(itemOfPersonalInfo.getContent());
        right.setImageResource(itemOfPersonalInfo.getImageId());

        return view;
    }
}
