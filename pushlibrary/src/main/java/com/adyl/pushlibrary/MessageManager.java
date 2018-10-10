package com.adyl.pushlibrary;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;

public class MessageManager {
    private HashMap<String, PushCallBack> callBacks = new HashMap<>();

    private static class Holder {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        return Holder.INSTANCE;
    }

    public void register(String key, PushCallBack callBack) {
        callBacks.put(key, callBack);
        Log.d("jiguang", "callback  count --" + callBacks.size());
    }

    public void unRegister(String key) {
        callBacks.remove(key);
    }

    public void execute(Intent intent) {
        String action = intent.getAction();
        for (Map.Entry<String, PushCallBack> key : callBacks.entrySet()) {
            PushCallBack callBack = key.getValue();
            if (TextUtils.equals(action, JPushInterface.ACTION_MESSAGE_RECEIVED)) {
                String data = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
                callBack.onMessageReceive(data);
            } else if (TextUtils.equals(action, JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
                callBack.onNotificationReceive();
            } else if (TextUtils.equals(action, JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                callBack.onNotificationClick();
            }
        }
    }
}
