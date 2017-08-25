package com.grantsome.valuelive.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.grantsome.valuelive.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Grantsome on 2017/8/25.
 */

public class AllLiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public CircleImageView getAvavtar() {
        return mAvavtar;
    }

    public TextView getName() {
        return mName;
    }

    private OnRecyclerViewItemClick mRvItemClick;

    @Bind (R.id.live_avatar)
    CircleImageView mAvavtar;

    @Bind(R.id.live_title)
    TextView mName;

    public AllLiveHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
        mAvavtar.setOnClickListener(this);
        mName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mRvItemClick!=null){
            mRvItemClick.onItemClick(v);
        }
    }

    public void setRvItemClick(OnRecyclerViewItemClick rvItemClick) {
        mRvItemClick = rvItemClick;
    }

    public interface OnRecyclerViewItemClick{
        void onItemClick(View view);
    }
}

