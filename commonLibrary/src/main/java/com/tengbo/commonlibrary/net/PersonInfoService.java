package com.tengbo.commonlibrary.net;

import com.tengbo.commonlibrary.commonBean.PersonInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface PersonInfoService {

    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/info/selectInfo")
    Observable<BaseResponse<PersonInfo>> getPersonalInfo(@Field("faccountId") int token);

}
