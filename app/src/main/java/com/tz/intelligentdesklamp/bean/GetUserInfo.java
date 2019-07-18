package com.tz.intelligentdesklamp.bean;


/**
 * 对应获取用户信息
 */

public class GetUserInfo {
    int code;
    String msg;
    Data data;

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

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{
        UserInfo userInfo;

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public class UserInfo{
            int id;
            String age;//
            boolean sex;//性别是int
            String phoneNum;
            String email;
            String region;
            String imagePath;
            String nickName;
            String backgroundPath;

            public void setId(int id) {
                this.id = id;
            }
            public int getId() {
                return id;
            }

            public void setAge(String age) {
                this.age = age;
            }
            public String getAge() {
                return age;
            }

            public void setSex(boolean sex) {
                this.sex = sex;
            }

            public boolean isSex() {
                return sex;
            }

            public void setPhoneNum(String phoneNum) {
                this.phoneNum = phoneNum;
            }
            public String getPhoneNum() {
                return phoneNum;
            }

            public void setEmail(String email) {
                this.email = email;
            }
            public String getEmail() {
                return email;
            }

            public void setRegion(String region) {
                this.region = region;
            }
            public String getRegion() {
                return region;
            }

            public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
            }
            public String getImagePath() {
                return imagePath;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }
            public String getNickName() {
                return nickName;
            }

            public void setBackgroundPath(String backgroundPath) {
                this.backgroundPath = backgroundPath;
            }
            public String getBackgroundPath() {
                return backgroundPath;
            }
        }


    }

}
