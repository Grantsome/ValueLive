package com.grantsome.valuelive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVObject;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.adapter.AllLiveAdapter;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AllLiveActivity extends AppCompatActivity {

    private List<String> mConversationIdList = new ArrayList<>();

    private List<String> mConversationNameList = new ArrayList<>();

    private List<AVObject> mAVObjectList = new ArrayList<>();

    private List<AVObject> mHotObjectList = new ArrayList<>();

    private String mObjectId;

    private AllLiveAdapter mAdapter;

    private SharedPreferences mPreferences;

    @Bind (R.id.recycler_view)
    RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_live);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
        init();
        setRv();
    }

    private void init() {
        mPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        if(mObjectId == null){
            mObjectId = mPreferences.getString("objectId",null);
        }
        if(mAVObjectList.size() == 0){
            mAVObjectList = HttpUtils.getCommonLiveList();
        }
        if(mHotObjectList.size() == 0){
            mHotObjectList = HttpUtils.getConversationList();
        }
        if(mConversationIdList.size() == 0){
            if(mAVObjectList.size() != 0){
                for(AVObject avObject:mAVObjectList){
                    mConversationIdList.add(avObject.getObjectId());
                }
            }
            if(mHotObjectList.size() != 0){
                for(AVObject avObject:mHotObjectList){
                    mConversationIdList.add(avObject.getObjectId());
                }
            }
        }
        if(mConversationNameList.size() == 0){
            if(mAVObjectList.size() != 0){
                for(AVObject avObject:mAVObjectList){
                    mConversationNameList.add(avObject.get("name").toString());
                }
            }
            if(mHotObjectList.size() != 0){
                for(AVObject avObject:mHotObjectList){
                    mConversationNameList.add(avObject.get("name").toString());
                }
            }
        }
        Log.d("AllLiveActivity", "init: Name.size() == " + mConversationNameList.size());
    }

    private void setRv() {
        mAdapter = new AllLiveAdapter(mConversationNameList,mConversationIdList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(mAdapter);
        if(null == mRv.getAdapter()){
            mRv.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
        Log.d("MyJoinLiveAdapter", "setRv: 完成");
    }

}
