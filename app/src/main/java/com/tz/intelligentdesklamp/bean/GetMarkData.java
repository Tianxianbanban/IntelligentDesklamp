package com.tz.intelligentdesklamp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取得分统计数据
 */

public class GetMarkData {
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
        private MarkData markData;

        public MarkData getMarkData() {
            return markData;
        }

        public void setMarkData(MarkData markData) {
            this.markData = markData;
        }

        public class MarkData{
            private List<Float> score;
            private float average;
            private float variance;
            private int grade;

            public List<Float> getScore() {
                return score;
            }

            public void setScore(List<Float> score) {
                this.score = score;
            }

            public float getAverage() {
                return average;
            }

            public void setAverage(float average) {
                this.average = average;
            }

            public float getVariance() {
                return variance;
            }

            public void setVariance(float variance) {
                this.variance = variance;
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

//{
//        "code":0,
//        "msg":null,
//        "data":{
//        "markData":{
//        "score":[
//        0,
//        0,
//        0,
//        0,
//        0,
//        0,
//        0
//        ],
//        "average":0,
//        "variance":0,
//        "grade":0
//        }
//        }
//}