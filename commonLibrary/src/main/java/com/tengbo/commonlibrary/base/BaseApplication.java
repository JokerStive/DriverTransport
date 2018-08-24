package com.tengbo.commonlibrary.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.BuildConfig;
import com.tengbo.commonlibrary.common.Constance;


public class BaseApplication extends Application {
    private static BaseApplication context;

    public static BaseApplication get() {

        return context;
    }

    public void toLogin() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LogUtil.initLogger();

    }




}
