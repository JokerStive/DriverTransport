package com.tengbo.commonlibrary.net;

import com.tamic.novate.Novate;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.Config;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 网络管理器，用于初始化网络框架
 */
public class NetHelper {

    private static final long TIME_OUT = 30;
    private static ApiService apis;
    private Novate novate;

    public static NetHelper getInstance() {
        return NetHelper.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final NetHelper INSTANCE = new NetHelper();
    }



    public Novate getRealNet() {
        if (novate == null) {
            novate = new Novate.Builder(BaseApplication.get())
                    .baseUrl("http://192.168.3.10:8000/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加拦截器
                    .addInterceptor(new HttpLoggingInterceptor())
                    //添加网络拦截器
                    .addNetworkInterceptor(new HttpInterceptor())

                    .build();

        }
        return novate;
    }

    public Retrofit.Builder getApiBuilder() {
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Config.BASE_URL);
    }




    public ApiService getApi() {
        apis = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Config.BASE_URL)
                .build().create(ApiService.class);
        return apis;
    }


    private static OkHttpClient getOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }


    private static X509TrustManager getTrustManager() {
        return null;
    }

    private static SSLSocketFactory getSslSocketFactory() {
        return null;
    }


}
