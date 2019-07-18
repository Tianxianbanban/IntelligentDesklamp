package com.tz.intelligentdesklamp.bean;

import java.util.List;

/**
 * 获取学习时长
 */

public class GetStudyTimeData {
    private int code;
    private String msg;
    private Content data;

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

    public class Content{//
       private StudyTimeData studyTimeData;

        public StudyTimeData getStudyTimeData() {
            return studyTimeData;
        }

        public void setStudyTimeData(StudyTimeData studyTimeData) {
            this.studyTimeData = studyTimeData;
        }

        public class StudyTimeData{
           private List<Float> time;
           private Float totalTime;
           private Float average;
           private int grade;

           public List<Float> getTime() {
               return time;
           }

           public void setTime(List<Float> time) {
               this.time = time;
           }

           public Float getTotalTime() {
               return totalTime;
           }

           public void setTotalTime(Float totalTime) {
               this.totalTime = totalTime;
           }

           public Float getAverage() {
               return average;
           }

           public void setAverage(Float average) {
               this.average = average;
           }

           public int getGrade() {
               return grade;
           }

           public void setGrade(int grade) {
               this.grade = grade;
           }
       }
    }
}
