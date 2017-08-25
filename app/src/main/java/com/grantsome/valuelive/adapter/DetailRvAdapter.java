package com.grantsome.valuelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.holder.DetailViewHolder;
import com.grantsome.valuelive.holder.MemberViewHolder;
import com.grantsome.valuelive.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grantsome on 2017/8/23.
 */

public class DetailRvAdapter extends RecyclerView.Adapter {

    public static final int HEADER_ITEM_TYPE = -1;
    public static final int FOOTER_ITEM_TYPE = -2;
    public static final int COMMON_ITEM_TYPE = 1;

    private View headerView = null;
    private View footerView = null;

    private List<String> nameList = new ArrayList<>();

    private List<String> urlList = new ArrayList<>();

    private List<String> mObjectIDList;

    private Context mContext;

    List<AVUser> mUserList = new ArrayList<>();

    public DetailRvAdapter(List<String> mObjectIDList,Context context) {
        this.mContext = context;
        this.mObjectIDList = mObjectIDList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public void setHeaderView(View view) {
        headerView = view;
    }

    public void setFooterView(View view) {
        footerView = view;
    }

    @Override
    public int getItemCount() {
        check();
        int itemCount = nameList.size();
        if (null != footerView) {
            ++itemCount;
        }
        return itemCount;
    }

    @Override
    public long getItemId(int position) {
        if (null != headerView && 0 == position) {
            return  HEADER_ITEM_TYPE;
        }

        if (null != footerView && position == getItemCount() - 1) {
            return FOOTER_ITEM_TYPE;
        }
        return super.getItemId(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (null != footerView && position == getItemCount() - 1) {
            return FOOTER_ITEM_TYPE;
        }

        return COMMON_ITEM_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int truePosition = position;
        if (null != footerView && position == getItemCount() - 1) {
            return;
        }
        if(truePosition >= 0 && truePosition < this.nameList.size()) {
            MemberViewHolder h = (MemberViewHolder) holder;
            check();
            Log.d("DetailAdapter", "onBindViewHolder: "+urlList.get(truePosition));
            Glide.with(mContext).load(urlList.get(truePosition)).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(h.getAvatar());
            h.setName(nameList.get(truePosition));
        }
    }

    private void check() {
        if(nameList.size() == 0){
            initData(mObjectIDList);
        }
    }

    private void initData(List<String> mObjectIDList) {
        List<String> mNameList = new ArrayList<>();
        List<String> mUrlList = new ArrayList<>();
        if(mUserList.size() == 0 || nameList.size() == 0){
            if(mUserList.size() ==0){
                mUserList.clear();
                mUserList = HttpUtils.getUserList();
            }
            Log.d("GroupActivity", "userList.size() " + mUserList.size());
            for(AVUser user:mUserList){
                Log.d("GroupActivity", "initData: user.objectID()"+user.getObjectId());
                for(String objectId:mObjectIDList){
                    Log.d("GroupActivity", "initData: object"+objectId);
                    if(user.getObjectId().equals(objectId)){
                        Log.d("GroupActivity", "initData: 两者相等");
                        mNameList.add(user.getUsername());
                        mUrlList.add(user.get("avatar")+"");
                    }
                }
            }
            Log.d("GroupActivity", "setupRv: "+mNameList.size());
            nameList = mNameList;
            urlList = mUrlList;
        }
        Log.d("GroupActivity", "initData: 执行次数");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (FOOTER_ITEM_TYPE == viewType) {
            DetailViewHolder itemHolder = new DetailViewHolder(parent.getContext(), parent);
            itemHolder.setView(footerView);
            return itemHolder;
        }
        if( COMMON_ITEM_TYPE == viewType){
           MemberViewHolder viewHolder = new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false));
           return viewHolder;
        }
        return null;
    }
}
