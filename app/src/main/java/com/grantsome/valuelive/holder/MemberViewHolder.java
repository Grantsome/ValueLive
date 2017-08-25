package com.grantsome.valuelive.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.grantsome.valuelive.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Grantsome on 2017/8/24.
 */

public class MemberViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.member_avatar)
    CircleImageView mAvatar;

    @Bind(R.id.member_name)
    TextView mName;

    public CircleImageView getAvatar() {
        return mAvatar;
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public MemberViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
