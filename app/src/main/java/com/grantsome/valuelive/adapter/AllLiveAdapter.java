package com.grantsome.valuelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.activity.GroupChatActivity;
import com.grantsome.valuelive.holder.AllLiveHolder;
import com.grantsome.valuelive.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by Grantsome on 2017/8/25.
 */

public class AllLiveAdapter extends RecyclerView.Adapter  {

    private List<String> mNameList = new ArrayList<>();

    private List<String> mConversationIdList = new ArrayList<>();

    private Context mContext;

    private String mObjectId;

    public AllLiveAdapter(List<String> nameList,List<String> conversationIdList,Context context) {
        mNameList = nameList;
        mConversationIdList = conversationIdList;
        mContext = context;
        SharedPreferences mPreferences = mContext.getSharedPreferences("account", Context.MODE_PRIVATE);
        mObjectId = mPreferences.getString("objectId",null);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllLiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        AllLiveHolder viewHolder = (AllLiveHolder) holder;
        viewHolder.getName().setText(mNameList.get(position));
        Log.d("JoinAdapter", "onBindViewHolder: "+mNameList.get(position));
        viewHolder.setRvItemClick(new AllLiveHolder.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()){
                    case R.id.live_avatar:
                    case R.id.live_title:
                        imLogin(position);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    public void imLogin(final int position) {
        if(mObjectId != null){
            LCChatKit.getInstance().open(mObjectId, new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (ToastUtils.filterException(e)) {
                        Intent intent = new Intent(mContext, GroupChatActivity.class);
                        intent.putExtra("cn.leancloud.chatkit.conversation_id",mConversationIdList.get(position));
                        mContext.startActivity(intent);
                    }
                }
            });
        }else {
            ToastUtils.showError(mContext.getString(R.string.objectId_error));
        }
    }
}
