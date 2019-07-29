package com.tz.intelligentdesklamp.fragment;
/**
 * 计划
 */

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tz.intelligentdesklamp.MainActivity;
import com.tz.intelligentdesklamp.R;
import com.tz.intelligentdesklamp.adpter.MusicAdapter;
import com.tz.intelligentdesklamp.base.BaseFragment;
import com.tz.intelligentdesklamp.bean.CurrentInfoBean;
import com.tz.intelligentdesklamp.bean.MusicBean;
import com.tz.intelligentdesklamp.bean.NormalBean;
import com.tz.intelligentdesklamp.control.InitConfig;
import com.tz.intelligentdesklamp.control.MySyntherizer;
import com.tz.intelligentdesklamp.control.NonBlockSyntherizer;
import com.tz.intelligentdesklamp.listener.UiMessageListener;
import com.tz.intelligentdesklamp.util.AutoCheck;
import com.tz.intelligentdesklamp.util.OfflineResource;
import com.tz.intelligentdesklamp.util.network.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlanFragment extends BaseFragment implements View.OnClickListener {

    private static String url_switch = "http://134.175.68.103:9095/switch";
    private static String url_getCurrentInfo = "http://134.175.68.103:9095/getCurrentInfo";
    private static String url_getMusics = "http://134.175.68.103:9095/getMusics";
    private static String url_alterMusic = "http://134.175.68.103:9095/alterMusic";

    /**
     * proxy 可以用于音乐的边听边缓存
     */
    private HttpProxyCacheServer proxy = null;

    private HttpProxyCacheServer getProxy() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(getContext());
    }

    //定义手势检测器实例
    private GestureDetector detector;
    private PopupWindow currentInfoPopWindow; //下拉是实现当前信息的PopupWindow

    //currentInfoPopWindow中的4个泡泡
    private TextView tv_current_info_brightness;
    private TextView tv_current_info_noise;
    private TextView tv_current_info_temperature;
    private TextView tv_current_info_humidity;

    private Boolean modeIsShowBack;
    private Boolean brightnessIsShowBack;

    AnimatorSet modeRightOutSet;
    AnimatorSet modeLeftInSet;

    AnimatorSet brightnessRightOutSet;
    AnimatorSet brightnessLeftInSet;

    private LinearLayout ll_reset; //在音乐模块的上面，用于翻转正面
    private TextView tv_show_mode_status;


    /**
     *铃声选择部分
     *
     * mPopWindow为弹出的可选音乐列表
     *
     */
    private LinearLayout ll_music_front;
    private PopupWindow musicPopWindow;
    private RecyclerView rv_music;
    private MusicAdapter adapter;
    private List<MusicBean.DataBean.MusicsBean>  mList;
    private MediaPlayer mediaPlayer;

    /**
     * 模式选择部分
     *
     * fl_mode作为容器，包含ll_mode_front，ll_mode_back，点击切换
     * ll_mode_back中选择具体模式，其中有4个Button：
     * bt_mode_auto，bt_mode_first，bt_mode_second 和 bt_mode_third
     */
    private FrameLayout fl_mode;
    private LinearLayout ll_mode_front;
    private LinearLayout ll_mode_back;

    private Button bt_mode_auto;
    private Button bt_mode_first;
    private Button bt_mode_second;
    private Button bt_mode_third;


    /**
     * 亮度调节部分
     *
     * fl_brightness作为容器，包含ll_brightness_front，ll_brightness_back，点击切换
     * ll_brightness_back中有一个sb_brightness
     */
    private FrameLayout fl_brightness;
    private LinearLayout ll_brightness_front;
    private LinearLayout ll_brightness_back;

    private SeekBar sb_brightness;


    /**
     * 语言合成部分 =========================================
     */

    /**
     * 请求权限的请求码
     */
    private static final int ACTION_REQUEST_PERMISSIONS = 8;

    /**
     * 所需的所有权限信息
     */
    private static String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    protected String appId = "15698752";

    protected String appKey = "ByTLzXMGfjEDwTWupoRPsBRn";

    protected String secretKey = "30oWaHUSEsq4OtZAv1bveY7T9IQ0ad7Z";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.MIX;

    protected Handler mainHandler;

    protected String offlineVoice = OfflineResource.VOICE_FEMALE;
    protected MySyntherizer synthesizer;
    private static final String TAG = "PlanFragment";

    // ===========  变量定义结束 ===============

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plan, null);
        modeIsShowBack = false;
        brightnessIsShowBack = false;
        ll_reset = view.findViewById(R.id.ll_reset);
        ll_reset.setOnClickListener(this);

        tv_show_mode_status = view.findViewById(R.id.tv_show_mode_status);
        String s = "当前为自动模式\n"+"你的小可爱\n"+"正在为你提供\n"+"无语伦比的\n"+"精神抖擞的\n"+"最自然的光线~\n";
        tv_show_mode_status.setText(s);

        //new一个手势检测器
        detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float minMove = 120;         //定义最小滑动距离
                float minVelocity = 0;      //定义最小滑动速度
                float beginX = e1.getX();
                float endX = e2.getX();
                float beginY = e1.getY();
                float endY = e2.getY();

