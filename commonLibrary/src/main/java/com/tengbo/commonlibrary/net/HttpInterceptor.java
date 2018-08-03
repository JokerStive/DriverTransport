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
        NetHelper.getInstance()
                .getApi()
                .getMe()
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Account>() {
                    @Override
                    protected void on_next(Account account) {

                    }
                });




        Request request = chain.request();

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
