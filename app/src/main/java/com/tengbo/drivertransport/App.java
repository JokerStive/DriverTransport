package com.tengbo.drivertransport;


import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.base.BaseApplication;

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initCC();
    }


    private void initCC() {
        CC.init(get());
        CC.enableRemoteCC(true);
        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
    }
}
