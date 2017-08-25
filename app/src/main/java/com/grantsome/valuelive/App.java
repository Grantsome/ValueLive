package com.grantsome.valuelive;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.grantsome.valuelive.provider.UserProvider;

import cn.leancloud.chatkit.LCChatKit;

import static com.grantsome.valuelive.utils.Common.APP_ID;
import static com.grantsome.valuelive.utils.Common.APP_KEY;

/**
 * Created by Grantsome on 2017/8/18.
 */

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, APP_ID, APP_KEY);
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可:调用调试日志
        AVOSCloud.setDebugLogEnabled(true);
        AVAnalytics.enableCrashReport(this,true);
        LCChatKit.getInstance().setProfileProvider(UserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        if(sContext==null){
            return null;
        }
        return sContext;
    }
}
