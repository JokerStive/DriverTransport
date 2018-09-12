package com.tengbo.commonlibrary.base;

import android.app.Application;
import android.content.Intent;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.BuildConfig;
import com.tengbo.commonlibrary.common.Constance;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;


public class BaseApplication extends Application {
    private static BaseApplication context;

    public static BaseApplication get() {

        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LogUtil.initLogger();
        initFragmentation();

    }

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
