package com.tengbo.commonlibrary.net;

import com.tamic.novate.callback.RxResultCallback;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.commonBean.Account;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Novate {


    private com.tamic.novate.Novate novate;

    public static Novate getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Novate INSTANCE = new Novate();
    }

    public com.tamic.novate.Novate getRealNet() {
        if (novate == null) {
            novate = new com.tamic.novate.Novate.Builder(BaseApplication.get())
                    .baseUrl("")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加拦截器
                    .addInterceptor(null)
                    //添加网络拦截器
                    .addNetworkInterceptor(null)

                    .build();

//            novate.rxGet("", null, new ProgressCallBack<Account>() {
//                @Override
//                protected void on_next(Account account) {
//
//                }
//            });
        }

        return novate;
    }

    public com.tamic.novate.Novate.Builder getRealNetBuilder() {
        return new com.tamic.novate.Novate.Builder(BaseApplication.get());
    }
}
