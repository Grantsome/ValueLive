package com.grantsome.valuelive.provider;

import com.grantsome.valuelive.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by Grantsome on 2017/8/21.
 */

public class UserProvider implements LCChatProfileProvider {

    private static UserProvider sUserProvider;

    private static List<LCChatKitUser> sChatUserList = HttpUtils.getChatUserList();

    public synchronized static UserProvider getInstance(){
        if(null == sUserProvider){
            sUserProvider = new UserProvider();
        }
        return sUserProvider;
    }

    private UserProvider() {
    }

    @Override
    public void fetchProfiles(List<String> list, LCChatProfilesCallBack lcChatProfilesCallBack) {
        List<LCChatKitUser> userList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < sChatUserList.size(); j++) {
                if(sChatUserList.get(j).getUserId().equals(list.get(i))){
                    userList.add(sChatUserList.get(j));
                }
            }
        }
        lcChatProfilesCallBack.done(userList,null);
    }

}
