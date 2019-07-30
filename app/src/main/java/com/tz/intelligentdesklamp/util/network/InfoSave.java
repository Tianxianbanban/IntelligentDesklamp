package com.tz.intelligentdesklamp.util.network;

/**
 * 存储各个接口的地址
 */

public class InfoSave {
    //登录
    static String loginUrl=new String("http://134.175.68.103:9095/login");
    //注册
    static String registerUrl=new String("http://134.175.68.103:9095/register");
    //获取验证码接口
    static String getcodeUrl=new String("http://134.175.68.103:9095/getcode");
    //获取用户信息
    static String getUserInfoUrl=new String("http://134.175.68.103:9095/getUserInfo");
    //上传自定义背景接口
    static String upBackUrl=new String("http://134.175.68.103:9095/uploadBackground");
    //获取用户背景接口
    static String backQueryUrl=new String("http://134.175.68.103:9095/queryBackgrounds");
    //删除背景
    static String deleteBackUrl=new String("http://134.175.68.103:9095/deleteBackground");
    //修改用户信息
    static String alterUserInfoUrl=new String("http://134.175.68.103:9095/alterUserInfo");
    //获取基本数据
    static String getBaseDataUrl=new String("http://134.175.68.103:9095/getBaseData");
    //获取所有任务
    static String getTasksUrl=new String("http://134.175.68.103:9095/getTasks");
    //修改用户头像
    static String alterheadPortraitUrl=new String("http://134.175.68.103:9095/alterheadPortrait");

    //获取专注信息
    static String getFocusDataUrl=new String("http://134.175.68.103:9095/getFocusData");
    //获取坐姿数据
    public static String getSittingPostureDataUrl="http://134.175.68.103:9095/getSittingPostureData";

    //获取学习时长
    public static String getStudyTimeDataUrl=new String("http://134.175.68.103:9095/getStudyTimeData");
    //获取得分数据
    public static String getMarkDataUrl=new String("http://134.175.68.103:9095/getMarkData");

    //番茄钟开始
    static String beginUrl=new String("http://134.175.68.103:9095/begin");
    //番茄钟结束
    static String endUrl=new String("http://134.175.68.103:9095/end");


    public static String getUpBackUrl() {
        return upBackUrl;
    }
    public static String getLoginUrl() {
        return loginUrl;
    }
    public static String getGetcodeUrl() {
        return getcodeUrl;
    }
    public static String getBackQueryUrl() {
        return backQueryUrl;
    }
    public static String getDeleteBackUrl() {
        return deleteBackUrl;
    }
    public static String getGetUserInfoUrl() {
        return getUserInfoUrl;
    }
    public static String getAlterUserInfoUrl() {
        return alterUserInfoUrl;
    }
    public static String getGetBaseDataUrl() {
        return getBaseDataUrl;
    }
    public static String getGetTasksUrl() {
        return getTasksUrl;
    }
    public static String getAlterheadPortraitUrl() {
        return alterheadPortraitUrl;
    }

    public static String getGetFocusDataUrl() {
        return getFocusDataUrl;
    }

    public static String getGetSittingPostureDataUrl() {
        return getSittingPostureDataUrl;
    }

    public static String getGetMarkDataUrl() {
        return getMarkDataUrl;
    }

    public static String getGetStudyTimeDataUrl() {
        return getStudyTimeDataUrl;
    }

    public static String getBeginUrl() {
        return beginUrl;
    }

    public static String getEndUrl() {
        return endUrl;
    }
}
