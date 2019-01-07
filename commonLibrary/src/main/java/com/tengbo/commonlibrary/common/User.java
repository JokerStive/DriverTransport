package com.tengbo.commonlibrary.common;

import com.tengbo.basiclibrary.utils.ACache;
import com.tengbo.basiclibrary.utils.SpUtils;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.commonBean.Account;

import java.util.Objects;

public class User {
    private static final String USER_ACCESS_TOKEN = "accessToken";
    private static final String USER_REFRESH_TOKEN = "refreshToken";
    private static final String USER_AUTO_LOGIN = "autoLogin";
    private static final String USER_INFO = "userInfo";
    private static final String USER_AVATAR = "avatar";


    public static void saveRefreshToken(String refreshToken) {
        ACache.get(BaseApplication.get()).put(USER_REFRESH_TOKEN, refreshToken);
    }

    public static void saveAccessToken(String accessToken) {
        ACache.get(BaseApplication.get()).put(USER_ACCESS_TOKEN, accessToken);
    }


    public static String getAccessToken() {
        return ACache.get(BaseApplication.get()).getAsString(USER_ACCESS_TOKEN);
    }

    public static String getRefreshToken() {
        return ACache.get(BaseApplication.get()).getAsString(USER_REFRESH_TOKEN);
    }


    public static void saveAvatar(String avatar) {
        ACache.get(BaseApplication.get()).put(USER_AVATAR, avatar);
    }

    public static String getAvatar() {
        return ACache.get(BaseApplication.get()).getAsString(USER_AVATAR);
    }


    public static String getIdNumber() {
//        return "511222198308168739";
        Account account = getAccount();
        Objects.requireNonNull(account);
        return account.getIdNumber();
    }


    public static String getName() {
        Account account = getAccount();
        Objects.requireNonNull(account);
        return account.getUserName();
    }


    public static String getAccountId() {
        Account account = getAccount();
        Objects.requireNonNull(account);
        return account.getAccountId();
    }

    public static String getUserId() {
        Account account = getAccount();
        Objects.requireNonNull(account);
        return account.getUserId();
    }


    public static void clear() {
        ACache.get(BaseApplication.get()).clear();
    }


    /**
     * @param account
     */
    public static void saveAccount(Account account) {
        ACache.get(BaseApplication.get()).put(USER_INFO, account);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static Account getAccount() {
        return (Account) ACache.get(BaseApplication.get()).getAsObject(USER_INFO);
    }

    public static void saveAutoLogin(boolean autoLogin) {
        SpUtils.putBoolean(BaseApplication.get(), USER_AUTO_LOGIN, autoLogin);
    }

    public static boolean isAutoLogin() {
        return SpUtils.getBoolean(BaseApplication.get(), USER_AUTO_LOGIN, true);
    }
}
