package com.tengbo.commonlibrary.net;


import com.tengbo.commonlibrary.commonBean.Account;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiService {

    @GET("Accounts/me")
    Observable<BaseResponse<Account>> getMe();

}
