package com.tengbo.commonlibrary.net;

import android.text.TextUtils;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.Account;

import java.io.IOException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yk
 * @Descriptcion okHttp网络拦截器
 */
public class HttpInterceptor implements Interceptor {


    private static final int ACCESS_TOKEN_INVALID = 401;
    private static final String HEADER_TOKEN_KEY = "Authorization";
    //TODO 刷新token的url设置
    private static final String URL_REFRESH_TOKEN = "";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String url = request.url().url().toString();
        LogUtil.d(url + "----start request");

        String token = getToken();
        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder().addHeader(HEADER_TOKEN_KEY, token).build();
        }


        Response response = chain.proceed(request);
        if (isTokenInvalid(response)) {
            String newToken = getNewToken();
            if (TextUtils.isEmpty(newToken)) {
                toLogin();
            } else {
                request = request
                        .newBuilder()
                        .removeHeader(HEADER_TOKEN_KEY)
                        .addHeader(HEADER_TOKEN_KEY, newToken)
                        .build();
                return chain.proceed(request);
            }
        }

        return response;
    }

    private String getNewToken() throws IOException {
        String refreshToken = User.getRefreshToken();
        if (TextUtils.isEmpty(refreshToken)) {
            return null;
        }
        Request request = new Request.Builder()
                .url(URL_REFRESH_TOKEN)
                .addHeader(HEADER_TOKEN_KEY, refreshToken)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        if (response.code() != 200) {
            return null;
        }
        return getTokenFromResponseAndSave(response);
    }


    private String getTokenFromResponseAndSave(Response response) {
        // TODO 从刷新token的response中获取token保存
        return null;
    }


    private boolean isTokenInvalid(Response response) {
        return response.code() == ACCESS_TOKEN_INVALID;
    }


    private void toLogin() {
        // TODO 退出到登陆界面
        BaseApplication.get().toLogin();
        throw new ApiException(ACCESS_TOKEN_INVALID, "登陆已经失效，请重新登陆");
    }


    private String getToken() {
        return User.getAccessToken();
    }

}
