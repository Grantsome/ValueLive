package com.grantsome.valuelive.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.grantsome.valuelive.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Grantsome on 2017/8/20.
 */

public class LiveDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @Bind (R.id.image_view)
    ImageView mLiveImage;

    @Bind(R.id.live_title)
    TextView mLiveTitle;

    @Bind(R.id.live_join_desc)
    TextView mLiveJoinDesc;

    @Bind(R.id.join_button)
    Button mJoinButton;

    @Bind(R.id.live_speaker_avatar)
    CircleImageView mLiveSpeakerAvatar;

    @Bind(R.id.live_speaker_name)
    TextView mLiveSpeakerName;

    @Bind(R.id.live_speaker_desc)
    TextView mLiveSpeakerDesc;

    @Bind(R.id.live_desc)
    TextView mLiveDesc;

    @Bind(R.id.live_outline_desc)
    TextView mLiveOutlineDesc;

    private OnRecyclerViewItemClick mRvItemClick;

    public LiveDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
        mJoinButton.setOnClickListener(this);
    }


    public ImageView getLiveImage() {
        return mLiveImage;
    }

    public TextView getLiveTitle() {
        return mLiveTitle;
    }

    public TextView getLiveJoinDesc() {
        return mLiveJoinDesc;
    }

    public Button getJoinButton() {
        return mJoinButton;
    }

    public CircleImageView getLiveSpeakerAvatar() {
        return mLiveSpeakerAvatar;
    }

    public TextView getLiveSpeakerName() {
        return mLiveSpeakerName;
    }

    public TextView getLiveSpeakerDesc() {
        return mLiveSpeakerDesc;
    }

    public TextView getLiveDesc() {
        return mLiveDesc;
    }

    public TextView getLiveOutlineDesc() {
        return mLiveOutlineDesc;
    }

    public void setRvItemClick(OnRecyclerViewItemClick rvItemClick) {
        mRvItemClick = rvItemClick;
    }

    @Override
    public void onClick(View v) {
        if(mRvItemClick!=null){
            mRvItemClick.onItemClick(v);
        }
    }

    public interface OnRecyclerViewItemClick{
        void onItemClick(View view);
    }

}
