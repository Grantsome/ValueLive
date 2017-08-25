package com.grantsome.valuelive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.Common;
import com.grantsome.valuelive.utils.StatusUtils;
import com.grantsome.valuelive.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyNameActivity extends AppCompatActivity {

    @Bind(R.id.name_et)
    EditText mNameEdit;

    @Bind(R.id.sure)
    Button mSure;

    @OnClick(R.id.toolbar_iv_left)
    public void setClickOnToolbar(){
        finish();
    }

    @OnClick(R.id.sure)
    public void setClickOnSure(){
        modifyName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
    }

    private void modifyName(){
        Intent intent = new Intent();
        intent.putExtra(Common.INTENT_MODIFY_VALUE,mNameEdit.getText()+"");
        setResult(RESULT_OK,intent);
        ToastUtils.showHint(getString(R.string.modify_success));
        finish();
    }
}
