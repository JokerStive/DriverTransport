package com.tengbo.commonlibrary.commonBean;

import java.io.Serializable;

public class Account implements Serializable {




    /**
     * userId : 用户id
     * accountId : 账户id
     * userName : 用户姓名
     * idNumber : 身份证号
     * userAvatar : 用户头像地址信息
     */

    private String userId;
    private String accountId;
    private String userName;
    private String idNumber;
    private String userAvatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
