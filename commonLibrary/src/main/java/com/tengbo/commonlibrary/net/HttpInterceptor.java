package com.tengbo.commonlibrary.net;

import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.google.gson.Gson;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.Token;

import java.io.IOException;
import java.io.Reader;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import utils.RequestUtils;

/**
 * @author yk
 * @Descriptcion okHttp网络拦截器
 */
public class HttpInterceptor implements Interceptor {


    private static final int TOKEN_INVALID = 700;
    private static final int ACCESS_SUCCESS = 200;
    private static final String HEADER_ACCESS_TOKEN = "Auth-Token";
    private static final String HEADER_KEY_USER_AGENT = "User-Agent";
    private static final String HEADER_VALUE_USER_AGENT = "Android";
    private static final String HEADER_REFRESH_KEY = "Auth-RefreshToken";
    //TODO 刷新token的url设置
    private static final String URL_REFRESH_TOKEN = Config.BASE_URL + "auth/refresh";
    private Gson gson;
    private String mResponseResult;


    HttpInterceptor() {
        gson = new Gson();
    }


    @Override
    public Response intercept(Chain chain) throws IOException {


//        NetworkUtils.isAvailableByPing()
        Request request = chain.request();
        LogUtil.d("发送请求--" + request.url().toString());

        String token = User.getAccessToken();
        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder()
                    .addHeader(HEADER_ACCESS_TOKEN, token)
                    .addHeader(HEADER_KEY_USER_AGENT, HEADER_VALUE_USER_AGENT)
                    .build();
        }


        Response response = chain.proceed(request);
        if (token != null && checkTokenAndData(response)) {
            String newToken = getNewToken();
            if (TextUtils.isEmpty(newToken)) {
                toLogin();
            } else {
                request = request
                        .newBuilder()
                        .removeHeader(HEADER_ACCESS_TOKEN)
                        .addHeader(HEADER_ACCESS_TOKEN, newToken)
                        .addHeader(HEADER_KEY_USER_AGENT, HEADER_VALUE_USER_AGENT)
                        .build();
                return chain.proceed(request);
            }
        }

        if (mResponseResult != null) {
            response = response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), mResponseResult))
                    .build();
        }


        return response;
    }

    /**
     * 用refreshToken刷新token信息
     */
    private synchronized String getNewToken() throws IOException {
        String refreshToken = User.getRefreshToken();
        if (TextUtils.isEmpty(refreshToken)) {
            LogUtil.d("refreshToken 本来就是空 ---重新登陆");
            return null;
        }
        LogUtil.d("accessToken 失效，利用refreshToken刷新");
        Request request = new Request.Builder()
                .url(URL_REFRESH_TOKEN)
                .addHeader(HEADER_REFRESH_KEY, refreshToken)
                .addHeader(HEADER_KEY_USER_AGENT, HEADER_VALUE_USER_AGENT)
                .method("POST", RequestUtils.createRequestBody("{}"))
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        return getTokenFromResponseAndSave(response);
    }


    /**
     * @param response 刷新token的response
     * @return 最新的accessToken
     */
    private String getTokenFromResponseAndSave(Response response) {
        //读取结果信息
        Reader reader = response.body().charStream();

        BaseResponse baseResponse = gson.fromJson(reader, BaseResponse.class);
        int code = baseResponse.getCode();
        if (code == ACCESS_SUCCESS) {
            LogUtil.d("token刷新成功，继续当前请求");
            Object data = baseResponse.getData();
            Token token = gson.fromJson(gson.toJson(data), Token.class);
            User.saveRefreshToken(token.getRefreshToken());
            User.saveAccessToken(token.getAccessToken());
            return token.getAccessToken();
        }
        LogUtil.d("refreshToken也过期，重新登陆");
        return null;
    }


    /**
     * @Desc token是否失效，处理response本身异常
     */
    private boolean checkTokenAndData(Response response) throws IOException {

        //如果response本身异常，则直接抛出错误
        if (response.code() != ACCESS_SUCCESS) {
            throw new ApiException(response.code(), "服务器错误");
        }

        BaseResponse baseResponse = getBaseResponse(response);


        if (baseResponse.getCode() != ACCESS_SUCCESS && baseResponse.getCode() != TOKEN_INVALID) {

            throw new ApiException(baseResponse.getCode(), baseResponse.getMessage());
        }
        return baseResponse.getCode() == TOKEN_INVALID;
    }


    private BaseResponse getBaseResponse(Response response) throws IOException {
        //读取结果信息
        mResponseResult = response.body().string();
        return gson.fromJson(mResponseResult, BaseResponse.class);
    }


    private void toLogin() {
        // TODO 退出到登陆界面
        User.saveAccessToken(null);
        CC.obtainBuilder(ComponentConfig.Main.COMPONENT_NAME)
                .setActionName(ComponentConfig.Main.ACTION_OPEN_LOGIN_ACTIVITY)
                .build().call();
        throw new ApiException(TOKEN_INVALID, "登陆已经失效，请重新登陆");
    }


}
