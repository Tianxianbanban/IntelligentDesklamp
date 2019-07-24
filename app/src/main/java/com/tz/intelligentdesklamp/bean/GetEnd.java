package com.tz.intelligentdesklamp.bean;

/**
 * 一个番茄时间结束返回信息
 */

//{"code":0,"msg":null,"data":{"feedback":{"wrongCount":5,"effectiveness":0.7,"effectiveTime":20,"frequent":1}}}
public class GetEnd {
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

        Feedback feedback;

        public Feedback getFeedback() {
            return feedback;
        }

        public void setFeedback(Feedback feedback) {
            this.feedback = feedback;
        }

        public class Feedback{
            int wrongCount;//错误次数
            float effectiveness;//效率
            float effectiveTime;//有效工作时间
            int frequent;//最频繁的错误姿势

            public int getWrongCount() {
                return wrongCount;
            }

            public void setWrongCount(int wrongCount) {
                this.wrongCount = wrongCount;
            }

            public float getEffectiveness() {
                return effectiveness;
            }

            public void setEffectiveness(float effectiveness) {
                this.effectiveness = effectiveness;
            }

            public float getEffectiveTime() {
                return effectiveTime;
            }

            public void setEffectiveTime(float effectiveTime) {
                this.effectiveTime = effectiveTime;
            }

            public int getFrequent() {
                return frequent;
            }

            public void setFrequent(int frequent) {
                this.frequent = frequent;
            }
        }
    }
}
