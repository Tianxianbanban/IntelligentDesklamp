package com.tz.intelligentdesklamp.activity;

/**
 * 图片缩略图展示
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.bean.BasicData;
import com.tz.intelligentdesklamp.bean.JsonQueryBackgrounds;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.PictureShowAdapter;
import com.tz.intelligentdesklamp.adpter.about.ItemOfBitmapShow;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PictureShow extends AppCompatActivity {

    private List<JsonQueryBackgrounds.DownloadData.Content> pictureInfo=new ArrayList<>();//包含服务器返回的图片的具体信息
    private List<ItemOfBitmapShow> itemOfBitmapShows=new ArrayList<>();
    private List<Bitmap> picture=new ArrayList<>();//仅仅用于展示图片
    private ListView listViewOfShow;
    private PictureShowAdapter pictureShowAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_show);

        listViewOfShow=(ListView)findViewById(R.id.lv_picture_show);
        pictureShowAdapter=new PictureShowAdapter(PictureShow.this,R.layout.item_of_pictureshow,itemOfBitmapShows);

        //取出含有一连串图片信息的json数据
        String urlContentList = getSharedPreferences("picture_data", MODE_PRIVATE)
                .getString("pictureList", "picture_null");
        //再进行解析
        Gson gson=new Gson();
        pictureInfo=gson.fromJson(urlContentList,new TypeToken<List<JsonQueryBackgrounds.DownloadData.Content>>(){}.getType());
        //基本信息日志输出
        for (int i=0;i<pictureInfo.size();i++){
            int bid=pictureInfo.get(i).getBid();
            int uid =pictureInfo.get(i).getUid();
            int flag=-pictureInfo.get(i).getFlag();
            String imagePath=pictureInfo.get(i).getImagePath();
            Log.d("图片信息", "onCreate: id="+bid+"+"+uid+"+"+flag+"+"+imagePath);
        }
        /**
         * 根据url加载图片
         */
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i=0;i<pictureInfo.size();i++) {
                    //获取byte数组
                    byte[] pictureByte=HttpUtil.getBitmap(pictureInfo.get(i).getImagePath(),PictureShow.this);
                    //将字节数组转化为bitmap
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureByte, 0, pictureByte.length);
                    picture.add(bitmap);

                    ItemOfBitmapShow itemOfBitmapShow=new ItemOfBitmapShow();
                    itemOfBitmapShow.setBid(pictureInfo.get(i).getBid());
                    itemOfBitmapShow.setUid(pictureInfo.get(i).getUid());
                    itemOfBitmapShow.setFlag(pictureInfo.get(i).getFlag());
                    itemOfBitmapShow.setBitmap(bitmap);
                    itemOfBitmapShows.add(itemOfBitmapShow);
                    //关于pictureInfo
                }

                //通过imageview与listview，设置图片
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listViewOfShow.setAdapter(pictureShowAdapter);
                    }
                });

            }
        }).start();

        //图片展示listview的点击事件
        listViewOfShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//点击将照片设置为背景
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ItemOfBitmapShow itemOfBitmapShow=itemOfBitmapShows.get(position);
                Toast.makeText(PictureShow.this,"点击了图片的系统id"
                        +itemOfBitmapShow.getFlag()+"图片id是"+itemOfBitmapShow.getBid(),Toast.LENGTH_SHORT).show();
                //点击选择作为背景未实现
            }
        });
        //长按删除照片
        listViewOfShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                ItemOfBitmapShow itemOfBitmapShow=itemOfBitmapShows.get(position);
                RequestBody requestBody=new FormBody.Builder()
                        .add("bid",String.valueOf(itemOfBitmapShow.getBid()))
                        .build();
                HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getDeleteBackUrl(), PictureShow.this, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtil.showWrong(PictureShow.this,"服务器故障！");
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData=response.body().string();
                        //解析
                        Gson gson=new Gson();
                        BasicData basicData =gson.fromJson(responseData,new TypeToken<BasicData>(){}.getType());
                        if (basicData.getCode()==0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpUtil.showSuccess(PictureShow.this,"删除成功！");
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpUtil.showSuccess(PictureShow.this,"删除失败！");
                                }
                            });
                        }
                    }
                });
                return false;
            }
        });

    }
}
