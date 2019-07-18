package com.tz.intelligentdesklamp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.bean.JsonLogin;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;
import com.tz.intelligentdesklamp.util.network.NetWorkUtils;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText ed_register_userid;
    private EditText ed_register_authentication;
    private EditText ed_register_password;
    private Button bt_register_determine;
    private Button bt_register_handin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);//关于动画
        setContentView(R.layout.activity_register);

        ed_register_userid=(EditText)findViewById(R.id.ed_register_userid);
        ed_register_authentication=(EditText)findViewById(R.id.ed_register_authentication);
        ed_register_password=(EditText)findViewById(R.id.ed_login_password);
        bt_register_determine=(Button) findViewById(R.id.bt_register_determine);
        bt_register_handin=(Button) findViewById(R.id.bt_register_handin);

        ed_register_userid.setOnClickListener(this);
        ed_register_authentication.setOnClickListener(this);
        ed_register_password.setOnClickListener(this);
        bt_register_determine.setOnClickListener(this);
        bt_register_handin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_register_determine:
                String userId=ed_register_userid.getText().toString().trim();
                if (userId.equals("")){//电话号码为空
                    Toast.makeText(Register.this,"请输入用户账号",Toast.LENGTH_SHORT).show();
                }else if (userId.length()>11){
                    Toast.makeText(Register.this,"手机号码过长！",Toast.LENGTH_SHORT).show();
                }else {
                    final RequestBody requestBody = new FormBody.Builder()
                            .add("phonenumber",userId)
                            .build();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String responseData=HttpUtil.sendOkHttpRequest(InfoSave.getGetcodeUrl(),requestBody);
                            Gson gson=new Gson();
                            //借用登录返回json格式
                            JsonLogin registerResponse=gson.fromJson(responseData,new TypeToken<JsonLogin>(){}.getType());
                            if (registerResponse.getCode()==0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Register.this,"已经发送短信验证码。",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if (registerResponse.getCode()==-1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Register.this,"用户已经存在！",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).start();
                }
                break;
            case R.id.bt_register_handin:
                final String name=ed_register_userid.getText().toString().trim();
                final String pass=NetWorkUtils.getMD5String(ed_register_password.getText().toString().trim());
                final String code=ed_register_authentication.getText().toString().trim();
                if (code.length()>4){
                    Toast.makeText(Register.this,"验证码超出字数限制。",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        RequestBody requestBodyHandin = new FormBody.Builder()
                            .add("username",name)
                            .add("password",pass)
                            .add("code",code)
                            .build();
                        @Override
                        public void run() {
                            String responseData=HttpUtil.sendOkHttpRequest(InfoSave.getGetcodeUrl(),requestBodyHandin);
                            Gson gson=new Gson();
                            //借用登录返回json格式
                            JsonLogin registerResponse=gson.fromJson(responseData,new TypeToken<JsonLogin>(){}.getType());
                            if (registerResponse.getCode()==0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Register.this,"注册成功，请返回登录。",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if (registerResponse.getCode()==-1){
                                Toast.makeText(Register.this,"用户已经存在！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                }

                break;
            default:
                break;
        }
    }
}
