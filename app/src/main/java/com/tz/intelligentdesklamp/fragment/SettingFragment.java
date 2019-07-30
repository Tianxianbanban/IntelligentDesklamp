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
import com.tz.intelligentdesklamp.bean.my_info_tosave.TaskAndTime;

import java.util.ArrayList;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends BaseFragment{
    String TAG="SettingFragment";
    private List<TaskAndTime> taskAndTimes=new ArrayList<>();//修改之后的任务列表
    ListView lv_efficency_todo;//列表
    Button bt_efficency_addtodo;//添加任务
    private TaskAndTime taskAndTime;//存储任务项
    TodoListAdapter todoListAdapter;
//    private Button bt_todo_item_edit;//编辑


    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_setting, null);
        lv_efficency_todo=(ListView)view.findViewById(R.id.lv_efficency_todo);
        bt_efficency_addtodo=(Button)view.findViewById(R.id.bt_efficency_addtodo);
//        bt_todo_item_edit=(Button)view.findViewById(R.id.bt_todo_item_edit);//去除了编辑

        bt_efficency_addtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomInput();//添加任务，弹出输入框
            }
        });

        refreshData();

        return view;
    }

    //展示底部输入框
    public void showBottomInput() {
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
        final EditText et_todo_taskinput=(EditText)view.findViewById(R.id.et_todo_taskinput);//任务添加内容
        final EditText et_todo_timeinput=(EditText)view.findViewById(R.id.et_todo_timeinput);//预估时间填写


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
                String timeWaitToAdd=et_todo_timeinput.getText().toString();
                int timeWaitToDo=-1;
                //将时间转换成数字
                if (timeWaitToAdd.trim().equals("")||taskWaitToAdd.trim().equals("")){
                    Toast.makeText(mContext,"还没有填写完任务清单哦~~~",Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        timeWaitToDo=Integer.parseInt(timeWaitToAdd);
                    }catch (Exception e){
                        Log.d(TAG, "onClick: Integer.parseInt解析异常");
                        Toast.makeText(mContext,"请填写预估时间呢~",Toast.LENGTH_SHORT).show();
                    }
                }

                //保存之前进行条件判断
                if (timeWaitToDo<0){

                }else if (taskWaitToAdd.length()>200){
                    Toast.makeText(mContext,"可以精简任务描述呢~~~",Toast.LENGTH_SHORT).show();
                }else if (timeWaitToDo>250){
                    Toast.makeText(mContext,"劳逸结合，效率会更高呢！",Toast.LENGTH_SHORT).show();
                }else{
                    //添加并设置适配器
                    taskAndTime=new TaskAndTime();
                    taskAndTime.setTask(taskWaitToAdd);
                    taskAndTime.setTime(String.valueOf(timeWaitToAdd));
                    taskAndTime.setFlag(false);//0即是未完成
                    taskAndTimes.add(taskAndTime);//已经添加，修改之后
                    todoListAdapter=new TodoListAdapter(getContext(),taskAndTimes);
                    lv_efficency_todo.setAdapter(todoListAdapter);
                    //将todoList转换成gson进行SP存储
                    Gson gson=new Gson();
                    String itemsToSave=gson.toJson(taskAndTimes);
                    SharedPreferences.Editor editor=getContext().getSharedPreferences("todoList_data",MODE_PRIVATE).edit();
                    editor.putString("todoList",itemsToSave);
                    editor.apply();
                    Log.d(TAG, "onClick: todoList.toString() 所有任务存储内容："+itemsToSave);
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        Log.d(TAG, "onResume: ==========================");
    }

    private void refreshData(){
        String items =getContext().getSharedPreferences("todoList_data", MODE_PRIVATE).getString("todoList", "data_null");
        Log.d(TAG, "initView: "+items);

        try{
            Gson gson=new Gson();
            taskAndTimes=gson.fromJson(items,new TypeToken<List<TaskAndTime>>(){}.getType());
            todoListAdapter=new TodoListAdapter(getContext(),taskAndTimes);
            todoListAdapter.notifyDataSetChanged();
            lv_efficency_todo.setAdapter(todoListAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
