package com.tengbo.module_exapmle;

import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.base.BaseApplication;

public class ExampleApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);
    }
}
