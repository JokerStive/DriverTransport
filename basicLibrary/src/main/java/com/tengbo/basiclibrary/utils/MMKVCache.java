package com.tengbo.basiclibrary.utils;

import android.content.Context;
import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

public class MMKVCache implements IKeyValueComponent {

    private final MMKV mmkv;

    public MMKVCache(Context context) {
        MMKV.initialize(context);
        mmkv = MMKV.defaultMMKV();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mmkv.encode(key,value);
    }



    @Override
    public void putInt(String key, int value) {
        mmkv.encode(key,value);
    }

    @Override
    public void putLong(String key, long value) {
        mmkv.encode(key,value);
    }

    @Override
    public void putFloat(String key, float value) {
        mmkv.encode(key,value);
    }

    @Override
    public void putDouble(String key, double value) {
        mmkv.encode(key,value);
    }

    @Override
    public void putBytes(String key, byte[] value) {
        mmkv.encode(key,value);
    }

    @Override
    public void putString(String key, String value) {
        mmkv.encode(key,value);
    }

    @Override
    public void putParcelable(String key, Parcelable value) {
        mmkv.encode(key,value);
    }

    @Override
    public boolean getBoolean(String key) {
        return mmkv.decodeBool(key);
    }

    @Override
    public int getInt(String key) {
        return mmkv.decodeInt(key);
    }

    @Override
    public long getLong(String key) {
        return mmkv.decodeLong(key);
    }

    @Override
    public float getFloat(String key) {
        return mmkv.decodeFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return mmkv.decodeDouble(key);
    }

    @Override
    public byte[] getBytes(String key) {
        return mmkv.decodeBytes(key);
    }

    @Override
    public String getString(String key) {
        return mmkv.decodeString(key);
    }

    @Override
    public <T extends Parcelable> T getParcelable(String key, Class<T> tClass) {
        return mmkv.decodeParcelable(key,tClass);
    }


    @Override
    public boolean containsKey(String key) {
        return mmkv.containsKey(key);
    }

    @Override
    public void clear() {
        mmkv.clearAll();
    }
}
