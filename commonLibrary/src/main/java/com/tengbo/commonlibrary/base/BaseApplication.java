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

import org.litepal.LitePal;

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

    }

}
