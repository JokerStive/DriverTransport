package com.tengbo.commonlibrary.net;


import com.tengbo.commonlibrary.commonBean.Account;
import com.tengbo.commonlibrary.commonBean.Token;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<Token>> login(@Field("f_account_name") String username ,@Field("f_login_pwd") String password);

}
