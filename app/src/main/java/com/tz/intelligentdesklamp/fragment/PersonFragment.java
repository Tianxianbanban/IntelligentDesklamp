package com.tz.intelligentdesklamp.fragment;

/**
 * 我的
 */

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.bean.GetUserInfo;
import com.tz.intelligentdesklamp.bean.JsonQueryBackgrounds;
import com.tz.intelligentdesklamp.activity.PictureShow;
import com.tz.intelligentdesklamp.util.use_never.GetFileSize;
import com.tz.intelligentdesklamp.bean.JsonUploadBackground;
import com.tz.intelligentdesklamp.activity.PersonalInfo;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class PersonFragment extends BaseFragment {
    String TAG="PersonFragment";

    protected static final int UP_SUCCESS = 21;//上传成功
    protected static final int UP_ERROR = 22;//长传失败
    protected static final int UP_UNKOWN = 23;//未知错误
    protected static final int UP_TOOBIG = 24;//未知错误
    protected static final int SHOW_SUCCESS = 31;//获取成功
    protected static final int SHOW_ERROR = 32;//获取失败
    protected static final int SHOW_UNKOWN = 33;//未知错误
    protected static final int INFO_CHANGE = 41;//未知错误

    final int IMAGE_CHOOSE = 66;//打开本地图库的返回数据请求码

    public ImageView background;//背景图片
    private CircleImageView civ_person_head;//头像
    private Button changeBack;//跟换背景右上角按钮
    private TextView tx_frag_person_usernickname;//用户昵称

    //用于返回主线程中界面变化
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UP_SUCCESS:
                    HttpUtil.showSuccess(getContext(), "上传成功！");
                    break;
                case UP_ERROR:
                    HttpUtil.showWrong(getContext(), "仅限向服务器上传5张图片！");
                    break;
                case UP_UNKOWN://未知错误
                    HttpUtil.showWrong(getContext(), "出现未知错误！");
                    break;
                case UP_TOOBIG:
                    HttpUtil.showWrong(getContext(), "文件超过5M，需要进行压缩！");
                    break;
                case SHOW_SUCCESS:
                    HttpUtil.showSuccess(getContext(), "获取成功！");
                    break;
                case SHOW_ERROR:
                    HttpUtil.showWrong(getContext(), "获取失败！");
                    break;
                case SHOW_UNKOWN://未知错误
                    HttpUtil.showWrong(getContext(), "服务器故障！");
                    break;
                case INFO_CHANGE://修改昵称
                    tx_frag_person_usernickname.setText((String)msg.obj);
                    Log.d(TAG, "handleMessage: changeUserInfo_nickName "+(String)msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.fragment_person, null);

        //控件初始化
        background=(ImageView)view.findViewById(R.id.image_person_background);
        civ_person_head=(CircleImageView)view.findViewById(R.id.civ_person_head);
        changeBack = (Button) view.findViewById(R.id.bt_frag_person_changeImage);
        tx_frag_person_usernickname=(TextView)view.findViewById(R.id.tx_frag_person_usernickname);

        //获取数据进行界面初始设置
        initData();

        //点击事件
        changeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();//底部选项对话框弹出
            }
        });

        //item选项
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.design_navigation_view);
        disableNavigationViewScrollbars(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.personfunc_info://点击进入个人资料显示
                        Toast.makeText(getContext(), "资料", Toast.LENGTH_SHORT).show();
                        Intent intent_personalInfo=new Intent(getContext(),PersonalInfo.class);
                        startActivity(intent_personalInfo);
                        break;
                    case R.id.personfunc_signin:
                        Toast.makeText(getContext(), "打卡", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.personfunc_mylamp:
                        Toast.makeText(getContext(), "台灯", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.personfunc_setting:
                        Toast.makeText(getContext(), "设置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.personfunc_help:
                        Toast.makeText(getContext(), "帮助", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.personfunc_aboutourselves:
                        Toast.makeText(getContext(), "关于", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.personfunc_logout:
                        Toast.makeText(getContext(), "退出", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return view;
    }

    //去除nvagationView的滚动条
    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }


    //展示底部对话框
    private void show() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_menu, null);
        Dialog dialog = new Dialog(getContext());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setContentView(view);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();

        //底部选项
        TextView upload = (TextView) view.findViewById(R.id.upload);//从本地上传至系统图库
        TextView change= (TextView) view.findViewById(R.id.change);//从系统图库选择图片进行更换

        //点击事件
        upload.setOnClickListener(new View.OnClickListener() {//上传图片
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "想要获取照片", Toast.LENGTH_SHORT).show();
                //动态申请权限
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission
                        .WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //打开本地图库
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, IMAGE_CHOOSE);//待返回数据
                }

            }
        });
        change.setOnClickListener(new View.OnClickListener() {//展示图片与选择图片进行替换
            @Override
            public void onClick(View view) {
//                发起请求
                HttpUtil.sendOkHttpRequestWithToken(InfoSave.getBackQueryUrl(), getContext(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(SHOW_UNKOWN);//未知错误
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData=response.body().string();
                        try{
                            //解析
                            Gson gson=new Gson();
                            JsonQueryBackgrounds jsonQueryBackgrounds=gson.fromJson(responseData,
                                    new TypeToken<JsonQueryBackgrounds>(){}.getType());
                            if (jsonQueryBackgrounds.getCode()==0){//得到正常返回数据
                                //将list部分倒退为json数据
                                String pictureListJson = gson.toJson(jsonQueryBackgrounds.getData().getBackgrounds());
                                //将这段数据存储，将系统图片的url保存，之后使用
                                SharedPreferences.Editor pictureEditor=getContext().getSharedPreferences("picture_data",MODE_PRIVATE).edit();
                                pictureEditor.putString("pictureList",pictureListJson);
                                pictureEditor.apply();
                                //跳转，展示图片
                                Intent pictureShowIntent=new Intent(getContext(),PictureShow.class);
                                startActivity(pictureShowIntent);
                                handler.sendEmptyMessage(SHOW_SUCCESS);//跳转活动
                            }else{
                                handler.sendEmptyMessage(SHOW_UNKOWN);//服务器故障
                            }

                        }catch (Exception e){

                        }

                    }
                });
            }
        });

    }

    //信息修改
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case IMAGE_CHOOSE://关于本地图库选择照片
                if (resultCode == RESULT_OK) {
                    HttpUtil.showSuccess(getContext(), "点击了照片");
                    //Parcelable photo=data.getParcelableExtra("data");//
                    //初步判断数据
                    if (data==null||data.getData()==null){//如果返回数据为空
                        HttpUtil.showWrong(getContext(),"照片获取失败");
                        return;
                    }
                    //处理数据
                    final Uri uri=data.getData();
                    String path=getImagePath(uri,null);
                    final File file=new File(path);//有关file的处理
                    /**
                     * 开启线程上传照片
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //对照片大小进行判断
                            long size = 0;
                            try {
                                size = GetFileSize.getFileSizes(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int isMoreThanFiveM = GetFileSize.isMoreThanFive(size);
                            //文件存在且大小不超过5M，可以上传
                            if (file != null && file.exists() && (isMoreThanFiveM == 0)) {
                                String responseData = HttpUtil.sendFileRequestWithOkHttpAndToken(InfoSave.getUpBackUrl(),getContext(),file);
                                if (responseData == null) {
                                    //对用户进行提示
                                    handler.sendEmptyMessage(UP_UNKOWN);//未知原因
                                } else {
                                    //解析返回Json数据
                                    Gson gson = new Gson();
                                    JsonUploadBackground jsonUploadBackground = gson.fromJson(responseData, new TypeToken<JsonUploadBackground>() {}.getType());
                                    if (jsonUploadBackground.getCode() == 0) {
                                        //将照片上传返回的bid保存，之后使用
                                        int bid = jsonUploadBackground.getData().getBid();
                                        SharedPreferences.Editor tokenEditor = getContext().getSharedPreferences("bid_data", MODE_PRIVATE).edit();
                                        tokenEditor.putInt("bid",bid);
                                        tokenEditor.apply();
                                        handler.sendEmptyMessage(UP_SUCCESS);
                                    } else if (jsonUploadBackground.getCode()==-1){//插入失败，提示上限5张
                                        handler.sendEmptyMessage(UP_ERROR);
                                    }else{
                                        handler.sendEmptyMessage(UP_UNKOWN);//出现未知错误
                                    }
                                    //Log.d("上传背景图片返回内容", "id: " + jsonUploadBackground.getData().getBid());//这句无法执行，不能保证data部分有数据，以至程序出错
                                }
                            } else {//如果文件过大
                                handler.sendEmptyMessage(UP_TOOBIG);//向用户发出信息，进行压缩
                            }
                        }
                    }).start();


//                    Bitmap bitmap= null;
//                    try {
//                        //根据uri转换照片
//                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //照片处理，裁剪
//                    background.setImageBitmap(bitmap);//将照片设置进背景
                    /**
                     * 将照片设置进背景还需下载一次照片，使用服务器存储的照片
                     * 将照片再进行一次存储
                     */
                    break;
                }
        }
    }

    /**
     * 获取照片真实路径
     */
    public String getImagePath(Uri uri,String selection){
        String path=null;
        //根据Uri和selection获取照片的真实路径
        Cursor cursor=getContext().getContentResolver()
                .query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void setBackground(ImageView background) {
        this.background = background;
    }
    public ImageView getBackground() {
        return background;
    }

    @Override
    protected void initData() {
        super.initData();
        /**
         * 请求获取用户信息
         */
        int id=getContext().getSharedPreferences("login_data", MODE_PRIVATE).getInt("id", 0);//取出用户id
        final RequestBody requestBody=new FormBody.Builder().add("id",String.valueOf(id)).build();
        HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getGetUserInfoUrl(),getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HttpUtil.showWrong(getContext(),"网络断开或者服务器故障！");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.d(TAG, "onResponse: "+responseData);
                if (response.code()==200){
                    //解析
                    Gson gson=new Gson();
                    GetUserInfo getUserInfo=gson.fromJson(responseData,new TypeToken<GetUserInfo>(){}.getType());
                    if (getUserInfo.getCode()==0){
                        //解析具体数据
                        int id=getUserInfo.getData().getUserInfo().getId();
                        String age=getUserInfo.getData().getUserInfo().getAge();//
                        boolean sex=getUserInfo.getData().getUserInfo().isSex();//性别是int
                        String phoneNum=getUserInfo.getData().getUserInfo().getPhoneNum();
                        String email=getUserInfo.getData().getUserInfo().getEmail();
                        String region=getUserInfo.getData().getUserInfo().getRegion();
                        String imagePath=getUserInfo.getData().getUserInfo().getImagePath();
                        String nickName=getUserInfo.getData().getUserInfo().getNickName();
                        String backgroundPath=getUserInfo.getData().getUserInfo().getBackgroundPath();
                        if(backgroundPath!=null){
                            //加载背景
                            Glide.with(PersonFragment.this).load(backgroundPath).into(background);
                        }
                        Log.d(TAG, "onResponse: information "+"id "+id+" age "+age+" sex "+sex+" phoneNum "+phoneNum+" email "+email+" region "+" region+imagePath "+imagePath+" nickName "+nickName+" backgroundPath "+backgroundPath);
                        //昵称则进行修改
                        Message message=new Message();
                        message.what=INFO_CHANGE;
                        message.obj=nickName;
                        handler.sendMessage(message);//传递修改昵称
                        //其他的获取到的个人信息进行保存
                        SharedPreferences.Editor editor=getContext().getSharedPreferences("userInfo_data",MODE_PRIVATE).edit();
                        editor.putInt("id",id);
                        editor.putString("age",age);
                        if (sex==true){
                            editor.putInt("sex",1);
                        }else if (sex==false){
                            editor.putInt("sex",0);
                        }else {
                            editor.putInt("sex",-1);
                        }
                        editor.putString("phoneNum",phoneNum);
                        editor.putString("email",email);
                        editor.putString("region",region);
                        editor.putString("imagePath",imagePath);
                        editor.putString("nickName",nickName);
                        editor.putString("backgroundPath",backgroundPath);
                        editor.apply();
                    }
                }



            }
        });
    }
}






