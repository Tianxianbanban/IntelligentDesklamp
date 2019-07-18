package com.tz.intelligentdesklamp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 获取坐姿统计数据
 */

public class GetSittingPostureData {
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
        private SittingPostureData sittingPostureData;

        public SittingPostureData getSittingPostureData() {
            return sittingPostureData;
        }

        public void setSittingPostureData(SittingPostureData sittingPostureData) {
            this.sittingPostureData = sittingPostureData;
        }

        public class SittingPostureData{
            private Postures postures;
            private float accuracy;
//            private String accuracy;//注意!!!
            private float average;
            private int grade;

            public Postures getPostures() {
                return postures;
            }

            public void setPostures(Postures postures) {
                this.postures = postures;
            }

            public float getAccuracy() {
                return accuracy;
            }

            public void setAccuracy(float accuracy) {
                this.accuracy = accuracy;
            }


//            public String getAccuracy() {
//                return accuracy;
//            }
//
//            public void setAccuracy(String accuracy) {
//                this.accuracy = accuracy;
//            }

            public float getAverage() {
                return average;
            }

            public void setAverage(float average) {
                this.average = average;
            }

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public class Postures{
                @SerializedName("0")
                int a;
                @SerializedName("1")
                int b;
                @SerializedName("2")
                int c;
                @SerializedName("3")
                int d;
                @SerializedName("4")
                int e;
                @SerializedName("5")
                int f;
                @SerializedName("6")
                int g;

                public int getA() {
                    return a;
                }

                public void setA(int a) {
                    this.a = a;
                }

                public int getB() {
                    return b;
                }

                public void setB(int b) {
                    this.b = b;
                }

                public int getC() {
                    return c;
                }

                public void setC(int c) {
                    this.c = c;
                }

                public int getD() {
                    return d;
                }

                public void setD(int d) {
                    this.d = d;
                }

                public int getE() {
                    return e;
                }

                public void setE(int e) {
                    this.e = e;
                }

                public int getF() {
                    return f;
                }

                public void setF(int f) {
                    this.f = f;
                }

                public int getG() {
                    return g;
                }

                public void setG(int g) {
                    this.g = g;
                }
            }
        }
    }
}

//{
//        "code":0,
//        "msg":null,
//        "data":{
//        "sittingPostureData":{
//        "postures":{
//        "0":80,
//        "1":5,
//        "2":4,
//        "3":10,
//        "4":3,
//        "5":4,
//        "6":2
//        },
//        "accuracy":0.7407407407407407,
//        "average":81.1,
//        "grade":5
//        }
//        }
//}
