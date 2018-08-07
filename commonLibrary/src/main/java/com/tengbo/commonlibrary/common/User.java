package com.tengbo.commonlibrary.common;

import com.tengbo.basiclibrary.utils.SpUtils;
import com.tengbo.commonlibrary.base.BaseApplication;

public class User {
    private static final String USER_ACCESS_TOKEN = "accessToken";
    private static final String USER_REFRESH_TOKEN = "refreshToken";
    private static final String USER_AUTO_LOGIN = "autoLogin";



    public static void saveToken(String accessToken, String refreshToken) {
        SpUtils.putString(BaseApplication.get(), USER_ACCESS_TOKEN, accessToken);
        SpUtils.putString(BaseApplication.get(), USER_REFRESH_TOKEN, accessToken);
    }

    public static String getAccessToken() {
        return SpUtils.getString(BaseApplication.get(), USER_ACCESS_TOKEN, null);
    }

    public static String getRefreshToken() {
        return SpUtils.getString(BaseApplication.get(), USER_REFRESH_TOKEN, null);
    }


    public static void saveAutoLogin(boolean autoLogin) {
         SpUtils.putBoolean(BaseApplication.get(), USER_AUTO_LOGIN, autoLogin);
    }

    public static  boolean  isAutoLogin(){
       return SpUtils.getBoolean(BaseApplication.get(), USER_AUTO_LOGIN, true);
    }
}
