package com.baidu.speech.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.baidu.aip.asrwakeup3.core.mini.ActivityMiniRecog;
import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManagerFactory;
import com.baidu.aip.asrwakeup3.core.view.ZoomImageView;
import java.util.ArrayList;

//public class MainActivity extends ActivityMiniRecog {
//
//
//
//}

public class MainActivity extends AppCompatActivity {

    ActivityMiniRecog act;
    Button btn;
    boolean check=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn =  findViewById(R.id.button);

        act = new ActivityMiniRecog();
        act.zoomImageView = findViewById(R.id.myView);
        act.zoomImageView.mediaPlayer = MediaPlayer.create(this,R.raw.bird);
        act.zoomImageView.mediaPlayer2 = MediaPlayer.create(this,R.raw.dock);
        act.zoomImageView.mediaPlayer3 = MediaPlayer.create(this,R.raw.noise);
        act.zoomImageView.mediaPlayer.setLooping(true);
        act.zoomImageView.mediaPlayer2.setLooping(true);
        act.zoomImageView.mediaPlayer3.setLooping(true);
        act.zoomImageView.mediaPlayer.start();
        act.zoomImageView.mediaPlayer3.start();


        act.txtResult = findViewById(R.id.editText);
        act.asr = EventManagerFactory.create(this, "asr");

        initPermission();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check){
                    if(act.zoomImageView.mediaPlayer.isPlaying()) act.zoomImageView.mediaPlayer.pause();
                    if(act.zoomImageView.mediaPlayer2.isPlaying()) act.zoomImageView.mediaPlayer2.pause();
                    if(act.zoomImageView.mediaPlayer3.isPlaying()) act.zoomImageView.mediaPlayer3.pause();
                    act.run(true);
                    btn.setText("说话");
                    check = false;
                }else {
                    if(act.zoomImageView.mediaPlayer.isPlaying()) act.zoomImageView.mediaPlayer.pause();
                    if(act.zoomImageView.mediaPlayer2.isPlaying()) act.zoomImageView.mediaPlayer2.pause();
                    if(act.zoomImageView.mediaPlayer3.isPlaying()) act.zoomImageView.mediaPlayer3.pause();
                    act.run(false);
                    btn.setText("复位");
                    check = true;
                }

            }
        });

    }
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
}