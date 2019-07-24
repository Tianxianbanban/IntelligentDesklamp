package com.tz.intelligentdesklamp.bean.my_info_tosave;

import androidx.annotation.NonNull;

/**
 * 存储添加的任务内容与响应的预估时间
 */

public class TaskAndTime {
    private String task;
    private String time;
    private boolean flag;//用于标记是否已经完成

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
