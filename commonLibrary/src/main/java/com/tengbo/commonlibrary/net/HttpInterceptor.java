package com.tengbo.commonlibrary.net;

import android.text.TextUtils;

import com.tengbo.commonlibrary.commonBean.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * oKHttp拦截器，自己扩展
 */
public class HttpInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        // TODO 这里做添加token和刷新token的操作
        // TODO 1.如何判定token失效，2.如何同步刷新token让用户无感 3.刷新token期间其他request拒绝
        String token = getToken();
        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder().addHeader("Authorization", token).build();
        }

        return chain.proceed(request);
    }


    private String getToken() {
        return null;
    }
}
