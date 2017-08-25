package com.grantsome.valuelive.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grantsome.valuelive.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Grantsome on 2017/8/18.
 */

public class LoginDialog extends Dialog {

    @Bind(R.id.message)
    TextView mMessageTextView;

    @Bind(R.id.login)
    Button mLoginButton;

    @Bind(R.id.register)
    Button mRegisterButton;

    @Bind(R.id.user_name_wrapper)
    TextInputLayout mUsernameWrapper;

    @Bind(R.id.password_wrapper)
    TextInputLayout mPasswordWrapper;

    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.dialog_login);
        ButterKnife.bind(this);
    }

    public TextView getMessageTextView(){
        return mMessageTextView;
    }

    public TextInputLayout getUsernameWrapper(){
        return mUsernameWrapper;
    }

    public TextInputLayout getPasswordWrapper(){
        return mPasswordWrapper;
    }

    public void setLoginButton(String message, View.OnClickListener listener){
        mLoginButton.setVisibility(View.VISIBLE);
        mLoginButton.setText(message);
        if(listener!=null){
            mLoginButton.setOnClickListener(listener);
        }
    }

    public void setLoginButton(int id,View.OnClickListener listener){
        setLoginButton(getContext().getString(id),listener);
    }

    public void setRegisterButton(String message,View.OnClickListener listener){
        mRegisterButton.setVisibility(View.VISIBLE);
        mRegisterButton.setText(message);
        if(listener!=null){
            mRegisterButton.setOnClickListener(listener);
        }
    }

    public void setRegisterButton(int id,View.OnClickListener listener){
        setRegisterButton(getContext().getString(id),listener);
    }

    public void addUsernameWrapper(String hint){
        mUsernameWrapper.setVisibility(View.VISIBLE);
        mUsernameWrapper.setHint(hint);
    }

    public void addPasswordWrapper(String hint){
        mPasswordWrapper.setVisibility(View.VISIBLE);
        mPasswordWrapper.setHint(hint);
    }

}
