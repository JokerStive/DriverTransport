package com.tengbo.commonlibrary.net;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface UpdatePasswordService {

    @FormUrlEncoded
    @POST("updatePasswd")
    Observable<BaseResponse> updatePassword(@Field("faccountId") int token, @FieldMap Map<String, String> map);
}
