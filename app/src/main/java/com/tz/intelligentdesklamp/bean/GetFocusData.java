package com.tz.intelligentdesklamp.bean;

import java.util.List;

public class GetFocusData {
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

    public class Content{
        private FocusData focusData;

        public FocusData getFocusData() {
            return focusData;
        }

        public void setFocusData(FocusData focusData) {
            this.focusData = focusData;
        }

        public class FocusData{
            List<Float> focusData;
            Float average;
            Float variance;
            int grade;

            public List<Float> getFocusData() {
                return focusData;
            }

            public void setFocusData(List<Float> focusData) {
                this.focusData = focusData;
            }

            public Float getAverage() {
                return average;
            }

            public void setAverage(Float average) {
                this.average = average;
            }

            public Float getVariance() {
                return variance;
            }

            public void setVariance(Float variance) {
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
