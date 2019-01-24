package com.tengbo.commonlibrary.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tamic.novate.Novate;
import com.tengbo.commonlibrary.BuildConfig;
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


/**
 * 网络管理器，用于初始化网络框架
 */
public class NetHelper {

    private static final long TIME_OUT = 30;
    private static ApiService apis;
    private static Gson gson;
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

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .build();
    }


    public ApiService getApi() {
        apis = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
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


    private static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .create();
        }
        return gson;
    }

    private static X509TrustManager getTrustManager() {
        return null;
    }

    private static SSLSocketFactory getSslSocketFactory() {
        return null;
    }


}
