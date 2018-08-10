package com.tengbo.commonlibrary.net;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface UpdateBankCardInfoService {
    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/bankcard/updateInfo")
    Observable<BaseResponse> updateBankCardInfo(@Field("faccountId") int token, @FieldMap Map<String, String> map);
}
