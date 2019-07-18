package com.tz.intelligentdesklamp.bean;

/**
 * 对应返回json数据基础
 */

public class BasicData {
    int code;
    String msg;
    String data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
