package com.tengbo.module_main.ui.update;

import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author yk
 * @Description apk下载管理器
 */
public class DownloadManager {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "M_DEFAULT_DIR";

    private Retrofit.Builder retrofit;

    private static DownloadManager instance;

    private static boolean isLoading = false;


    //获得一个单例类
    public static synchronized DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }


    /**
     * 下载文件
     */
    public void download(String url, DownLoadCallBack callBack) {
        if (isLoading) return;
        isLoading = true;
        if (TextUtils.isEmpty(url)) {
            isLoading = false;
            callBack.onFail(new NullPointerException("apk url can't be  null "));
            return;
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        /*
      目标文件存储的文件名
     */
        String fileName = "test.apk";
        String BASE_URL = "http://wap.dl.pinyin.sogou.com/";
        retrofit.baseUrl(BASE_URL)
                .client(initOkHttpClient())
                .build()
                .create(IFileLoad.class)
                .loadFile(url)
                .enqueue(new FileCallback(fileName) {
                    @Override
                    public void onSuccess(File file) {
                        isLoading = false;
                        callBack.onSuccess(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        callBack.onProgress((int) progress, (int) total);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        isLoading = false;
                        callBack.onFail(t);
                    }
                });
    }


    public interface IFileLoad {
        @GET
        retrofit2.Call<ResponseBody> loadFile(@Url String url);
    }



    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

}
