package com.tz.intelligentdesklamp.bean;

public class GetBegin {
    int code;
    String msg;
    Content data;

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

    public Content getData() {
        return data;
    }

    public void setData(Content data) {
        this.data = data;
    }

    public class Content{
        String key;

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
