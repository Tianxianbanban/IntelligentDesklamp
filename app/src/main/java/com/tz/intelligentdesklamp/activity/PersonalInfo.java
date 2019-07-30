package com.tz.intelligentdesklamp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.about.ItemOfPersonalInfo;
import com.tz.intelligentdesklamp.adpter.PersonalInfoAdapeter;
import com.tz.intelligentdesklamp.bean.BasicData;
import com.tz.intelligentdesklamp.util.use_never.FileUpload;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 个人信息
 * 修改后根据返回数据替换信息内容
 */

public class PersonalInfo extends AppCompatActivity {
    final int IMAGE_CHOOSE=11;

    private List<ItemOfPersonalInfo> itemOfPersonalInfos=new ArrayList<>();
    private LinearLayout personalInfo_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        personalInfo_head=(LinearLayout)findViewById(R.id.personalInfo_head);

        //头像部分的点击事件
        personalInfo_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开相册
                Toast.makeText(PersonalInfo.this, "想要获取照片", Toast.LENGTH_SHORT).show();
                //动态申请权限
                if (ContextCompat.checkSelfPermission(PersonalInfo.this,Manifest.permission
                        .WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PersonalInfo.this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //打开本地图库
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, IMAGE_CHOOSE);//待返回数据
                }
                //上传文件


            }
        });


        /**
         * 个人资料项目列表适配
         */
        initItem();//处理信息
        PersonalInfoAdapeter personalInfoAdapeter=new PersonalInfoAdapeter(
                PersonalInfo.this,R.layout.item_of_personalinfo,itemOfPersonalInfos);
        ListView listView=(ListView)findViewById(R.id.lv_personalinfo_text);
        listView.setAdapter(personalInfoAdapeter);

        //个人信息项目的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ItemOfPersonalInfo itemOfPersonalInfo=itemOfPersonalInfos.get(position);
                Toast.makeText(PersonalInfo.this,"点击了"
                        +itemOfPersonalInfo.getItem(),Toast.LENGTH_SHORT).show();
                //创建意图
                Intent changeContent=new Intent(PersonalInfo.this,ChangeContentInput.class);
                changeContent.putExtra("item",itemOfPersonalInfo.getItem());
                changeContent.putExtra("content",itemOfPersonalInfo.getContent());
                //点击进行修改
                switch (itemOfPersonalInfo.getItem()){
                    case "昵称":
                        startActivityForResult(changeContent,0);
                        break;
                    case "性别":
                        startActivityForResult(changeContent,1);
                        break;
                    case "年龄":
                        startActivityForResult(changeContent,2);
                        break;
                    case "地区":
                        startActivityForResult(changeContent,5);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 处理修改活动返回信息
     */
        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (resultCode == RESULT_OK) {
                SharedPreferences.Editor editor = getSharedPreferences("userInfo_data", MODE_PRIVATE).edit();
                String returnData = data.getStringExtra("return_data");
                switch (requestCode) {
                    case 0:
                        editor.putString("nickName", returnData);
                        itemOfPersonalInfos.get(0).setContent(returnData);
                        break;
                    case 1://性别这里注意
                        if (returnData.equals("男")) {
                            editor.putInt("sex", 0);
                        } else if (returnData.equals("女")) {
                            editor.putInt("sex", 1);
                        } else {
                            editor.putInt("sex", -1);
                        }
                        itemOfPersonalInfos.get(1).setContent(returnData);
                        break;
                    case 2:
                        editor.putString("age", returnData);
                        itemOfPersonalInfos.get(2).setContent(returnData);
                        break;
                    case 5:
                        editor.putString("region", returnData);
                        itemOfPersonalInfos.get(5).setContent(returnData);
                        break;
                    case IMAGE_CHOOSE://关于本地图库选择照片
                            HttpUtil.showSuccess(PersonalInfo.this, "点击了照片");
                            //Parcelable photo=data.getParcelableExtra("data");//
                            //初步判断数据
                            if (data==null||data.getData()==null){//如果返回数据为空
                                HttpUtil.showWrong(PersonalInfo.this,"照片获取失败");
                                return;
                            }
                            //处理数据
                            final Uri uri=data.getData();//uri
                            String path=FileUpload.getImagePath(uri,null,PersonalInfo.this);//获取照片真实路径
                            final File file=new File(path);//有关file的处理
                            //对于文件大小的判断
                            long fileSize=FileUpload.getFileSizes(file);
                            //文件不大于5M即可上传
                            if (file != null && file.exists() && (FileUpload.isMoreThanFive(fileSize)== 0)){
                                //关于requestbody
                                MultipartBody.Builder requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM);
                                RequestBody body=RequestBody.create(MediaType.parse("image/*"),file);//MediaType.parse()里面是上传的文件类型
                                requestBody.addFormDataPart("photo",file.getName(),body);//请求键值要注意
                                //发起网络请求上传图片
                                HttpUtil.sendOkHttpRequestWithTokenAndHead(InfoSave.getAlterheadPortraitUrl(), PersonalInfo.this, file, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                HttpUtil.showWrong(PersonalInfo.this,"上传失败！");
                                            }
                                        });
                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responseData=response.body().string();
                                        try{
                                            if (response.code()==200){//若正常返回
                                                Gson gson=new Gson();
                                                BasicData basicData=gson.fromJson(responseData,BasicData.class);
                                                if (basicData.getCode()==0){
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            HttpUtil.showSuccess(PersonalInfo.this,"上传成功！");
                                                        }
                                                    });
                                                }
                                            }else{
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        HttpUtil.showSuccess(PersonalInfo.this,"服务器故障！");
                                                    }
                                                });
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }else {
                                Toast.makeText(PersonalInfo.this,"请将文件压缩至5M以下",Toast.LENGTH_SHORT).show();
                            }
                }
                PersonalInfoAdapeter personalInfoAdapeter = new PersonalInfoAdapeter(
                        PersonalInfo.this, R.layout.item_of_personalinfo, itemOfPersonalInfos);
                ListView listView = (ListView) findViewById(R.id.lv_personalinfo_text);
                listView.setAdapter(personalInfoAdapeter);
            }
        }

    /**
     * 方法调用部分
     */
    //获取到的信息处理
    private void initItem(){
        //取出信息
        int id=getSharedPreferences("userInfo_data", MODE_PRIVATE).getInt("id", 0);
        String age=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("age", "0");
        int sex=getSharedPreferences("userInfo_data", MODE_PRIVATE).getInt("sex", 1);//默认为女
        String phoneNum=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("phoneNum", "未填写");
        String email=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("email", "未填写");
        String region=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("region", "未填写");
        String imagePath=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("imagePath", "未填写");
        String nickName=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("nickName", "未填写");
        String backgroundPath=getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("backgroundPath", "未填写");

        String sexcontent;
        if (sex==0){
            sexcontent="男";
        }else if (sex==1){
            sexcontent="女";
        }else {
            sexcontent="未填写";
        }
        ItemOfPersonalInfo infoE=new ItemOfPersonalInfo("昵称",nickName,R.drawable.right);
        itemOfPersonalInfos.add(infoE);
        ItemOfPersonalInfo infoF=new ItemOfPersonalInfo("性别",sexcontent,R.drawable.right);
        itemOfPersonalInfos.add(infoF);
        ItemOfPersonalInfo infoA=new ItemOfPersonalInfo("年龄",age,R.drawable.right);
        itemOfPersonalInfos.add(infoA);
        ItemOfPersonalInfo infoB=new ItemOfPersonalInfo("手机",phoneNum,R.drawable.right);
        itemOfPersonalInfos.add(infoB);
        ItemOfPersonalInfo infoC=new ItemOfPersonalInfo("邮箱",email,R.drawable.right);
        itemOfPersonalInfos.add(infoC);
        ItemOfPersonalInfo infoD=new ItemOfPersonalInfo("地区",region,R.drawable.right);
        itemOfPersonalInfos.add(infoD);
    }



