package com.baidu.aip.asrwakeup3.core.mini;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import com.baidu.aip.asrwakeup3.core.view.ZoomImageView;
import com.baidu.aip.asrwakeup3.core.R;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  集成文档： http://ai.baidu.com/docs#/ASR-Android-SDK/top 集成指南一节
 *  demo目录下doc_integration_DOCUMENT
 *      ASR-INTEGRATION-helloworld  ASR集成指南-集成到helloworld中 对应 ActivityMiniRecog
 *      ASR-INTEGRATION-TTS-DEMO ASR集成指南-集成到合成DEMO中 对应 ActivityRecog
 */

public class ActivityMiniRecog extends AppCompatActivity implements EventListener {
     public ZoomImageView zoomImageView;
     public TextView txtLog;
    public EditText txtResult;
    public Button btn;
    public Button stopBtn;
    public Map<String, Object> params;
    private float x,y;
    private boolean check;
//    public ContextCompat contextCompat;
    public static String DESC_TEXT = "精简版识别，带有SDK唤醒运行的最少代码，仅仅展示如何调用，\n" +
            "也可以用来反馈测试SDK输入参数及输出回调。\n" +
            "本示例需要自行根据文档填写参数，可以使用之前识别示例中的日志中的参数。\n" +
            "需要完整版请参见之前的识别示例。\n" +
            "需要测试离线命令词识别功能可以将本类中的enableOffline改成true，首次测试离线命令词请联网使用。之后请说出“打电话给张三”";

    public EventManager asr;

    public boolean logTime = true;

    public boolean enableOffline = false; // 测试离线命令词，需要改成true

    /**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     * 测试参数填在这里
     */
    public void start() {
//        txtLog.setText("");
        if(check) {
            txtResult.setText("");
            f(x,y);
            stop();
            return;
        }
        params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号

        /* 语音自训练平台特有参数 */
        // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：搜索模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068); // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
//        (new AutoCheck(getApplicationContext(), new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n");
//                        ; // 可以用下面一行替代，在logcat中查看代码
//                        // Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//        },enableOffline)).checkAsr(params);
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
//        printLog("______________\n" + json);
        asr.send(event, json, null, 0, 0);
//        printLog("输入参数：" + json);
//        txtLog.setText(json);
    }

    /**
     * 点击停止按钮
     *  基于SDK集成4.1 发送停止事件
     */
    public void stop() {

        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }


    /**
     * enableOffline设为true时，在onCreate中调用
     * 基于SDK离线命令词1.4 加载离线资源(离线时使用)
     */
    public void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    /**
     * enableOffline为true时，在onDestory中调用，与loadOfflineEngine对应
     * 基于SDK集成5.1 卸载离线资源步骤(离线时使用)
     */
    public void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }

    public String getString(){
        return txtResult.getText().toString();
    }

    public void run(boolean check) {
        asr.registerListener(this); //  EventListener 中 onEvent方法

        this.check = check;
        start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_mini);
        initView();
        initPermission();
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr");
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stop();
            }
        });
        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    @Override
    public  void onPause(){
        super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        Log.i("ActivityMiniRecog","On pause");
    }

    @Override
    public  void onDestroy() {
        super.onDestroy();
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }

    // 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
    // 基于SDK集成3.1 开始回调事件
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        StringBuffer sb = new StringBuffer();
        for(int i=0;params!=null && i<params.length();i++){
            if(params.charAt(i) == ':'){
                for(int j=i+3;j<params.length() && params.charAt(j)!='"';j++){
                    sb.append(params.charAt(j));
                }
                break;
            }
        }
        String ans = sb.toString();
        if(ans.getBytes().length != ans.length())
        {
            txtResult.setText(ans);
            String str = ans;
                if(str.equals("郊野")){
                    x=973;
                    y=689;
                    if(!zoomImageView.mediaPlayer.isPlaying()) zoomImageView.mediaPlayer.start();
                    f(x,y);
                }else if(str.equals("码头")){
                    x=707;
                    y=1070;
                    if(!zoomImageView.mediaPlayer2.isPlaying())
                        zoomImageView.mediaPlayer2.start();
                    f(x,y);
                }else if(str.equals("街道")){
                    x=147;
                    y=829;
                    if(!zoomImageView.mediaPlayer3.isPlaying())
                        zoomImageView.mediaPlayer3.start();
                    f(x,y);
                }else return;
        }
    }
        public void f(float x,float y) {
        if(zoomImageView.getScale() < zoomImageView.SCALE_MID) {
//            AutoScaleRunnable as = new AutoScaleRunnable(act.zoomImageView,act.zoomImageView.SCALE_MID,x,y);
            zoomImageView.postDelayed(new AutoScaleRunnable(zoomImageView,zoomImageView.SCALE_MID,x,y),16);
            zoomImageView.isAutoScale = true;
        }else {
            if(!zoomImageView.mediaPlayer.isPlaying())zoomImageView.mediaPlayer.start();
            if(!zoomImageView.mediaPlayer3.isPlaying()) zoomImageView.mediaPlayer3.start();
            zoomImageView.postDelayed(new AutoScaleRunnable(zoomImageView,zoomImageView.initScale,x,y),16);
            zoomImageView.isAutoScale = true;
        }
    }
    public  void printLog(String ans) {
//        txtLog.append(text + "\n");
//        if (logTime) {
//            text += "  ;time=" + System.currentTimeMillis();
//        }
//        text += "\n";
//        Log.i(getClass().getName(), text);
        txtResult.append(ans + "\n\n");
    }


    public  void initView() {
//        txtResult = (EditText) findViewById(R.id.txtResult);
        txtLog = (TextView) findViewById(R.id.txtLog);
        btn = (Button) findViewById(R.id.btn);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        txtLog.setText(DESC_TEXT + "\n");
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    public  void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }
    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * 缩放的中心
         */
        private float x;
        private float y;

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         * @param targetScale
         */
        public AutoScaleRunnable(ZoomImageView view,float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (view.getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }
        }

        @Override
        public void run() {
            // 进行缩放
            zoomImageView.mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            zoomImageView.checkBorderAndCenterWhenScale();
            zoomImageView.setImageMatrix(zoomImageView.mScaleMatrix);

            final float currentScale = zoomImageView.getScale();
            // 如果值在合法范围内，继续缩放
            if (((tmpScale > 1f) && (currentScale < mTargetScale)) || ((tmpScale < 1f) && (mTargetScale < currentScale))) {

                zoomImageView.postDelayed(this, 16);
            } else {
                // 设置为目标的缩放比例
                final float deltaScale = mTargetScale / currentScale;
                zoomImageView.mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                zoomImageView.checkBorderAndCenterWhenScale();
                zoomImageView.setImageMatrix(zoomImageView.mScaleMatrix);
                zoomImageView.isAutoScale = false;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

}
