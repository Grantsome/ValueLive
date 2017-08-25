package com.grantsome.valuelive.utils;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.leancloud.chatkit.LCChatKitUser;

/**
 * Created by Grantsome on 2017/8/17.
 */

public class HttpUtils {

    private static Map<String, AVUser> userMap;

    static {
        userMap = new HashMap<String, AVUser>();
    }

    private static List<AVObject> mHotLiveList = new ArrayList<>();

    private static List<AVUser> mUserList = new ArrayList<>();

    private static List<LCChatKitUser> sChatUserList = new ArrayList<>();

    private static List<AVObject> sConversationList = new ArrayList<>();

    private static List<AVObject> sCommonLiveList = new ArrayList<>();

    public static List<AVObject> getCommonLiveList(){
        if(sCommonLiveList.size() == 0){
            AVQuery<AVObject> avQuery = new AVQuery<>("Commonive");
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(null == e){
                        sCommonLiveList.clear();
                        sCommonLiveList.addAll(list);
                    }
                }
            });
        }
        return sCommonLiveList;
    }

    public static List<AVObject> getHotLiveList(){
        AVQuery<AVObject> avQuery = new AVQuery<>("HotLive");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mHotLiveList.addAll(list);
                } else {
                    e.printStackTrace();
                }
            }
        });
        return mHotLiveList;
    }

    public static List<AVUser> getUserList(){
        AVQuery<AVUser> avQuery = new AVQuery<>("_User");
        avQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    mUserList.clear();
                    mUserList.addAll(list);
                } else {
                    e.printStackTrace();
                }
            }
        });
        return mUserList;
    }

    public static AVUser findUserById(String mObjectId) {
        List<AVUser> userList = getUserList();
        for(AVUser mUser:userList){
            if(mUser.getObjectId().equals(mObjectId)){
                Log.d("CreateLiveActivity", "findUserById: ");
                return mUser;
            }
        }
        return null;
    }

    public static List<String> findJoinConversationNameListById(String mObjectId) {
        List<AVObject> objectList = getConversationList();
        List<String> conversationNameList = new ArrayList<>();
        for(AVObject object:objectList){
            for(Object memberO:object.getList("m")){
                String memberS = (String) memberO;
                Log.d("HttpUtils", "findJoinConversationIdListById: ObjectId:"+mObjectId);
                Log.d("HttpUtils", "findJoinConversationIdListById: memberS:"+memberS);
                if(memberS.equals(mObjectId)){
                    conversationNameList.add(object.get("name").toString());
                    Log.d("HttpUtils", "findJoinConversationIdListById: 相等");
                    break;
                }
            }
        }
        return conversationNameList;
    }

    public static List<String> findCreateConversationIdListById(String mObjectId) {
        List<AVObject> objectList = getConversationList();
        List<String> conversationNameList = new ArrayList<>();
        for(AVObject object:objectList){
                String createrId = object.get("createrId").toString();
                Log.d("HttpUtils", "findJoinConversationIdListById: ObjectId:"+mObjectId);
                Log.d("HttpUtils", "findJoinConversationIdListById: memberS:"+mObjectId);
                if(createrId.equals(mObjectId)){
                    conversationNameList.add(object.getObjectId().toString());
                    Log.d("HttpUtils", "findJoinConversationIdListById: 相等");
                    break;
                }
        }
        return conversationNameList;
    }

    public static List<String> findCreateConversationNameListById(String mObjectId) {
        List<AVObject> objectList = getConversationList();
        List<String> conversationNameList = new ArrayList<>();
        for(AVObject object:objectList){
            String createrId = object.get("createrId").toString();
            Log.d("HttpUtils", "findJoinConversationIdListById: ObjectId:"+mObjectId);
            Log.d("HttpUtils", "findJoinConversationIdListById: memberS:"+mObjectId);
            if(createrId.equals(mObjectId)){
                conversationNameList.add(object.get("name").toString());
                Log.d("HttpUtils", "findJoinConversationIdListById: 相等");
                break;
            }
        }
        return conversationNameList;
    }

    public static List<String> findJoinConversationIdListById(String mObjectId) {
        List<AVObject> objectList = getConversationList();
        List<String> conversationIdList = new ArrayList<>();
        for(AVObject object:objectList){
            for(Object memberO:object.getList("m")){
                String memberS = (String) memberO;
                Log.d("HttpUtils", "findJoinConversationIdListById: ObjectId:"+mObjectId);
                Log.d("HttpUtils", "findJoinConversationIdListById: memberS:"+memberS);
                if(memberS.equals(mObjectId)){
                    conversationIdList.add(object.getObjectId());
                    Log.d("HttpUtils", "findJoinConversationIdListById: 相等");
                    break;
                }
            }
        }
        return conversationIdList;
    }

    public static List<LCChatKitUser> getChatUserList(){
         checkUserList();
         if(mUserList.size() > 0 && sChatUserList.size() == 0){
             for (int i = 0; i < mUserList.size(); i++) {
                 LCChatKitUser chatUser = new LCChatKitUser(mUserList.get(i).getObjectId(),
                         mUserList.get(i).getUsername(),
                         mUserList.get(i).get("avatar").toString().isEmpty()?
                                 "http://i1.bvimg.com/603710/b3d794f02eaad517.png":mUserList.get(i).get("avatar").toString());
                 sChatUserList.add(chatUser);
             }
         }
         return sChatUserList;
     }

     private static void checkUserList(){
         if(mUserList.size() == 0){
             getUserList();
         }
         if(sChatUserList.size() !=0){
             sChatUserList.clear();
         }
     }

    public static List<AVObject> getConversationList() {
        if(sConversationList.size() == 0){
            AVQuery<AVObject> avQuery = new AVQuery<>("_Conversation");
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(null == e){
                        sConversationList.clear();
                        sConversationList.addAll(list);
                        Log.d("HttpUtils", "done: "+list.get(0).getObjectId());
                    }else {
                        e.printStackTrace();
                    }
                }
            });
        }
        Log.d("HttpUtils", "getConversationList: size() = "+sConversationList.size());
        return sConversationList;
    }

    private static void checkConList(){
        if(sConversationList.size()!=0){
            sConversationList.clear();
        }
    }

    public static abstract class UserCallback {
        public abstract void done(List<AVUser> userList, Exception e);
    }

    public static void fetchUsers(final List<String> ids, final UserCallback cacheUserCallback) {
        Set<String> uncachedIds = new HashSet<String>();
        for (String id : ids) {
            if (!userMap.containsKey(id)) {
                uncachedIds.add(id);
            }
        }

        if (uncachedIds.isEmpty()) {
            if (null != cacheUserCallback) {
                cacheUserCallback.done(getUsersFromCache(ids), null);
                return;
            }
        }

        AVQuery<AVUser> q = AVUser.getQuery(AVUser.class);
        q.whereContainedIn("objectId", uncachedIds);
        q.setLimit(1000);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (null == e) {
                    for (AVUser user : list) {
                        userMap.put(user.getObjectId(), user);
                    }
                }
                if (null != cacheUserCallback) {
                    cacheUserCallback.done(getUsersFromCache(ids), e);
                }
            }
        });
    }

    public static List<AVUser> getUsersFromCache(List<String> ids) {
        List<AVUser> userList = new ArrayList<AVUser>();
        for (String id : ids) {
            if (userMap.containsKey(id)) {
                userList.add(userMap.get(id));
            }
        }
        return userList;
    }

    public static AVUser getCachedUser(String objectId) {
        return userMap.get(objectId);
    }

}
