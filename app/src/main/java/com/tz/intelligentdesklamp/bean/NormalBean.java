package com.tz.intelligentdesklamp.bean;

/**
 * NormalBean
 * 只关注返回的json中
 * code是否等于0
 */
public class NormalBean {

    /**
     * code : 0
     * msg : null
     * data : null
     */

    private int code;
    private String msg;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
