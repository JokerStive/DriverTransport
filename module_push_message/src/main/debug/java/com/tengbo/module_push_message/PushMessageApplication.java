package com.tengbo.module_push_message;

import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.base.BaseApplication;

import cn.jpush.android.api.JPushInterface;

public class PushMessageApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
