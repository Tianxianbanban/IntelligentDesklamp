package com.tz.intelligentdesklamp.adpter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.gson.Gson;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.activity.TodoItemStart;
import com.tz.intelligentdesklamp.bean.my_info_tosave.TaskAndTime;
import com.tz.intelligentdesklamp.util.use.TaskAndTimeUtil;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class TodoListAdapter extends BaseSwipeAdapter {
    String TAG="TodoListAdapter";

    private Context mContext;
    List<TaskAndTime> todoListInAdapter;

    public TodoListAdapter(Context mContext,List<TaskAndTime> list) {
        this.mContext = mContext;
        this.todoListInAdapter=list;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.lv_todo_item_swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_of_todo, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);//!!!
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
            }
        });
        //双击
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                if (!todoListInAdapter.get(position).isFlag()){
                    Intent intentOfTodoItemStart=new Intent(mContext,TodoItemStart.class);//跳转到番茄计时
                    intentOfTodoItemStart.putExtra("task",todoListInAdapter.get(position).getTask());//任务内容传递
                    intentOfTodoItemStart.putExtra("time",todoListInAdapter.get(position).getTime());//任务时间传递
                    intentOfTodoItemStart.putExtra("position",position);//position传递
                    mContext.startActivity(intentOfTodoItemStart);
                }else{
                    Toast.makeText(mContext,"真是太棒了，已经完成任务了呢！",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //单击
//        swipeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "点击啊"+todoListInAdapter.get(position), Toast.LENGTH_SHORT).show();
//                //创建意图
//                Intent intentOfTodoItemStart=new Intent(mContext,TodoItemStart.class);//跳转到番茄计时
//                intentOfTodoItemStart.putExtra("task",todoListInAdapter.get(position));//任务内容传递
//                mContext.startActivity(intentOfTodoItemStart);
//            }
//        });

        //去除了编辑
//        v.findViewById(R.id.bt_todo_item_edit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "点击编辑", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        //删除任务项
        v.findViewById(R.id.bt_todo_item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除
                todoListInAdapter.remove(position);
                //更新
                notifyDataSetChanged();
                //将任务重新整理存储
                Gson gson=new Gson();
                String itemsToSave=gson.toJson(todoListInAdapter);
                SharedPreferences.Editor editor=mContext.getSharedPreferences("todoList_data",MODE_PRIVATE).edit();
                editor.putString("todoList",itemsToSave);
                editor.apply();
                Log.d(TAG, "onClick: todoListInAdapter.toString() 删除个所有任务存储内容："+itemsToSave);
                swipeLayout.close();
            }
        });

        //确认完成
        v.findViewById(R.id.bt_todo_item_confirm_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //使得完成戳可见
                todoListInAdapter.get(position).setFlag(true);//标记为已经完成
                TaskAndTimeUtil.putTaskAndTimes(mContext,todoListInAdapter);//存储
                notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ImageView image_itemoftodo_finish=(ImageView)convertView.findViewById(R.id.image_itemoftodo_finish) ;
        TextView t = (TextView)convertView.findViewById(R.id.tx_itemoftodo_content);//任务事项
        TextView tx_itemoftodo_time=(TextView)convertView.findViewById(R.id.tx_itemoftodo_time);//预估时间
        t.setText(todoListInAdapter.get(position).getTask());
        tx_itemoftodo_time.setText(todoListInAdapter.get(position).getTime()+"分钟");

        if (todoListInAdapter.get(position).isFlag()){//如果已经完成
            image_itemoftodo_finish.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getCount() {
        return todoListInAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

