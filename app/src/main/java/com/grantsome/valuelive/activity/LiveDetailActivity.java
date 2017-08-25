package com.grantsome.valuelive.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.avos.avoscloud.AVObject;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.adapter.LiveDetailRvAdapter;
import com.grantsome.valuelive.utils.StatusUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveDetailActivity extends AppCompatActivity {

    private AVObject mHotLive;

    private LiveDetailRvAdapter mRvAdapter;

    private String mAvatarUrl;

    private String mName;

    @Bind(R.id.recycler_view)
    RecyclerView mRv;

    @OnClick(R.id.toolbar_iv_left)
    public void setClickIvLeft(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
        mHotLive = getIntent().getParcelableExtra("live_info");
        try{
            mAvatarUrl = getIntent().getStringExtra("url");
            mName = getIntent().getStringExtra("name");
        }catch (Exception e){
            e.printStackTrace();
        }
        setRv();
    }

    private void setRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        if(null != mAvatarUrl){
            mRvAdapter = new LiveDetailRvAdapter(this,mHotLive,mAvatarUrl,mName);
        }else {
            mRvAdapter = new LiveDetailRvAdapter(this,mHotLive);
        }
        mRv.setAdapter(mRvAdapter);
        if(null == mRv.getAdapter()){
            mRv.setAdapter(mRvAdapter);
        }else{
            mRvAdapter.notifyDataSetChanged();
        }
    }

}
