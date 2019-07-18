package com.tz.intelligentdesklamp.adpter;

/**
 * 图片缩略图列表适配器
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.about.ItemOfBitmapShow;

import java.util.List;


public class PictureShowAdapter extends ArrayAdapter<ItemOfBitmapShow> {
    private int resourceId;
    public PictureShowAdapter(Context context, int textViewResourceId, List<ItemOfBitmapShow> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        ItemOfBitmapShow picture=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        ImageView pic=(ImageView) view.findViewById(R.id.iv_picture_show_item);

        pic.setImageBitmap(picture.getBitmap());
        return view;
    }
}


//public class PictureShowAdapter extends ArrayAdapter<Bitmap> {
//    private int resourceId;
//    public PictureShowAdapter(Context context, int textViewResourceId, List<Bitmap> objects){
//        super(context,textViewResourceId,objects);
//        resourceId=textViewResourceId;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Bitmap picture=getItem(position);
//        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//
//        ImageView pic=(ImageView) view.findViewById(R.id.iv_picture_show_item);
//        pic.setImageBitmap(picture);
//        return view;
//    }
//}
