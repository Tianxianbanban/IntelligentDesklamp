package com.tz.intelligentdesklamp.bean;

/**
 * 对应登录返回json
 */
public class JsonLogin {
    private int code;
    private String msg;
    private LoginData data;

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

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public static class LoginData {

        private int id;
        private String token;

        public String getToken() {
            return token;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

}
