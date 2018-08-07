package com.tengbo.drivertransport;


import com.billy.cc.core.component.CC;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.Constance;

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initCC();
//        initLogger();
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(1)
                .methodOffset(5)
                .tag(Constance.LOGGER_TAG)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private void initCC() {
        CC.init(get());
        CC.enableRemoteCC(true);
        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
    }
}
