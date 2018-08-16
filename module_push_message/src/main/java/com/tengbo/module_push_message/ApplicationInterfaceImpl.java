package com.tengbo.module_push_message;

import android.util.Log;

import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.ApplicationInterface;

import cn.jpush.android.api.JPushInterface;

public class ApplicationInterfaceImpl implements ApplicationInterface {
    @Override
    public void onCreate(BaseApplication application) {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(application);     // 初始化 JPush

        Log.e("JPush", "module_push_message ApplicationInterfaceImpl");
    }
}
