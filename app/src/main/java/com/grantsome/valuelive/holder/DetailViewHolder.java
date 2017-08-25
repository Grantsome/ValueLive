package com.grantsome.valuelive.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.grantsome.valuelive.R;

import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;

/**
 * Created by Grantsome on 2017/8/23.
 */

public class DetailViewHolder extends LCIMCommonViewHolder {

    LinearLayout rootLayout;

    public DetailViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.detail_layout);
        rootLayout = (LinearLayout)itemView.findViewById(R.id.root_view);
    }

    public void setView(View view) {
        rootLayout.removeAllViews();
        if (null != view) {
            rootLayout.addView(view);
        }
    }

    @Override
    public void bindData(Object o) {}
}