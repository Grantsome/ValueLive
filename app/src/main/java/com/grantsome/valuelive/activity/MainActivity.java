package com.grantsome.valuelive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.StatusUtils;
import com.grantsome.valuelive.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    private List<AVObject> mHotLiveList = new ArrayList<>();

    private List<AVUser> mUserList = new ArrayList<>();

    private int mCurrentIndex;

    private String mAvatarUrl;

    private String mName;

    @Bind(R.id.toolbar_title_right)
    TextView mTextView;

    @Bind(R.id.pager)
    ViewPager pager;

    @OnClick(R.id.toolbar_title_right)
    public void clickOnToolBarTitle(){
        boolean isLogin = mPreferences.getBoolean("isLogin",false);
        if(!isLogin){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }else {
            startActivity(new Intent(MainActivity.this,CreateLiveActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
        mPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        //addFragment();
        setListener();
    }



    private void setListener() {
        mHotLiveList = HttpUtils.getHotLiveList();
        mUserList = HttpUtils.getUserList();
        pager.setOnItemClickListener(new ViewPager.OnItemClickListener() {
            @Override
            public void click(View v, String url) {
               for(int i = 0; i < mHotLiveList.size();i++){
                   if(mHotLiveList.get(i).getString("url").equals(url)){
                       mCurrentIndex = i;
                       Log.d("MainActivity", "click: "+ mCurrentIndex+" url: " + mHotLiveList.get(i).getString("url"));
                       for(int j = 0; j < mUserList.size();j++){
                           Log.d("MainActivity", "click: 进入user循环"+j);
                           if(mUserList.get(j).getObjectId().equals(mHotLiveList.get(mCurrentIndex).get("speakerId"))){
                               mAvatarUrl = mUserList.get(j).get("avatar").toString();
                               mName = mUserList.get(j).getUsername();
                               Log.d("MainActivity", "click: 找到"+j);
                           }
                       }
                       Intent intent = new Intent(MainActivity.this,LiveDetailActivity.class);
                       intent.putExtra("live_info",mHotLiveList.get(mCurrentIndex));
                       if(mAvatarUrl != null){
                           intent.putExtra("url",mAvatarUrl);
                           intent.putExtra("name",mName);
                       }
                       startActivity(intent);
                       overridePendingTransition(0,0);
                   }
               }
            }
        });
    }


}
