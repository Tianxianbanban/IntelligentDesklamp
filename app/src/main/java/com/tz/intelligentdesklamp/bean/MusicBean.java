package com.tz.intelligentdesklamp.bean;

import java.util.List;

public class MusicBean {

    /**
     * code : 0
     * msg : null
     * data : {"musics":[{"id":3,"musicName":"music","uid":null,"musicUrl":"https://smartdesklamp-1257009269.cos.ap-guangzhou.myqcloud.com/music/18934698676_musicName_99990247-8d45-4658-b7d8-43baacaf4fba","flag":"1"},{"id":4,"musicName":"music.mp3","uid":null,"musicUrl":"https://smartdesklamp-1257009269.cos.ap-guangzhou.myqcloud.com/music/18934698676_musicName_4e0068e5-08ac-48e0-af56-f20910308f53music.mp3","flag":"1"}],"currentMusic":4}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * musics : [{"id":3,"musicName":"music","uid":null,"musicUrl":"https://smartdesklamp-1257009269.cos.ap-guangzhou.myqcloud.com/music/18934698676_musicName_99990247-8d45-4658-b7d8-43baacaf4fba","flag":"1"},{"id":4,"musicName":"music.mp3","uid":null,"musicUrl":"https://smartdesklamp-1257009269.cos.ap-guangzhou.myqcloud.com/music/18934698676_musicName_4e0068e5-08ac-48e0-af56-f20910308f53music.mp3","flag":"1"}]
         * currentMusic : 4
         */

        private int currentMusic;
        private List<MusicsBean> musics;

        public int getCurrentMusic() {
            return currentMusic;
        }

        public void setCurrentMusic(int currentMusic) {
            this.currentMusic = currentMusic;
        }

        public List<MusicsBean> getMusics() {
            return musics;
        }

        public void setMusics(List<MusicsBean> musics) {
            this.musics = musics;
        }

        public static class MusicsBean {
            /**
             * id : 3
             * musicName : music
             * uid : null
             * musicUrl : https://smartdesklamp-1257009269.cos.ap-guangzhou.myqcloud.com/music/18934698676_musicName_99990247-8d45-4658-b7d8-43baacaf4fba
             * flag : 1
             */

            private int id;
            private String musicName;
            private Object uid;
            private String musicUrl;
            private String flag;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMusicName() {
                return musicName;
            }

            public void setMusicName(String musicName) {
                this.musicName = musicName;
            }

            public Object getUid() {
                return uid;
            }

            public void setUid(Object uid) {
                this.uid = uid;
            }

            public String getMusicUrl() {
                return musicUrl;
            }

            public void setMusicUrl(String musicUrl) {
                this.musicUrl = musicUrl;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }
        }
    }
}
