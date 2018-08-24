package com.tengbo.module_main;

import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.base.BaseApplication;

public class MainApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);


        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
    }
}
