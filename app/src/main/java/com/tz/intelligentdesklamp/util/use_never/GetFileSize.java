package com.tz.intelligentdesklamp.util.use_never;

/**
 * 判断文件大小
 */

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class GetFileSize {
    //获取文件长度
    public static long getFileSizes(File file) throws IOException {
        long size=0;
        if (file.exists()){
            FileInputStream fis=null;
            fis=new FileInputStream(file);
            size=fis.available();
            fis.close();
        }else {
            file.createNewFile();
            Log.d("文件信息", "文件不存在");
        }
        return size;
    }

    //转换文件大小
    public  static String FormentFileSize(long fileSize){
        DecimalFormat decimalFormat=new DecimalFormat("#.00");
        String fileSizeSring="";
        if (fileSize<1024){
            fileSizeSring=decimalFormat.format((double)fileSize)+"B";
        }else if (fileSize<1048576){
            fileSizeSring=decimalFormat.format((double)fileSize/1024)+"k";
        }else if (fileSize<1073741824){
            fileSizeSring=decimalFormat.format((double)fileSize/1048576)+"M";
        }else {
            fileSizeSring=decimalFormat.format((double)fileSize/1073741824)+"G";
        }
        return fileSizeSring;
    }

    //判断文件与5M大小的关系
    public static int isMoreThanFive(long fileSize){
        double fileSizeM=(double)fileSize/1048576;
        if (fileSizeM<5){
            return 0;
        }
        return 1;
    }
}
