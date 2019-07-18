package com.tz.intelligentdesklamp.fragment;

/**
 * 效率
 */


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.TodoListAdapter;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.bean.JsonLogin;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends BaseFragment{
    private List<String> todoList=new ArrayList<>();//展示待办事项
    ListView lv_efficency_todo;//列表
    Button bt_efficency_addtodo;//添加任务
    TodoListAdapter todoListAdapter;
    private Button bt_todo_item_edit;//编辑


    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_setting, null);
        lv_efficency_todo=(ListView)view.findViewById(R.id.lv_efficency_todo);
        bt_efficency_addtodo=(Button)view.findViewById(R.id.bt_efficency_addtodo);
        bt_todo_item_edit=(Button)view.findViewById(R.id.bt_todo_item_edit);

        bt_efficency_addtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomInput(null);//添加任务
            }
        });

//        bt_todo_item_edit.setOnClickListener(new View.OnClickListener() {//点击编辑
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(),"编辑",Toast.LENGTH_SHORT);
//            }
//        });

        //内容
//        todoList.add("任务添加在这里");
//        todoList.add("hhh");
//        todoList.add("ooo");
//        todoList.add("ppp");

        String items =getContext().getSharedPreferences("todoList_data", MODE_PRIVATE)
                .getString("todoList", "data_null");
        Log.d("items", "initView: "+items);
//        Gson gson=new Gson();
//        todoList=gson.fromJson(items,new TypeToken<List<String>>(){}.getType());
        //取出内容
        if (todoList.size()>0){
            Log.d("itemsToGet", "initView: "+todoList.toString());
        }

//        todoListAdapter=new TodoListAdapter(getContext(),R.layout.item_of_todo,todoList);
        todoListAdapter=new TodoListAdapter(getContext(),todoList);
        lv_efficency_todo.setAdapter(todoListAdapter);

        return view;
    }

    //展示底部输入框
    public void showBottomInput(String content) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_menu_todo_input, null);
        final Dialog dialog = new Dialog(getContext());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setContentView(view);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();

        TextView tx_todo_cancel=(TextView)view.findViewById(R.id.tx_todo_cancel);
        TextView tx_todo_save=(TextView)view.findViewById(R.id.tx_todo_save);
        final EditText et_todo_taskinput=(EditText)view.findViewById(R.id.et_todo_taskinput);
        et_todo_taskinput.setText(content);

        //点击事件
        tx_todo_cancel.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tx_todo_save.setOnClickListener(new View.OnClickListener() {//保存添加
            @Override
            public void onClick(View view) {
                String taskWaittoAdd=et_todo_taskinput.getText().toString();
                if (taskWaittoAdd.equals("")){
                    dialog.dismiss();
                }else{
                    todoList.add(taskWaittoAdd);
                    todoListAdapter=new TodoListAdapter(getContext(),todoList);
                    lv_efficency_todo.setAdapter(todoListAdapter);

                    Gson gson=new Gson();
                    String itemsToSave=gson.toJson(todoList);
                    SharedPreferences.Editor editor=getContext().getSharedPreferences("todoList_data",MODE_PRIVATE).edit();
//                    editor.putString("todoList",todoList.toString());
                    editor.putString("todoList",itemsToSave);
//                    Log.d("todoList.toString()", "onClick: "+todoList.toString());
                    Log.d("todoList.toString()", "onClick: "+itemsToSave);
                    editor.apply();
                    dialog.dismiss();
                }

            }
        });

    }


}
