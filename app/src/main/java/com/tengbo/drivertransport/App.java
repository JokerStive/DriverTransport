package com.tengbo.drivertransport;


import com.billy.cc.core.component.CC;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.BuildConfig;
import com.tengbo.commonlibrary.base.BaseApplication;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * @author yk_de
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initCC();
        initFragmentation();
        initDB();
        initPush();
        initLogger();
        initBugly();
        initLeakCanary();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initBugly() {
        Bugly.init(this,"e5138a10e8", BuildConfig.DEBUG);
    }

    private void initLogger() {
        LogUtil.initLogger();
    }


    /**
     *@Desc  初始化激光推送
     */
    private void initPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }


    /**
     *@Desc  初始化组建通讯框架
     */
    private void initCC() {
        CC.init(get());
        CC.enableRemoteCC(true);
        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
    }


    /**
     * @Desc 初始化数据库
     */
    private void initDB() {
        LitePal.initialize(this);
        LitePal.getDatabase();
    }

    /**
     *@Desc
     */
    private void initFragmentation() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                // 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                    }
                })
                .install();
    }
}
