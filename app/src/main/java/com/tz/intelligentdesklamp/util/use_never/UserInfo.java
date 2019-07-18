package com.tz.intelligentdesklamp.util.use_never;

/**
 * 指向用户信息
 */
public class UserInfo {


    public static class InfoData{
        String token;
        String username = null;
        String grade = null;
        String age = null;
        String phone = null;
        String email = null;
        String area = null;

        public void setToken(String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public  String getUsername() {
            return username;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
        public String getGrade() {
            return grade;
        }

        public void setAge(String age) {
            this.age = age;
        }
        public String getAge() {
            return age;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getPhone() {
            return phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public String getEmail() {
            return email;
        }

        public void setArea(String area) {
            this.area = area;
        }
        public String getArea() {
            return area;
        }
    }
}