package com.grantsome.valuelive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.StatusUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyLiveActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    private String mObjectId;

    private  AVUser user;

    @Bind(R.id.iv_avatar)
    CircleImageView mAvatar;

    @Bind(R.id.username_tv)
    TextView mUserName;

    @Bind(R.id.my_join_live_tv)
    TextView mJoinLive;

    @Bind(R.id.my_create_live)
    TextView mCreateLive;

    @OnClick(R.id.username_layout)
    public void setClickUserLayout(){

    }

    @OnClick(R.id.iv_avatar)
    public void setClickAvatar(){

    }

    @OnClick(R.id.username_tv)
    public void setClickUserName(){

    }

    @OnClick(R.id.create_layout)
    public void setClickCreateLayout(){
        setClickOnMyCreateLive();
    }

    @OnClick(R.id.my_create_live)
    public void setClickCreateLive(){
        setClickOnMyCreateLive();
    }

    @OnClick(R.id.join_layout)
    public void setClickJoinLayout(){
        setClickOnMyJoinLive();
    }

    @OnClick(R.id.my_join_live_tv)
    public void setClickJoinLive(){
        setClickOnMyJoinLive();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
        mPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        mObjectId = mPreferences.getString("objectId",null);
        init();
    }

    private void init() {
        user = HttpUtils.findUserById(mObjectId);
        if(user.get("avatar").toString().equals("")){
            mAvatar.setImageResource(R.drawable.ic_account_circle_gray_24dp);
        }else {
            Glide.with(this).load(user.get("avatar").toString()).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(mAvatar);
        }
        mUserName.setText(user.getUsername());
    }

    private void setClickOnMyJoinLive(){
        Intent intent = new Intent(MyLiveActivity.this,MyJoinLiveActivity.class);
        startActivity(intent);
    }

    private void setClickOnMyCreateLive(){
        Intent intent = new Intent(MyLiveActivity.this,MyCreateLiveActivity.class);
        startActivity(intent);
    }

}
