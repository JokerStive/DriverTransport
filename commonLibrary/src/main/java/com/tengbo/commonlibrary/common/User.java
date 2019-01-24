package com.tengbo.commonlibrary.common;


import com.tengbo.basiclibrary.utils.MMKVCache;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.commonBean.Account;

import java.util.Objects;

/**
 * @author yk_de
 */
public class User {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String ACCOUNT = "userInfo";
    private static final String AVATAR_PATH = "avatar";
    private static MMKVCache mmkvCache;


    static {
        mmkvCache = new MMKVCache(BaseApplication.get());
    }


    public static void saveRefreshToken(String refreshToken) {
        mmkvCache.putString(REFRESH_TOKEN, refreshToken);
    }

    public static void saveAccessToken(String accessToken) {
        mmkvCache.putString(ACCESS_TOKEN, accessToken);
    }


    public static String getAccessToken() {
        return mmkvCache.getString(ACCESS_TOKEN);
    }

    public static String getRefreshToken() {
        return mmkvCache.getString(REFRESH_TOKEN);
    }


    public static void putAvatarPath(String avatarPath) {
        mmkvCache.putString(AVATAR_PATH, avatarPath);
    }

    public static String getAvatarPath() {
        return mmkvCache.getString(AVATAR_PATH);
    }


    public static String getIdNumber() {
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
        mmkvCache.clear();
    }


    public static void putAccount(Account account) {
        mmkvCache.putParcelable(ACCOUNT, account);
    }


    private static Account getAccount() {
        return mmkvCache.getParcelable(ACCOUNT, Account.class);
    }

}
