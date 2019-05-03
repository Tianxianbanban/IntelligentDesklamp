package com.tz.intelligentdesklamp.bean;

public class CurrentInfoBean {

    /**
     * code : 0
     * msg : null
     * data : {"environmentInfoViewObject":{"brightness":26,"noise":53,"temperature":13,"humidity":6,"musicId":4,"workTime":1044,"brightnessChange":20,"noiseChange":18,"temperatureChange":-1,"humidityChange":-7}}
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
         * environmentInfoViewObject : {"brightness":26,"noise":53,"temperature":13,"humidity":6,"musicId":4,"workTime":1044,"brightnessChange":20,"noiseChange":18,"temperatureChange":-1,"humidityChange":-7}
         */

        private EnvironmentInfoViewObjectBean environmentInfoViewObject;

        public EnvironmentInfoViewObjectBean getEnvironmentInfoViewObject() {
            return environmentInfoViewObject;
        }

        public void setEnvironmentInfoViewObject(EnvironmentInfoViewObjectBean environmentInfoViewObject) {
            this.environmentInfoViewObject = environmentInfoViewObject;
        }

        public static class EnvironmentInfoViewObjectBean {
            /**
             * brightness : 26
             * noise : 53
             * temperature : 13
             * humidity : 6
             * musicId : 4
             * workTime : 1044
             * brightnessChange : 20
             * noiseChange : 18
             * temperatureChange : -1
             * humidityChange : -7
             */

            private int brightness;
            private int noise;
            private int temperature;
            private int humidity;
            private int musicId;
            private int workTime;
            private int brightnessChange;
            private int noiseChange;
            private int temperatureChange;
            private int humidityChange;

            public int getBrightness() {
                return brightness;
            }

            public void setBrightness(int brightness) {
                this.brightness = brightness;
            }

            public int getNoise() {
                return noise;
            }

            public void setNoise(int noise) {
                this.noise = noise;
            }

            public int getTemperature() {
                return temperature;
            }

            public void setTemperature(int temperature) {
                this.temperature = temperature;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }

            public int getMusicId() {
                return musicId;
            }

            public void setMusicId(int musicId) {
                this.musicId = musicId;
            }

            public int getWorkTime() {
                return workTime;
            }

            public void setWorkTime(int workTime) {
                this.workTime = workTime;
            }

            public int getBrightnessChange() {
                return brightnessChange;
            }

            public void setBrightnessChange(int brightnessChange) {
                this.brightnessChange = brightnessChange;
            }

            public int getNoiseChange() {
                return noiseChange;
            }

            public void setNoiseChange(int noiseChange) {
                this.noiseChange = noiseChange;
            }

            public int getTemperatureChange() {
                return temperatureChange;
            }

            public void setTemperatureChange(int temperatureChange) {
                this.temperatureChange = temperatureChange;
            }

            public int getHumidityChange() {
                return humidityChange;
            }

            public void setHumidityChange(int humidityChange) {
                this.humidityChange = humidityChange;
            }
        }
    }
}
