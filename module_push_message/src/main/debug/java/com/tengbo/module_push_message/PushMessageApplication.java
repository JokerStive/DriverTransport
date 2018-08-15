package com.tengbo.module_personal_center;

import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.base.BaseApplication;

public class PersonalCenterApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);
    }
}
