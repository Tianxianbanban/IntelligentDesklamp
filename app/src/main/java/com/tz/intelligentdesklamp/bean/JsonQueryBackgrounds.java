package com.tz.intelligentdesklamp.bean;

import java.util.List;

/**
 * 获取用户所有的背景图片信息返回Json
 */
public class JsonQueryBackgrounds {
    private int code;
    private String msg;
    private DownloadData data;

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

    public void setData(DownloadData data) {
        this.data = data;
    }
    public DownloadData getData() {
        return data;
    }



    public static class DownloadData {
        List<Content>  backgrounds;

        public void setBackgrounds(List<Content> backgrounds) {
            this.backgrounds = backgrounds;
        }

        public List<Content> getBackgrounds() {
            return backgrounds;
        }
        public static class Content{
            private int bid;
            private int uid;
            private int flag;
            private String imagePath;

            public int getBid() {
                return bid;
            }

            public void setBid(int bid) {
                this.bid = bid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getUid() {
                return uid;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public int getFlag() {
                return flag;
            }

            public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
            }

            public String getImagePath() {
                return imagePath;
            }
        }

    }

}
