package com.tz.intelligentdesklamp.util.use_never;

/**
 * 文件上传之前进行的处理
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileUpload {

    /**
     * 关于文件的大小判断
     */
    //获取文件长度
    public static long getFileSizes(File file){
        long size=0;
        if (file.exists()){
            FileInputStream fis=null;
            try {
                fis=new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                size=fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("文件信息", "文件不存在");
            }
        }
        return size;
    }

    //获取文件大小
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

    /**
     * 获取照片真实路径
     */
    public static String getImagePath(Uri uri, String selection, Context context){
        String path=null;
        //根据Uri和selection获取照片的真实路径
        Cursor cursor=context.getContentResolver()
                .query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
