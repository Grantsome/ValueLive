package com.grantsome.valuelive.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.model.ConversationType;
import com.grantsome.valuelive.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

public class GroupChatActivity extends LCIMConversationActivity {

    private AVIMConversation mConversation;

    private String mObjectId;

    private List<String> memberList;

    public static final int QUIT_GROUP_REQUEST = 200;

    @Override
    protected void initActionBar(String title) {
        super.initActionBar(title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initBar() {
        if(mConversation != null){
            initActionBar(mConversation.getName());
        }
    }

    @Override
    protected void updateConversation(AVIMConversation conversation) {
        super.updateConversation(conversation);
        this.mConversation = conversation;
        memberList = mConversation.getMembers();
        initBar();
        dealWithIsMember();
    }

    private void dealWithIsMember(){
        SharedPreferences mPreferences = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        final List<String> mAddMemberList = new ArrayList<>();
        if(mObjectId == null){
            mObjectId = mPreferences.getString("objectId",null);
            mAddMemberList.clear();
            mAddMemberList.add(mObjectId);
        }
        if(mConversation!=null){
            if(memberList.size() == 0){
                memberList = mConversation.getMembers();
            }
            Log.d("Adapter", "dealWithIsMember: size() = " + memberList.size());
            if(memberList.size() != 0){
                  check(mAddMemberList);
            }
        }
    }

    private void check(final List<String> mAddMemberList){
        if(!memberList.contains(mObjectId)){
            new AlertDialog.Builder(this).setMessage(R.string.is_add)
                    .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.showHint(getString(R.string.add_member));
                            mConversation.addMembers(mAddMemberList, new AVIMConversationCallback() {
                                @Override
                                public void done(AVIMException e) {
                                    ToastUtils.showHint(getString(R.string.add_success));
                                }
                            });
                        }
                    }).setNegativeButton(R.string.quit_group_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ToastUtils.showHint(getString(R.string.not_member_quit));
                    finish();
                }
            }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gropu_chat, menu);
        if (null != menu && menu.size() > 0) {
            MenuItem item = menu.getItem(0);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.people) {
            if (null != mConversation) {
                Intent intent = new Intent(GroupChatActivity.this,GroupDetailActivity.class);
                intent.putExtra(LCIMConstants.CONVERSATION_ID, mConversation.getConversationId());
                startActivityForResult(intent, QUIT_GROUP_REQUEST);
            }else {
                Log.d("GroupChatActivity", "onOptionsItemSelected: mConversation为空");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void getConversation(String memberId) {
        super.getConversation(memberId);
        createSingleConversation(memberId, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                updateConversation(avimConversation);
            }
        });
    }

    public static void createSingleConversation(String memberId, AVIMConversationCreatedCallback callback) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
        LCChatKit.getInstance().getClient().createConversation(Arrays.asList(memberId), "", attrs, false, true, callback);
    }

}
