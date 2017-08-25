package com.grantsome.valuelive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.StatusUtils;
import com.grantsome.valuelive.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.grantsome.valuelive.utils.Common.CHOOSE_IMAGE;

public class CreateLiveActivity extends AppCompatActivity implements View.OnClickListener{

    private byte[] mImageBytes = null;

    private SharedPreferences mPreferences;

    private String mObjectId;

    @Bind(R.id.image_view)
    ImageView mImageView;

    @Bind(R.id.input_title)
    EditText mTitleInput;

    @Bind(R.id.input_name)
    EditText mNameInput;

    @Bind(R.id.input_intro)
    EditText mIntroInput;

    @Bind(R.id.input_desc)
    EditText mDescInput;

    @Bind(R.id.input_outline)
    EditText mOutlineInput;

    @Bind(R.id.commit_button)
    Button mCommitButton;

    @Bind(R.id.layout)
    LinearLayout rlytTimerName;

    @OnClick(R.id.image_view)
    public void setClickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @OnClick(R.id.commit_button)
    public void setClickCommit(){
        if(checkEditTextAndImageIsNull()){
            commitIntoServer();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_live);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
        mPreferences = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        mObjectId = mPreferences.getString("objectId",null);
        init();
    }

    private boolean checkEditTextAndImageIsNull() {
        if(mImageBytes == null){
            ToastUtils.showHint(getString(R.string.image_can_not_null));
            return false;
        }
        if(mTitleInput.getText().toString().equals(null)||mTitleInput.getText().toString().equals("")){
            ToastUtils.showHint(getString(R.string.title_can_not_null));
            return false;
        }
        if(mNameInput.getText().toString().equals(null)||mNameInput.getText().toString().equals("")){
            ToastUtils.showHint(getString(R.string.name_can_not_null));
            return false;
        }
        if(mIntroInput.getText().toString().equals(null)||mIntroInput.getText().toString().equals("")){
            ToastUtils.showHint(getString(R.string.intro_can_not_null));
            return false;
        }
        if(mDescInput.getText().toString().equals(null)||mDescInput.getText().toString().equals("")){
            ToastUtils.showHint(getString(R.string.desc_can_not_null));
            return false;
        }
        if(mOutlineInput.getText().toString().equals(null)||mOutlineInput.getText().toString().equals("")){
            ToastUtils.showHint(getString(R.string.outline_can_not_null));
            return false;
        }
        return true;
    }

    private void commitIntoServer() {
        AVObject live = new AVObject("CommonLive");
        live.put("title", mTitleInput.getText().toString());
        live.put("name",mNameInput.getText().toString());
        live.put("intro",mIntroInput.getText().toString());
        live.put("desc",mDescInput.getText().toString());
        live.put("outline",mOutlineInput.getText().toString());
        live.put("createId",mObjectId);
        live.put("owner",HttpUtils.findUserById(mObjectId));
        live.put("image", new AVFile("image", mImageBytes));
        live.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.d("CreateLiveActivity", "commitIntoServer");
                    createConversation();
                    startActivity(new Intent(CreateLiveActivity.this,MainActivity.class));
                } else {
                    ToastUtils.showHint(getString(R.string.create_live_fail));
                }
            }
        });
    }

    private void createConversation() {
        AVObject live = new AVObject("_Conversation");
        live.put("name", mTitleInput.getText().toString());
        live.put("m",new String[] {mObjectId});
        live.put("createrId",mObjectId);
        live.put("owner",HttpUtils.findUserById(mObjectId));
        live.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(null == e){
                    ToastUtils.showHint(getString(R.string.create_live_success));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {
            try {
                mImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                mImageBytes = getBytes(getContentResolver().openInputStream(data.getData()));
                Log.d("CreateLiveActivity", "onActivityResult: 获取图片数据完成");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("CreateLiveActivity", "onActivityResult: 失败");
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void init(){
        initEtName(mTitleInput);
        initEtName(mNameInput);
        initEtName(mDescInput);
        initEtName(mIntroInput);
        initEtName(mOutlineInput);
    }

    private void initEtName(final EditText etName) {
        // 使RelativeLayout 获取焦点，防止 EditText 截取
        rlytTimerName.setFocusable(true);
        rlytTimerName.setFocusableInTouchMode(true);
        rlytTimerName.requestFocus();
        etName.setOnClickListener(this);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNameGetFocus(etName);
                } else {
                    etNameLostFocus(etName);
                }
            }
        });
        etName.setText(etName.getText().toString());
        etNameLostFocus(etName);
    }

    // 重置edittext, 居中并失去焦点
    private void etNameLostFocus(EditText etName) {
        etName.clearFocus();
        InputMethodManager manager = (InputMethodManager) etName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }

    // 获取焦点
    private void etNameGetFocus(final EditText etName) {
        etName.requestFocus();
        etName.setGravity(Gravity.START);
        etName.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) etName.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(etName, 0);
            }
        });
        // 光标置于文字最后
        etName.setSelection(etName.getText().length());
    }

    @RequiresApi (api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_title:
            case R.id.input_outline:
            case R.id.input_desc:
            case R.id.input_name:
            case R.id.input_intro:
                // 设置 EditText 的点击事件（如果处于编辑状态则，不做操作；否则，获取焦点进入可编辑状态）；
                EditText et = (EditText)v;
                if (!et.isCursorVisible()) {
                    etNameGetFocus(et);
                }
                break;
        }
    }

    // 点击屏幕其他地方，使 etName 失去焦点（EditText）
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                // 判断点击的点是否落在当前焦点所在的 view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
