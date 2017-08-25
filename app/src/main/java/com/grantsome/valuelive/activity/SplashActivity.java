package com.grantsome.valuelive.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.StatusUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash();
        StatusUtils.changeStatus(this);
    }

    private void splash(){
        final Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //定时跳转
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //销毁StartActivity，使得按住BACK键的时候直接退出程序
                finish();
                startActivity(intent);
            }
        };
        timer.schedule(timerTask,1000*2);//定时两秒
    }
}
