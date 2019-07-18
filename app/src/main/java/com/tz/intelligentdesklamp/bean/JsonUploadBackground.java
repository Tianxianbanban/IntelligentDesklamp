package com.tz.intelligentdesklamp.bean;


/**
 * 对应上传背景图片返回json
 */
public class JsonUploadBackground {
    private int code;
    private String msg;
    private UploadData data;

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

    public UploadData getData() {
        return data;
    }

    public void setData(UploadData data) {
        this.data = data;
    }

    public static class UploadData {

        private int bid;

        public int getBid() {
            return bid;
        }
        public void setBid(int bid) {
            this.bid = bid;
        }

    }

}
