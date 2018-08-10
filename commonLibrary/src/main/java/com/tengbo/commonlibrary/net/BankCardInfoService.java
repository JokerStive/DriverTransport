package com.tengbo.commonlibrary.net;

import com.tengbo.commonlibrary.commonBean.BankCardInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface BankCardInfoService {

    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/bankcard/selectInfo")
    Observable<BaseResponse<BankCardInfo>> getBankCardInfo(@Field("faccountId") int token);
}
