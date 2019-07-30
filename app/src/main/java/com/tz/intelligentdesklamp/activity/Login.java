package com.tz.intelligentdesklamp.activity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.base.BaseActivity;
import com.tz.intelligentdesklamp.bean.JsonLogin;
import com.tz.intelligentdesklamp.MainActivity;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;
import com.tz.intelligentdesklamp.util.network.NetWorkUtils;
import com.tz.intelligentdesklamp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Login extends BaseActivity implements View.OnClickListener {

    private  static String token=null;
    private  static int id;

    protected static final int WHAT_SUCCESS=1;//登录成功
    protected static final int WHAT_ERROR=2;//密码错误
    protected static final int WHAT_UNKNOWN=3;//未知错误
    protected static final int WHAT_FAILURE=4;//失败状况

    private EditText et_login_userid;//用户账号
    private EditText et_login_password;//密码
    private Button bt_login_login;//登录按钮
    private TextView tx_login_register;//注册

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SUCCESS://返回正确，可以跳转活动
                    HttpUtil.showSuccess(Login.this,"登录成功!");
                    Intent intent=new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case WHAT_ERROR://密码错误
                    HttpUtil.showWrong(Login.this,"密码或账号错误，请确认信息！");
                case WHAT_UNKNOWN://未知错误
                    HttpUtil.showWrong(Login.this,"出现未知错误。");
                    break;
                case WHAT_FAILURE://网络连接问题
                    HttpUtil.showWrong(Login.this,"登录失败，请检查网络！");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_userid=(EditText) findViewById(R.id.ed_login_userid);
        et_login_password=(EditText) findViewById(R.id.ed_login_password);
        bt_login_login=(Button) findViewById(R.id.bt_login_login);
        tx_login_register=(TextView) findViewById(R.id.tx_login_register);
        //点击事件设置
        et_login_userid.setOnClickListener(this);
        et_login_password.setOnClickListener(this);
        bt_login_login.setOnClickListener(this);
        tx_login_register.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //点击登录
            case R.id.bt_login_login:
                if(!NetWorkUtils.isNetworkAvailable(Login.this)){//网络未连接
                    handler.sendEmptyMessage(WHAT_FAILURE);
                    return;
                }else{
                    String userId= et_login_userid.getText().toString().trim();//获取用户输入信息
                    String password= et_login_password.getText().toString().trim();
                    String md5Password = NetWorkUtils.getMD5String(password);//进行md5加密
                    //格式判断
                    if(userId.equals("")||password.equals("")){
                        HttpUtil.showWrong(Login.this,"账号或者密码不能为空！");
                    }else if(userId.length()>11){
                        HttpUtil.showWrong(Login.this,"手机号码过长！");
                    }else{
                        //关于url
                        //final String url=InfoSave.getLoginUrl() +"?username="+userId+"&password="+md5Password;
                        RequestBody requestBody=new FormBody.Builder()
                                .add("username",userId)
                                .add("password",md5Password)
                                .build();
                        HttpUtil.sendOkHttpRequestWithBody(InfoSave.getLoginUrl(), requestBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                HttpUtil.showWrong(Login.this,"服务器故障");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData=response.body().string();
                                Log.d("responseData", "onResponse: "+responseData);
                                //解析
                                Gson gson=new Gson();
                                JsonLogin jsonLogin=gson.fromJson(responseData,new TypeToken<JsonLogin>(){}.getType());
                                if (jsonLogin.getCode()==0){//可以登录
                                    //保存token 与 id 信息
                                    String token=jsonLogin.getData().getToken();
                                    int id=jsonLogin.getData().getId();
                                    SharedPreferences.Editor editor=getSharedPreferences("login_data",MODE_PRIVATE).edit();
                                    editor.putString("token",token);
                                    editor.putInt("id",id);
                                    editor.apply();
                                    handler.sendEmptyMessage(WHAT_SUCCESS);//跳转活动
                                }else if (jsonLogin.getCode()==-1){
                                    handler.sendEmptyMessage(WHAT_ERROR);
                                }else{
                                    handler.sendEmptyMessage(WHAT_UNKNOWN);
                                }
                            }
                        });
                    }
                }
                //获取用户输入信息
                break;
            case R.id.tx_login_register:
                Intent registerIntent=new Intent(Login.this,Register.class);//跳转注册
                startActivity(registerIntent,ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                break;
        }
    }


}
