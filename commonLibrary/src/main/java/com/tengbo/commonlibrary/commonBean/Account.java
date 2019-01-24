package com.tengbo.commonlibrary.commonBean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author yk_de
 */
public class Account implements Parcelable {




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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.accountId);
        dest.writeString(this.userName);
        dest.writeString(this.idNumber);
        dest.writeString(this.userAvatar);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.userId = in.readString();
        this.accountId = in.readString();
        this.userName = in.readString();
        this.idNumber = in.readString();
        this.userAvatar = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