//        /**
//         * 请求获取用户信息
//         */
//        int idcontent=getSharedPreferences("login_data", MODE_PRIVATE).getInt("id", 0);
//        final RequestBody requestBody=new FormBody.Builder().add("id",String.valueOf(idcontent)).build();
//        HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getGetUserInfoUrl(),PersonalInfo.this, requestBody, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                HttpUtil.showWrong(PersonalInfo.this,"服务器故障！");
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData=response.body().string();
//                //解析
//                Gson gson=new Gson();
//                GetUserInfo getUserInfo=gson.fromJson(responseData,new TypeToken<GetUserInfo>(){}.getType());
//
//                if (getUserInfo.getCode()==0){
//                    //解析具体数据
//                    int id=getUserInfo.getData().getUserInfo().getId();
//                    String age=getUserInfo.getData().getUserInfo().getAge();//
//                    boolean sex=getUserInfo.getData().getUserInfo().isSex();//性别是int
//                    String phoneNum=getUserInfo.getData().getUserInfo().getPhoneNum();
//                    String email=getUserInfo.getData().getUserInfo().getEmail();
//                    String region=getUserInfo.getData().getUserInfo().getRegion();
//                    String imagePath=getUserInfo.getData().getUserInfo().getImagePath();
//                    String nickName=getUserInfo.getData().getUserInfo().getNickName();
//                    String backgroundPath=getUserInfo.getData().getUserInfo().getBackgroundPath();
//
//                    //将获取到的个人信息进行保存
//                    SharedPreferences.Editor editor=getSharedPreferences("userInfo_data",MODE_PRIVATE).edit();
//                    editor.putInt("id",id);
//                    editor.putString("age",age);
//                    if (sex==true){
//                        editor.putInt("sex",1);
//                    }else if (sex==false){
//                        editor.putInt("sex",0);
//                    }else {
//                        editor.putInt("sex",-1);
//                    }
//                    editor.putString("phoneNum",phoneNum);
//                    editor.putString("email",email);
//                    editor.putString("region",region);
//                    editor.putString("imagePath",imagePath);
//                    editor.putString("nickName",nickName);
//                    editor.putString("backgroundPath",backgroundPath);
//                    editor.apply();
//                    }
//            }
//        });

}
