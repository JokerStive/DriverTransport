package com.tengbo.module_personal_center.utils;

/**
 * author WangChenchen
 * 返回码常量类
 */
public class ResponseCode {

    // 请求成功
    public static final int STATUS_OK = 200;

    // 用户不存在
    public static final int USER_NOT_EXIST = 40101;

    // 密码错误
    public static final int PASSWORD_ERROR = 40102;

    // 系统错误
    public static final int SYSTEM_ERROR = 404;

    // 账号被禁用
    public static final int USER_BASIC_INFO_ERROR = 40103;

    // 开户人同用户的身份证号不一致
    public static final int USER_ID_CARD_NUM_ERROR = 40104;
}
