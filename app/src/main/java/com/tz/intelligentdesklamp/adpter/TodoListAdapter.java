package com.tz.intelligentdesklamp.adpter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.gson.Gson;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.activity.TodoItemStart;
import com.tz.intelligentdesklamp.fragment.SettingFragment;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static org.litepal.LitePalApplication.getContext;
//
//public class TodoListAdapter extends ArrayAdapter<String> {
//    private int resourceId;
//
//    public TodoListAdapter(Context context, int textViewResourceId, List<String> objects){
//        super(context,textViewResourceId,objects);
//        resourceId=textViewResourceId;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        String itemOfTodo=getItem(position);
//        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//        TextView item=(TextView)view.findViewById(R.id.tx_itemoftodo_content);
//        item.setText(itemOfTodo);
//
//        //关于编辑和删除
//        Button bt_todo_item_edit=(Button)view.findViewById(R.id.bt_todo_item_edit);
//        bt_todo_item_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(),"编辑",Toast.LENGTH_SHORT);
//            }
//        });
//
//
//        return view;
//    }
//}

public class TodoListAdapter extends BaseSwipeAdapter {

    private Context mContext;
    List<String> todoListInAdapter;

    public TodoListAdapter(Context mContext,List<String> list) {
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
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "点击啊"+todoListInAdapter.get(position), Toast.LENGTH_SHORT).show();
                //创建意图
                Intent intentOfTodoItemStart=new Intent(mContext,TodoItemStart.class);//跳转到番茄计时
                intentOfTodoItemStart.putExtra("task",todoListInAdapter.get(position));//任务内容传递
                mContext.startActivity(intentOfTodoItemStart);
            }
        });
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
        v.findViewById(R.id.bt_todo_item_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击编辑", Toast.LENGTH_SHORT).show();
                
            }
        });
        v.findViewById(R.id.bt_todo_item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoListInAdapter.remove(position);
                notifyDataSetChanged();
                swipeLayout.close();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.tx_itemoftodo_content);//任务事项
//        t.setText((position ) + ".");
        t.setText(todoListInAdapter.get(position));
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

