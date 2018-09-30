package com.adyl.pushlibrary;

import android.content.Intent;
import android.text.TextUtils;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

public class MessageManager {
    private ArrayList<PushCallBack> callBacks = new ArrayList<>();

    private static class Holder {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        return Holder.INSTANCE;
    }

    public void register(PushCallBack callBack) {
        callBacks.add(callBack);
    }

    public void unRegister(PushCallBack callBack) {
        callBacks.remove(callBack);
    }

    public void execute(Intent intent) {
        String action = intent.getAction();
        for (PushCallBack callBack : callBacks) {

            if (TextUtils.equals(action, JPushInterface.ACTION_MESSAGE_RECEIVED)) {
                String data = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
                callBack.onMessageReceive(data);
            }

            else if (TextUtils.equals(action, JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
                callBack.onNotificationReceive();
            }

            else if (TextUtils.equals(action, JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                callBack.onNotificationClick();
            }
        }
    }
}
