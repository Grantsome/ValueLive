package com.grantsome.valuelive.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grantsome.valuelive.R;
import com.grantsome.valuelive.activity.AllLiveActivity;
import com.grantsome.valuelive.activity.LoginActivity;
import com.grantsome.valuelive.activity.MyLiveActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Grantsome on 2017/8/18.
 */

public class FunctionItemFragment extends android.support.v4.app.Fragment {

    private SharedPreferences mPreferences;

    @Bind(R.id.my_live_tv)
    TextView mLiveText;

    @OnClick(R.id.my_live_tv)
    public void setClickLive(){
        boolean isLogin = mPreferences.getBoolean("isLogin",false);
        if(!isLogin){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }else {
            startActivity(new Intent(getContext(),MyLiveActivity.class));
        }
    }

    @OnClick(R.id.class_browse_tv)
    public void setClickClass(){
        startActivity(new Intent(getContext(),AllLiveActivity.class));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_item,container,false);
        ButterKnife.bind(this,view);
        mPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        return view;
    }
}
