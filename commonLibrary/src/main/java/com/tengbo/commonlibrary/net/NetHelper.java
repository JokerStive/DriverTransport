package com.tengbo.commonlibrary.net;

import com.tengbo.commonlibrary.common.Config;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
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

    public static NetHelper getInstance() {
        return NetHelper.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final NetHelper INSTANCE = new NetHelper();
    }

    public Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl);
    }


    public ApiService getApi(String baseUrl) {
        apis = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
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
                .addNetworkInterceptor(new LogInterceptor(new HttpLogger()))
//                .sslSocketFactory(getSslSocketFactory(), getTrustManager())
                .build();
    }


    private static X509TrustManager getTrustManager() {
        return null;
    }

    private static SSLSocketFactory getSslSocketFactory() {
        return null;
    }


}
