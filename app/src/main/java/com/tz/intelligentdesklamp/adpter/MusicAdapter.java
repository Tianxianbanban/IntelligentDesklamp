package com.tz.intelligentdesklamp.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.MusicBean;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    List<MusicBean.DataBean.MusicsBean>  mList;
    Context mContext;

    MusicCallBack musicCallBack;

    public void setMusicCallBack(MusicCallBack callBack){
        musicCallBack = callBack;
    }

    public interface MusicCallBack{
        void onClicked(MusicBean.DataBean.MusicsBean bean);
        void onLongClicked(MusicBean.DataBean.MusicsBean bean);
    }

    public MusicAdapter(Context context, List<MusicBean.DataBean.MusicsBean>  list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ll_music_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                MusicBean.DataBean.MusicsBean bean = mList.get(pos);
                musicCallBack.onClicked(bean);
            }
        });
        viewHolder.ll_music_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                MusicBean.DataBean.MusicsBean bean = mList.get(pos);
                musicCallBack.onLongClicked(bean);
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MusicBean.DataBean.MusicsBean bean = mList.get(i);
        viewHolder.tv_music_name.setText(bean.getMusicName());
        viewHolder.tv_music_info.setText(bean.getMusicUrl());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_music_name;
        TextView tv_music_info;
        LinearLayout ll_music_item;
        public ViewHolder(View view){
            super(view);
            tv_music_name = view.findViewById(R.id.tv_music_name);
            tv_music_info = view.findViewById(R.id.tv_music_info);
            ll_music_item = view.findViewById(R.id.ll_music_item);
        }
    }
}
