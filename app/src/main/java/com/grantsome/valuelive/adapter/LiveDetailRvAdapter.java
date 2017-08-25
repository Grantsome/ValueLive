package com.grantsome.valuelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.activity.GroupChatActivity;
import com.grantsome.valuelive.holder.LiveDetailViewHolder;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by Grantsome on 2017/08/20.
 */

public class LiveDetailRvAdapter extends RecyclerView.Adapter {

    private AVObject mHotLive;

    private Context mContext;

    private String mAvatarUrl;

    private String mName;

    private List<AVObject> mConversationList = new ArrayList<>();

    private String mObjectId;

    private AVObject mConversation;

    public boolean init(){
        SharedPreferences mPreferences = mContext.getSharedPreferences("account", Context.MODE_PRIVATE);;
        boolean isLogin = mPreferences.getBoolean("isLogin",false);
        mObjectId = mPreferences.getString("objectId",null);
        return isLogin;
    }

    public LiveDetailRvAdapter(Context context,AVObject hotLive) {
        mContext = context;
        mHotLive = hotLive;
        mConversationList = HttpUtils.getConversationList();
        init();
    }

    public LiveDetailRvAdapter(Context context,AVObject hotLive,String avatarUrl,String name) {
        mContext = context;
        mHotLive = hotLive;
        mAvatarUrl = avatarUrl;
        mName = name;
        mConversationList = HttpUtils.getConversationList();
        init();
    }

    private String getConversationIdBySpeaker(){
        if(mConversationList.size() == 0){
            mConversationList = HttpUtils.getConversationList();
        }
        Log.d("Adapter", "getConversationIdBySpeaker: size()= " + mConversationList.size());
        for (int i = 0; i < mConversationList.size(); i++)
            if (mConversationList.get(i).get("createrId").toString().equals(mHotLive.get("speakerId").toString())) {
                mConversation = mConversationList.get(i);
                return mConversationList.get(i).getObjectId();
            }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return new LiveDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_detail,parent,false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LiveDetailViewHolder viewHolder = (LiveDetailViewHolder) holder;
        if(null != mHotLive) {
            Glide.with(mContext).load(mHotLive.get("url")).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(((LiveDetailViewHolder) holder).getLiveImage());
            viewHolder.getLiveTitle().setText(mHotLive.get("subject") + "");
            viewHolder.getLiveJoinDesc().setText(mHotLive.get("joinDesc") + "");
            viewHolder.setRvItemClick(new LiveDetailViewHolder.OnRecyclerViewItemClick() {
                @Override
                public void onItemClick(View view) {
                    switch (view.getId()){
                        case R.id.join_button:
                            if(getConversationIdBySpeaker()!=null){
//                                Intent intent = new Intent(mContext, GroupChatActivity.class);
//                                intent.putExtra("cn.leancloud.chatkit.conversation_id",getConversationIdBySpeaker());
//                                mContext.startActivity(intent);
                                imLogin();
                            }else {
                                ToastUtils.showError(mContext.getString(R.string.conv_error));
                            }
                    }
                }
            });
            if(mAvatarUrl == null){
                viewHolder.getLiveSpeakerName().setText(mHotLive.get("speakerName") + "");
                viewHolder.getLiveSpeakerAvatar().setImageResource(R.drawable.ic_account_circle_gray_24dp);
            }else {
                Glide.with(mContext).load(mAvatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(((LiveDetailViewHolder) holder).getLiveSpeakerAvatar());
                viewHolder.getLiveSpeakerName().setText(mHotLive.get("speakerName") +"(" +mName+")");
            }
            viewHolder.getLiveSpeakerDesc().setText(mHotLive.get("speakerDesc") + "");
            viewHolder.getLiveDesc().setText(mHotLive.get("liveDesc") + "");
            viewHolder.getLiveOutlineDesc().setText(mHotLive.get("outline") + "");
        }
    }

    public void imLogin() {
        if(mObjectId != null){

            LCChatKit.getInstance().open(mObjectId, new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (ToastUtils.filterException(e)) {
                        Intent intent = new Intent(mContext, GroupChatActivity.class);
                        intent.putExtra("cn.leancloud.chatkit.conversation_id",getConversationIdBySpeaker());
                        mContext.startActivity(intent);
                    }
                }
            });
        }else {
            ToastUtils.showError(mContext.getString(R.string.objectId_error));
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