//                if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
//                    Toast.makeText(getContext(),"左滑",Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
//                }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
//                    Toast.makeText(getContext(),"右滑",Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
//                }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
//                    Toast.makeText(getContext(),"上滑",Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
//                }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
//                    Toast.makeText(getContext(),"下滑",Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
//                }

                if (endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){//下滑

                    //Toast.makeText(getContext(),"下滑",Toast.LENGTH_SHORT).show();
                    showCurrentInfoPopWindowPopupWindow();
                }
                return false;
            }
        });

        ll_reset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);//返回手势识别触发的事件;
            }
        });

        // ========= 音乐 =========
        mList = new ArrayList<>();
        initPopupWindow();
        getMusicList();
        mediaPlayer = new MediaPlayer();
        ll_music_front = view.findViewById(R.id.ll_music_front);
        ll_music_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusicPopWindowPopupWindow();
            }
        });

        // ========= 模式 =========
        fl_mode = view.findViewById(R.id.fl_mode);
        ll_mode_front = view.findViewById(R.id.ll_mode_front);
        ll_mode_front.setOnClickListener(this);
        ll_mode_back = view.findViewById(R.id.ll_mode_back);

        bt_mode_auto = view.findViewById(R.id.bt_mode_auto);
        bt_mode_first = view.findViewById(R.id.bt_mode_first);
        bt_mode_second = view.findViewById(R.id.bt_mode_second);
        bt_mode_third = view.findViewById(R.id.bt_mode_third);

        bt_mode_auto.setOnClickListener(this);
        bt_mode_first.setOnClickListener(this);
        bt_mode_second.setOnClickListener(this);
        bt_mode_third.setOnClickListener(this);

        // ========= 亮度 =========
        fl_brightness = view.findViewById(R.id.fl_brightness);
        ll_brightness_front = view.findViewById(R.id.ll_brightness_front);
        ll_brightness_front.setOnClickListener(this);
        ll_brightness_back = view.findViewById(R.id.ll_brightness_back);
        sb_brightness = view.findViewById(R.id.sb_brightness);
        sb_brightness.setMax(100);
        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getContext(), "亮度: "+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

            }
        });
        setAnimators();
        setCameraDistance();
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        }
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    print(msg.obj.toString());
                }
            }

        };
        initialTts();
        return view;
    }

    // 设置动画
    private void setAnimators() {
        modeRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.anim_out);
        modeLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.anim_in);

        brightnessRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.anim_out);
        brightnessLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.anim_in);

        // ========模式 动画监听========
        modeRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fl_mode.setClickable(false);
            }
        });
        modeLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fl_mode.setClickable(true);
            }
        });

        // ========亮度 动画监听========
        brightnessRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fl_brightness.setClickable(false);
            }
        });
        brightnessLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fl_brightness.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        ll_mode_front.setCameraDistance(scale);
        ll_mode_back.setCameraDistance(scale);
        ll_brightness_front.setCameraDistance(scale);
        ll_brightness_back.setCameraDistance(scale);
    }

    // 模式 处翻转卡片
    public void modeFlipCard(View view) {

        if (modeIsShowBack) {//此时mode显示背面，执行以下会载入正面
            modeRightOutSet.setTarget(ll_mode_back);
            modeLeftInSet.setTarget(ll_mode_front);
            modeRightOutSet.start();
            modeLeftInSet.start();
            modeIsShowBack = false;
            ll_mode_front.setEnabled(true);
            ll_mode_front.setClickable(true);
        } else {
            modeRightOutSet.setTarget(ll_mode_front);
            modeLeftInSet.setTarget(ll_mode_back);
            modeRightOutSet.start();
            modeLeftInSet.start();
            modeIsShowBack = true;
            ll_mode_front.setEnabled(false);
            ll_mode_front.setClickable(false);
        }
    }

    // 亮度 处翻转卡片
    public void brightnessFlipCard(View view) {

        if (brightnessIsShowBack) {//此时brightness显示背面，执行以下会载入正面
            brightnessRightOutSet.setTarget(ll_brightness_back);
            brightnessLeftInSet.setTarget(ll_brightness_front);
            brightnessRightOutSet.start();
            brightnessLeftInSet.start();
            brightnessIsShowBack = false;
            ll_brightness_front.setEnabled(true);
            ll_brightness_front.setClickable(true);
        } else {
            brightnessRightOutSet.setTarget(ll_brightness_front);
            brightnessLeftInSet.setTarget(ll_brightness_back);
            brightnessRightOutSet.start();
            brightnessLeftInSet.start();
            brightnessIsShowBack = true;
            ll_brightness_front.setEnabled(false);
            ll_brightness_front.setClickable(false);
        }
    }


    /**
     * 获取所有可选音乐
     */
    private void getMusicList(){
        HttpUtil.sendOkHttpRequestWithToken(url_getMusics, getContext(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "getMusicList onFailure");
                showUserWrong("getMusicList onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<MusicBean>(){}.getType();
                    try{
                        MusicBean bean = gson.fromJson(response.body().string(), type);
                        if (bean.getCode() == 0){
                            List<MusicBean.DataBean.MusicsBean>  list = bean.getData().getMusics();
                            for (int i=0; i<list.size(); i++){
                                mList.add(list.get(i));
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }else{
                            Log.e("TAG", "getMusicList bean.getCode() != 0");
                            showUserWrong("bean.getCode() != 0");
                        }
                    }catch (Exception e){
                        Log.e("TAG", "json error");
                        showUserWrong("json error");
                    }
                }else {
                    Log.e("TAG", "!response.isSuccessful()");
                    showUserWrong("!response.isSuccessful()");
                }
            }
        });
    }

    /**
     * 播放选择的音乐
     * @param url
     */
    private void startMediaPlayer(String url){
        mediaPlayer.reset();
        try{
            proxy = getProxy();
            String proxyUrl = proxy.getProxyUrl(url);
            mediaPlayer.setDataSource(proxyUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            Log.e("TAG", "startMediaPlayer error");
            showUserWrong("error");
        }
    }


    /**
     * 可选音乐弹出list初始化
     *
     * 以及
     * 下拉时当前信息的显示PopupWindow
     */
    private void initPopupWindow() {
        // music 部分
        View musicView = LayoutInflater.from(getContext()).inflate(R.layout.music_pop, null);
        rv_music = musicView.findViewById(R.id.rv_music);
        adapter = new MusicAdapter(getContext(), mList);
        adapter.setMusicCallBack(new MusicAdapter.MusicCallBack() {
            @Override
            public void onClicked(MusicBean.DataBean.MusicsBean bean) {
                startMediaPlayer(bean.getMusicUrl());
            }

            @Override
            public void onLongClicked(final MusicBean.DataBean.MusicsBean bean) {
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("修改音乐")
                        .setMessage("是否设置为此音乐？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alterMusic(bean.getId());
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });
        rv_music.setAdapter(adapter);
        rv_music.setLayoutManager(new LinearLayoutManager(getContext()));

        musicPopWindow = new PopupWindow(musicView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        musicPopWindow.setContentView(musicView);
        musicPopWindow.setAnimationStyle(R.style.pop_anim_music);
        musicPopWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        musicPopWindow.setOutsideTouchable(true);
        musicPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgAlpha(1f);
                mediaPlayer.reset();
            }
        });

        // current info 部分
        View currentInfoView = LayoutInflater.from(getContext()).inflate(R.layout.current_info_pop, null);
        tv_current_info_brightness = currentInfoView.findViewById(R.id.tv_current_info_brightness);
        tv_current_info_noise = currentInfoView.findViewById(R.id.tv_current_info_noise);
        tv_current_info_temperature = currentInfoView.findViewById(R.id.tv_current_info_temperature);
        tv_current_info_humidity = currentInfoView.findViewById(R.id.tv_current_info_humidity);

        currentInfoPopWindow = new PopupWindow(currentInfoView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        currentInfoPopWindow.setContentView(currentInfoView);
        currentInfoPopWindow.setAnimationStyle(R.style.pop_anim_current_info);
        currentInfoPopWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        currentInfoPopWindow.setOutsideTouchable(true);
        currentInfoPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                stop();
            }
        });
        currentInfoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentInfoPopWindow.dismiss();
                return false;
            }
        });
    }

    /**
     * 音乐模块被点击时
     * 弹出list
     */
    private void showMusicPopWindowPopupWindow() {
        //显示PopupWindow
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        musicPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        bgAlpha(0.5f);
    }

    /**
     * 下拉时
     * 显示当前信息
     */
    private void showCurrentInfoPopWindowPopupWindow(){
        getCurrentInfo();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        currentInfoPopWindow.showAtLocation(rootView, Gravity.TOP, 0, 0);
    }

    /**
     * pop list 弹出前后
     * 改变背景
     * @param bgAlpha
     * bgAlpha 为 0f - 1f 的一个值
     * 显示list时，传0.5f，背景半透明
     * 关闭list时，传1f，取消透明效果
     */
    private void bgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }

    private void changeMode(int modeType){
        final RequestBody requestBody=new FormBody.Builder()
                .add("type",modeType+"")
                .build();
        HttpUtil.sendOkHttpRequestWithTokenAndBody(url_switch, getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "changeMode onFailure");
                showUserWrong("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Gson gson=new Gson();
                    java.lang.reflect.Type type = new TypeToken<NormalBean>(){}.getType();
                    try{
                        NormalBean bean = gson.fromJson(response.body().string(), type);
                        if (bean.getCode() == 0){
                            showUserSuccessful("切换成功");
                        }else{
                            Log.e("TAG", "code != 0");
                            showUserWrong("切换失败");
                        }
                    }catch (Exception e){
                        Log.e("TAG", "json error");
                        showUserWrong("json解析失败");
                    }
                }else{
                    Log.e("TAG", "changeMode !response.isSuccessful()");
                    showUserWrong("!response.isSuccessful()");
                }
            }
        });
    }

    /**
     * 获取当前台灯的信息
     */
    private void getCurrentInfo(){
        HttpUtil.sendOkHttpRequestWithToken(url_getCurrentInfo, getContext(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "changeMode onFailure");
                showUserWrong("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Gson gson=new Gson();
                    java.lang.reflect.Type type = new TypeToken<CurrentInfoBean>(){}.getType();
                    try{
                        final CurrentInfoBean bean = gson.fromJson(response.body().string(), type);
                        if (bean.getCode() == 0){
                            final String brightness = "亮度: "+bean.getData().getEnvironmentInfoViewObject().getBrightness();
                            final String noise = "噪音: "+bean.getData().getEnvironmentInfoViewObject().getNoise();
                            final String temperature = "温度: "+bean.getData().getEnvironmentInfoViewObject().getTemperature();
                            final String humidity = "湿度: "+bean.getData().getEnvironmentInfoViewObject().getHumidity();

                            final String allInfo = "当前台灯环境"+"\n"+brightness
                                    + "\n" + temperature+"\n"
                                    +noise+"\n"+humidity+"\n";
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_current_info_brightness.setText(brightness);
                                    tv_current_info_noise.setText(noise);
                                    tv_current_info_temperature.setText(temperature);
                                    tv_current_info_humidity.setText(humidity);
                                    speak(allInfo);
                                }
                            });
                        }else{
                            Log.e("TAG", "code != 0");
                            showUserWrong("获取信息失败");
                        }
                    }catch (Exception e){
                        Log.e("TAG", "json error");
                        showUserWrong("json解析失败");
                    }
                }else{
                    Log.e("TAG", "changeMode !response.isSuccessful()");
                    showUserWrong("!response.isSuccessful()");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_reset:
                Log.e("TAG", "ll_reset");
                if (modeIsShowBack) {
                    modeFlipCard(v);
                }
                if (brightnessIsShowBack){
                    brightnessFlipCard(v);
                }
                break;
            case R.id.ll_mode_front:
                Log.e("TAG", "ll_mode_front");
                modeFlipCard(v);
                break;
            case R.id.bt_mode_auto:
                //Toast.makeText(getContext(), "auto mode", Toast.LENGTH_SHORT).show();
                changeMode(0);
                modeFlipCard(v);
                break;
            case R.id.bt_mode_first:
                //Toast.makeText(getContext(), "first mode", Toast.LENGTH_SHORT).show();
                changeMode(1);
                modeFlipCard(v);
                break;
            case R.id.bt_mode_second:
                //Toast.makeText(getContext(), "second mode", Toast.LENGTH_SHORT).show();
                changeMode(2);
                modeFlipCard(v);
                break;
            case R.id.bt_mode_third:
                //Toast.makeText(getContext(), "third mode", Toast.LENGTH_SHORT).show();
                changeMode(3);
                modeFlipCard(v);
                break;
            case R.id.ll_brightness_front:
                Log.e("TAG", "ll_brightness_front");
                brightnessFlipCard(v);
                break;
        }
    }

    private void showUserWrong(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserSuccessful(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        synthesizer.release();
        Log.i(TAG, "释放资源成功");
        super.onDestroy();

    }


    /**
     * 修改提醒铃声
     * @param musicId
     */
    private void alterMusic(int musicId){
        final RequestBody requestBody=new FormBody.Builder()
                .add("musicId",musicId+"")
                .build();
        HttpUtil.sendOkHttpRequestWithTokenAndBody(url_alterMusic, getContext(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "changeMode onFailure");
                showUserWrong("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Gson gson=new Gson();
                    java.lang.reflect.Type type = new TypeToken<NormalBean>(){}.getType();
                    try{
                        NormalBean bean = gson.fromJson(response.body().string(), type);
                        if (bean.getCode() == 0){
                            showUserSuccessful("音乐修改成功");
                        }else{
                            Log.e("TAG", "code != 0");
                            showUserWrong("修改失败");
                        }
                    }catch (Exception e){
                        Log.e("TAG", "json error");
                        showUserWrong("json解析失败");
                    }
                }else{
                    Log.e("TAG", "changeMode !response.isSuccessful()");
                    showUserWrong("!response.isSuccessful()");
                }
            }
        });
    }

    /**
     * 权限检测
     *
     * @param neededPermissions 所需的所有权限
     * @return 是否检测通过
     */
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(getContext(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }
            if (isAllGranted) {

            } else {
                Toast.makeText(getContext(), "拒绝授予权限将无法使用语言服务", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void print(String message) {
        Log.i(TAG, message);
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            print("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(getContext()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                        Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });
        synthesizer = new NonBlockSyntherizer(getContext(), initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(getContext(), voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
        }
        return offlineResource;
    }

    private void speak(final String msg) {
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = synthesizer.speak(msg);
        checkResult(result, "speak");
    }

    private void stop() {
        int result = synthesizer.stop();
        checkResult(result, "stop");
    }
}
