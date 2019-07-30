package com.tz.intelligentdesklamp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.bean.BasicData;
import com.tz.intelligentdesklamp.util.network.HttpUtil;
import com.tz.intelligentdesklamp.util.network.InfoSave;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeContentInput extends AppCompatActivity {
    String TAG="ChangeContentInput";


    private Button bt_change_content_back;
    private TextView tx_change_content_item;//显示修改项目
    private Button bt_change_content_save;
    private EditText et_change_content;
    private RadioGroup rbtgroup_change_sex;
    private RadioButton rbt_change_sex_male;
    private RadioButton rbt_change_sex_female;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_content_input);

        //将状态栏与标题栏融合
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        bt_change_content_back = (Button) findViewById(R.id.bt_change_content_back);
        tx_change_content_item = (TextView) findViewById(R.id.tx_change_content_item);
        et_change_content = (EditText) findViewById(R.id.et_change_content);
        bt_change_content_save = (Button) findViewById(R.id.bt_change_content_save);

        rbtgroup_change_sex = (RadioGroup) findViewById(R.id.rbtgroup_change_sex);
        rbt_change_sex_male = (RadioButton) findViewById(R.id.rbt_change_sex_male);
        rbt_change_sex_female = (RadioButton) findViewById(R.id.rbt_change_sex_female);


        Intent intent = getIntent();
        final String item = intent.getStringExtra("item");//取出列表项目
        final String content = intent.getStringExtra("content");//取出内容
        tx_change_content_item.setText("修改" + item);
        /**
         * 对于性别修改单独处理
         */
        if (item.equals("性别")) {
            et_change_content.setVisibility(View.GONE);
            if (content.equals("true")) {
                rbt_change_sex_male.setChecked(true);
            } else if (content.equals("false")) {
                rbt_change_sex_female.setChecked(true);
            }
        } else {
            rbtgroup_change_sex.setVisibility(View.GONE);
            et_change_content.setText(content);
            et_change_content.setSelection(content.length());//将光标移动到文本末尾
        }


        //点击返回
        bt_change_content_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //点击保存
        bt_change_content_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dataChanged = et_change_content.getText().toString();
                Log.d(TAG, "onClick: getDataChanged" + dataChanged);
                //再次取出数据
                boolean sex = getSharedPreferences("userInfo_data", MODE_PRIVATE).getBoolean("sex", false);//默认为女
                String age = getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("age", "0");
                String region = getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("region", "未填写");
                String nickName = getSharedPreferences("userInfo_data", MODE_PRIVATE).getString("nickName", "未填写");

                switch (item) {
                    case "昵称":
                        nickName = dataChanged;
                        break;
                    case "性别":
                        if (rbt_change_sex_male.isChecked()) {
                            sex = true;
                        } else if (rbt_change_sex_female.isChecked()) {
                            sex = false;
                        } else {
                            sex = false;
                        }
                        break;
                    case "年龄":
                        age = dataChanged;
                        break;
                    case "地区":
                        region = dataChanged;
                        break;
                    default:
                        break;
                }

                RequestBody requestBody = new FormBody.Builder()
                        .add("sex", sex ? String.valueOf(1) : String.valueOf(0))
                        .add("age", age)
                        .add("region", region)
                        .add("nickName", nickName)
                        .build();
                Log.d(TAG, "个人信息修改-----requestBody:"+" sex="+ (sex ? String.valueOf(0) : String.valueOf(1))+" age="+age+" region="+region+" nickName="+nickName);

                HttpUtil.sendOkHttpRequestWithTokenAndBody(InfoSave.getAlterUserInfoUrl(), getApplicationContext(), requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();//返回可能跟请求的内容格式有关，格式错误可能不能正常上传服务器
                        Log.d(TAG, "信息修改返回--onResponse:"+responseData);
                        try {
                            Gson gson = new Gson();
                            BasicData basicData = gson.fromJson(responseData, BasicData.class);
                            if (basicData.getCode() == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangeContentInput.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

                Intent intent = getIntent();
                if (item.equals("性别")) {
                    if (rbt_change_sex_male.isChecked()) {
                        intent.putExtra("return_data", "true");
                    } else if (rbt_change_sex_female.isChecked()) {
                        intent.putExtra("return_data", "false");
                    }
                } else {
                    intent.putExtra("return_data", et_change_content.getText().toString());
                }
                setResult(RESULT_OK, intent);
                finish();
            }

        });

    }
}
