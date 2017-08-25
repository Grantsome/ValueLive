package com.grantsome.valuelive.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.adapter.DetailRvAdapter;
import com.grantsome.valuelive.model.ConversationType;
import com.grantsome.valuelive.utils.Common;
import com.grantsome.valuelive.utils.HttpUtils;
import com.grantsome.valuelive.utils.StatusUtils;
import com.grantsome.valuelive.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMConstants;

import static com.grantsome.valuelive.utils.ToastUtils.filterException;

public class GroupDetailActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRv;

    private View mNameLayout;

    private View mQuitLayout;

    private LinearLayoutManager mLayoutManager;

    private DetailRvAdapter mUserListAdapter;

    private ConversationType mConversationType;

    private AVIMConversation mConversation;

    @OnClick (R.id.toolbar_iv_left)
    public void setClickIvLeft(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        StatusUtils.changeStatus(this);
        mConversation = LCChatKit.getInstance().getClient().getConversation(getIntent().getStringExtra(LCIMConstants.CONVERSATION_ID));
        View footerView = getLayoutInflater().inflate(R.layout.item_detail_footer,null);
        mNameLayout = footerView.findViewById(R.id.name_layout);
        mNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setClickOnName();
            }
        });
        mQuitLayout = footerView.findViewById(R.id.quit_layout);
        mQuitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickOnQuit();
            }
        });
        setupRv(footerView);
    }

    public void setClickOnName(){
        Intent intent = new Intent(this,ModifyNameActivity.class);
        intent.putExtra(Common.INTENT_MODIFY_KEY,getString(R.string.live_group_name));
        startActivityForResult(intent,Common.MODIFY_NAME);
    }

    public void setClickOnQuit(){
        new AlertDialog.Builder(this).setMessage(R.string.quit_group_tip)
                .setPositiveButton(R.string.quit_group_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String convid = mConversation.getConversationId();
                        mConversation.quit(new AVIMConversationCallback() {
                            @Override
                            public void done(AVIMException e) {
                                if (filterException(e)) {
                                    LCIMConversationItemCache.getInstance().deleteConversation(convid);
                                    ToastUtils.showHint(getString(R.string.quit_group_success));
                                    setResult(RESULT_OK);
                                    startActivity(new Intent(GroupDetailActivity.this,MainActivity.class));
                                }
                            }
                        });
                    }
                }).setNegativeButton(R.string.quit_group_cancel, null).show();
    }


    private void setupRv(View footerView) {
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        List<String> mObjectIDList = mConversation.getMembers();
        mUserListAdapter = new DetailRvAdapter(mObjectIDList,this);
        mUserListAdapter.setFooterView(footerView);
        mRv.setLayoutManager(mLayoutManager);
        mRv.setAdapter(mUserListAdapter);
        setTitle(getString(R.string.group_detail));
        mConversationType = typeOfConversation(mConversation);
        setVisibility();
    }

    private void setVisibility(){
        if (mConversationType == ConversationType.Single) {
            mNameLayout.setVisibility(View.GONE);
            mQuitLayout.setVisibility(View.GONE);
        } else {
            mNameLayout.setVisibility(View.VISIBLE);
            mQuitLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Common.MODIFY_NAME){
                String mNewName = data.getStringExtra(Common.INTENT_MODIFY_VALUE);
                 updateName(mConversation, mNewName, new AVIMConversationCallback() {
                     @Override
                     public void done(AVIMException e) {
                         if(filterException(e)){
                             refresh();
                         }
                     }
                 });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refresh(){
        HttpUtils.fetchUsers(mConversation.getMembers(), new HttpUtils.UserCallback() {
            @Override
            public void done(List<AVUser> userList, Exception e) {
                //mUserListAdapter.setDataList(userList);

                mUserListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateName(final AVIMConversation conv, String newName, final AVIMConversationCallback callback) {
        conv.setName(newName);
        conv.updateInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e != null) {
                    if (callback != null) {
                        callback.done(e);
                    }
                } else {
                    if (callback != null) {
                        callback.done(null);
                    }
                }
            }
        });
    }

    public static ConversationType typeOfConversation(AVIMConversation conversation) {
        if (conversation == null) {
            Object typeObject = conversation.getAttribute(ConversationType.TYPE_KEY);
            int typeInt = (Integer) typeObject;
            return ConversationType.fromInt(typeInt);
        } else {
           return ConversationType.Group;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

}
