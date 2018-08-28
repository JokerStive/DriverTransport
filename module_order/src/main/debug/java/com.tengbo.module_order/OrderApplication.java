package com.tengbo.module_order;

import android.support.annotation.NonNull;

import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.base.BaseApplication;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

public class OrderApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);

        Fragmentation.builder().debug(true)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(@NonNull Exception e) {

                    }
                })
                .stackViewMode(Fragmentation.BUBBLE)
                .install();
    }


}
