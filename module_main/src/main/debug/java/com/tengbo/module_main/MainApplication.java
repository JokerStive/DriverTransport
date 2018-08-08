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
    }
}
