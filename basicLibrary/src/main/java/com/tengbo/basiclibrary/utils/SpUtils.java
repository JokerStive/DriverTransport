package com.tengbo.basiclibrary.utils;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * SharedPreferences存储的工具类
 */
public class SpUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static boolean contains(Context context,String key) {
        return SpUtils.getSharedPreferences(context).contains(key);
    }

    public static int getInt(Context context,final String key, final int defaultValue) {
        return SpUtils.getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static boolean putInt(Context context,final String key, final int pValue) {
        final SharedPreferences.Editor editor = SpUtils.getSharedPreferences(context).edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    public static long getLong(Context context,final String key, final long defaultValue) {
        return SpUtils.getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static Long getLong(Context context,final String key, final Long defaultValue) {
        if (SpUtils.getSharedPreferences(context).contains(key)) {
            return SpUtils.getSharedPreferences(context).getLong(key, 0);
        } else {
            return null;
        }
    }


    public static boolean putLong(Context context,final String key, final long pValue) {
        final SharedPreferences.Editor editor = SpUtils.getSharedPreferences(context).edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    public static boolean getBoolean(Context context,final String key, final boolean defaultValue) {
        return SpUtils.getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static boolean putBoolean(Context context,final String key, final boolean pValue) {
        final SharedPreferences.Editor editor = SpUtils.getSharedPreferences(context).edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    public static String getString(Context context,final String key, final String defaultValue) {
        return SpUtils.getSharedPreferences(context).getString(key, defaultValue);
    }

    public static boolean putString(Context context,final String key, final String pValue) {
        final SharedPreferences.Editor editor = SpUtils.getSharedPreferences(context).edit();

        editor.putString(key, pValue);

        return editor.commit();
    }


    public static boolean remove(Context context,final String key) {
        final SharedPreferences.Editor editor = SpUtils.getSharedPreferences(context).edit();

        editor.remove(key);

        return editor.commit();
    }

    public static boolean clear(Context context) {
        final SharedPreferences.Editor editor = SpUtils.getSharedPreferences(context).edit();

        editor.clear();
        return editor.commit();
    }

}