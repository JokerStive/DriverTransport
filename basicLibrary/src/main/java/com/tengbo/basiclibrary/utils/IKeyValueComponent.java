package com.tengbo.basiclibrary.utils;

import android.os.Parcelable;

public interface IKeyValueComponent {

    void putBoolean(String key, boolean value);

    void putInt(String key, int value);

    void putLong(String key,long value);

    void putFloat(String key,float value);

    void putDouble(String key,double value);

    void putBytes(String key,byte[] value);

    void putString(String key,String value);

    void putParcelable(String key, Parcelable value);


    boolean getBoolean(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    byte[] getBytes(String key);

    String getString(String key);

    <T extends Parcelable> T   getParcelable(String key,Class<T> tClass );


    boolean containsKey(String key);


    void clear();
}

