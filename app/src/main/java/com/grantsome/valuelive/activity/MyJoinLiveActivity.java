package com.grantsome.valuelive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.grantsome.valuelive.R;
import com.grantsome.valuelive.adapter.MyJoinLiveRvAdapter;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyJoinLiveActivity extends AppCompatActivity {

    private List<String> mConversationIdList = new ArrayList<>();

    private List<String> mConversationNameList = new ArrayList<>();

    private String mObjectId;

    private MyJoinLiveRvAdapter mAdapter;

    private SharedPreferences mPreferences;

    @Bind(R.id.recycler_view)
    RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_join_live);
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
        if(mConversationIdList.size() == 0){
            mConversationIdList = HttpUtils.findJoinConversationIdListById(mObjectId);
        }
        Log.d("MyJoinLiveActivity", "init: Id.size() == " + mConversationIdList.size());
        if(mConversationNameList.size() == 0){
            mConversationNameList = HttpUtils.findJoinConversationNameListById(mObjectId);
        }
        Log.d("MyJoinLiveActivity", "init: Name.size() == " + mConversationNameList.size());
    }

    private void setRv() {
           mAdapter = new MyJoinLiveRvAdapter(mConversationNameList,mConversationIdList,this);
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
