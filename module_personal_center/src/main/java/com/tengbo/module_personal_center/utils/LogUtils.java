package com.tengbo.module_personal_center.utils;

import android.util.Log;

/**
 * @author Wang Chenchen
 * @date 2018/8/17
 * 日志打印工具类
 */
public class LogUtils {

    // 控制日志打印标识
    private static final boolean isDebug = true;

    public static final void v(String TAG, String msg)
    {
        if(isDebug)
            Log.v(TAG, msg);
    }

    public static final void d(String TAG, String msg)
    {
        if(isDebug)
            Log.d(TAG, msg);
    }

    public static final void i(String TAG, String msg)
    {
        if(isDebug)
            Log.i(TAG, msg);
    }

    public static final void w(String TAG, String msg)
    {
        if(isDebug)
            Log.w(TAG, msg);
    }

    public static final void e(String TAG, String msg)
    {
        if(isDebug)
            Log.e(TAG, msg);
    }
}
