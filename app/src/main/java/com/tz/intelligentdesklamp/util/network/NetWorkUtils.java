package com.tz.intelligentdesklamp.util.network;
/**
 * 网络状态判断
 * md5加密
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.math.BigInteger;
import java.security.MessageDigest;

public class NetWorkUtils {

    //对网络状态进行判断
    public static boolean isNetworkAvailable(Context context){
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager =(ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo =manager.getActiveNetworkInfo();//获取当前连接的可用网络
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();//返回网络可用的信息
        }
        return false;
    }

    //有关md5加密
    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
