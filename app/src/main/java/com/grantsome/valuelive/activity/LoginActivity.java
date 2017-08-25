package com.grantsome.valuelive.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.ToastUtils;
import com.grantsome.valuelive.widget.LoginDialog;

import cn.leancloud.chatkit.LCChatKit;

import static com.avos.avoscloud.AVUser.getCurrentUser;

public class LoginActivity extends AppCompatActivity {

    private LoginDialog mDialog;

    private SharedPreferences mPreferences;

    private SharedPreferences.Editor mEditor;

    private static String mObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mDialog = new LoginDialog(this);
        initDialog();
    }

    private void initDialog(){
        mDialog.show();
        mDialog.getMessageTextView().setText(R.string.welcome);
        mDialog.setRegisterButton(R.string.register, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = getUserInfoFromInput();
                String password = getPasswordInfoFromInput();
                register(username,password);
            }
        });
        mDialog.setLoginButton(R.string.login, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getUserInfoFromInput();
                String password = getPasswordInfoFromInput();
                login(username,password);
            }
        });
        mDialog.addUsernameWrapper(getString(R.string.hint_user_name));
        mDialog.addPasswordWrapper(getString(R.string.hint_password));
        String username = mPreferences.getString("username","");
        String password = mPreferences.getString("password","");
        mDialog.getUsernameWrapper().getEditText().setTag(username);
        mDialog.getPasswordWrapper().getEditText().setText(password);
        boolean isLogin = mPreferences.getBoolean("isLogin",false);
        if(isLogin){
            login(username,password);
        }
    }

    private String getUserInfoFromInput(){
        String username = mDialog.getUsernameWrapper().getEditText().getText().toString();
        return username;
    }

    private String getPasswordInfoFromInput(){
        String password = mDialog.getPasswordWrapper().getEditText().getText().toString();
        return password;
    }


    private void login(String username,String password){
        loginOrRegister(false,username,password);
    }

    private void register(String username,String password){
        if (username.length()<2){
            mDialog.getUsernameWrapper().setError(getString(R.string.warn_user_name));
            return;
        }
        if (password.length()<6){
            mDialog.getPasswordWrapper().setError(getString(R.string.warn_password));
            return;
        }
        loginOrRegister(true,username,password);
    }

    private void loginOrRegister(boolean isRegirster,String username, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.logining));
        progressDialog.show();
        final AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        if(isRegirster){
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        ToastUtils.showHint(getString(R.string.register_success));
                        mEditor.putBoolean("isLogin",true);
                        mEditor.apply();
                        imLogin();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        saveInfo(user.getObjectId());
                        LoginActivity.this.finish();
                    } else {
                        progressDialog.dismiss();
                        ToastUtils.showError(e.getMessage());
                    }
                }
            });
        }
        if(!isRegirster){
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        ToastUtils.showHint(getString(R.string.login_success));
                        saveInfo(avUser.getObjectId());
                        imLogin();
                        LoginActivity.this.finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveInfo(String objectId){
        String username = getUserInfoFromInput();
        String password = getPasswordInfoFromInput();
        mEditor.putString("username",username);
        mEditor.putString("password",password);
        mEditor.putBoolean("isLogin",true);
        mEditor.putString("objectId",objectId);
        mEditor.apply();
    }

    public void imLogin() {

        LCChatKit.getInstance().open(getCurrentUserId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (ToastUtils.filterException(e)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public static String getCurrentUserId () {
        AVUser currentUser = getCurrentUser(AVUser.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }

}
