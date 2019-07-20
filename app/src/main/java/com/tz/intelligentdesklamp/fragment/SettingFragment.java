package com.tz.intelligentdesklamp.fragment;

/**
 * 效率
 */


import android.app.Dialog;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.TodoListAdapter;
import com.tz.intelligentdesklamp.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends BaseFragment{
    String TAG="SettingFragment";
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
//        bt_todo_item_edit=(Button)view.findViewById(R.id.bt_todo_item_edit);//去除了编辑

        bt_efficency_addtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomInput(null);//添加任务，弹出输入框
            }
        });


        //将存储任务取出
        String items =getContext().getSharedPreferences("todoList_data", MODE_PRIVATE)
                .getString("todoList", "data_null");
        Log.d("items", "initView: "+items);
        //设置列表内容
        Gson gson=new Gson();
        todoList=gson.fromJson(items,new TypeToken<List<String>>(){}.getType());
        if (todoList.size()>0){
            Log.d("itemsToGet", "initView: "+todoList.toString());
        }
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
        tx_todo_cancel.setOnClickListener(new View.OnClickListener() {//取消添加
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tx_todo_save.setOnClickListener(new View.OnClickListener() {//保存添加
            @Override
            public void onClick(View view) {
                String taskWaitToAdd=et_todo_taskinput.getText().toString();//等待存储的任务内容
                if (taskWaitToAdd.equals("")){//如果输入为空直接关闭输入框
                    dialog.dismiss();
                }else if (taskWaitToAdd.length()>100){
                    Toast.makeText(mContext,"可以精简任务描述呢~~~",Toast.LENGTH_SHORT).show();
                }else{
                    //添加并设置适配器
                    todoList.add(taskWaitToAdd);
                    todoListAdapter=new TodoListAdapter(getContext(),todoList);
                    lv_efficency_todo.setAdapter(todoListAdapter);
                    //将todoList转换成gson进行SP存储
                    Gson gson=new Gson();
                    String itemsToSave=gson.toJson(todoList);
                    SharedPreferences.Editor editor=getContext().getSharedPreferences("todoList_data",MODE_PRIVATE).edit();
                    editor.putString("todoList",itemsToSave);
                    editor.apply();
                    Log.d(TAG, "onClick: todoList.toString() 所有任务存储内容："+itemsToSave);
                    dialog.dismiss();
                }

            }
        });

    }

}
