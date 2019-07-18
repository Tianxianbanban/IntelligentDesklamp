package com.tz.intelligentdesklamp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 获取基本数据
 */

public class GetBaseData {

    private int code;
    private String msg;
    private Data data;

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

    public void setDatabean(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }


    public class Data {
        private BaseDataViewObject baseDataViewObject;

        public BaseDataViewObject getBaseDataViewObject() {
            return baseDataViewObject;
        }
        public void setBaseDataViewObject(BaseDataViewObject baseDataViewObject) {
            this.baseDataViewObject = baseDataViewObject;
        }

        public class BaseDataViewObject {
            private double score;//69.5当前评分
            private int grade;//7 等级
            private int totalTime;//34 连续工作时长
            private double accuracy;//0.5882352941176471坐姿正确率
            private SittingPostureStatistics sittingPostureStatistics;//关于坐姿的情况

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public int getGrade() {
                return grade;
            }
            public void setGrade(int grade) {
                this.grade = grade;
            }
            public int getTotalTime() {
                return totalTime;
            }

            public void setTotalTime(int totalTime) {
                this.totalTime = totalTime;
            }

            public double getAccuracy() {
                return accuracy;
            }

            public void setAccuracy(double accuracy) {
                this.accuracy = accuracy;
            }

            public SittingPostureStatistics getSittingPostureStatistics() {
                return sittingPostureStatistics;
            }

            public void setSittingPostureStatistics(SittingPostureStatistics sittingPostureStatistics) {
                this.sittingPostureStatistics = sittingPostureStatistics;
            }

            public class SittingPostureStatistics{
                @SerializedName("0")
                int a;//正确坐姿
                @SerializedName("1")
                int b;//左手错误放置
                @SerializedName("2")
                int c;//右手错误放置
                @SerializedName("3")
                int d;//头左偏
                @SerializedName("4")
                int e;//头右偏
                @SerializedName("5")
                int f;//身体倾斜
                @SerializedName("6")
                int g;//趴下

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
































//public class GetBaseData {
//
//    private int code;
//    private String msg;
//    private Data Data;
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//    public int getCode() {
//        return code;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setData(Data data) {
//        Data = data;
//    }
//    public Data getData() {
//        return Data;
//    }
//
//
//    public class Data {
//        private BaseDataViewObject baseDataViewObject;
//
//        public BaseDataViewObject getBaseDataViewObject() {
//            return baseDataViewObject;
//        }
//        public void setBaseDataViewObject(BaseDataViewObject baseDataViewObject) {
//            this.baseDataViewObject = baseDataViewObject;
//        }
//
//        public class BaseDataViewObject {
//            private double score;//69.5当前评分
//            private int grade;//7 等级
//            private int totalTime;//34 连续工作时长
//            private double accuracy;//0.5882352941176471坐姿正确率
//            private SittingPostureStatistics sittingPostureStatistics;//关于坐姿的情况
//
//            public double getScore() {
//                return score;
//            }
//
//            public void setScore(double score) {
//                this.score = score;
//            }
//
//            public int getGrade() {
//                return grade;
//            }
//            public void setGrade(int grade) {
//                this.grade = grade;
//            }
//            public int getTotalTime() {
//                return totalTime;
//            }
//
//            public void setTotalTime(int totalTime) {
//                this.totalTime = totalTime;
//            }
//
//            public double getAccuracy() {
//                return accuracy;
//            }
//
//            public void setAccuracy(double accuracy) {
//                this.accuracy = accuracy;
//            }
//
//            public SittingPostureStatistics getSittingPostureStatistics() {
//                return sittingPostureStatistics;
//            }
//
//            public void setSittingPostureStatistics(SittingPostureStatistics sittingPostureStatistics) {
//                this.sittingPostureStatistics = sittingPostureStatistics;
//            }
//
//            public class SittingPostureStatistics{
//                int a;
//                int b;
//                int c;
//                int d;
//                int e;
//                int f;
//                int g;
//
//                public int getA() {
//                    return a;
//                }
//
//                public void setA(int a) {
//                    this.a = a;
//                }
//
//                public int getB() {
//                    return b;
//                }
//
//                public void setB(int b) {
//                    this.b = b;
//                }
//
//                public int getC() {
//                    return c;
//                }
//
//                public void setC(int c) {
//                    this.c = c;
//                }
//
//                public int getD() {
//                    return d;
//                }
//
//                public void setD(int d) {
//                    this.d = d;
//                }
//
//                public int getE() {
//                    return e;
//                }
//
//                public void setE(int e) {
//                    this.e = e;
//                }
//
//                public int getF() {
//                    return f;
//                }
//
//                public void setF(int f) {
//                    this.f = f;
//                }
//
//                public int getG() {
//                    return g;
//                }
//
//                public void setG(int g) {
//                    this.g = g;
//                }
//            }
//
//        }
//    }
//}
