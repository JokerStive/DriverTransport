package com.tengbo.commonlibrary.net;

import com.tamic.novate.Novate;
import com.tengbo.commonlibrary.base.BaseApplication;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetHelper {


    private  Novate novate;

    public static NetHelper getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder{
        private static final NetHelper INSTANCE = new NetHelper();
    }

    public   Novate getRealNet(){
        new Retrofit.Builder()
                .build();
        if (novate==null){
            novate = new Novate.Builder(BaseApplication.get())
                    .baseUrl("")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加拦截器
                    .addInterceptor(null)
                    //添加网络拦截器
                    .addNetworkInterceptor(null)
                    .build();
        }
        return novate;
    }

    public Novate.Builder getRealNetBuilder(){
       return  new Novate.Builder(BaseApplication.get());
    }
}
