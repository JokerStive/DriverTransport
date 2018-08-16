package com.tengbo.commonlibrary.net;


import com.tengbo.commonlibrary.commonBean.BankCardInfo;
import com.tengbo.commonlibrary.commonBean.PersonInfo;
import com.tengbo.commonlibrary.commonBean.Token;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<Token>> login(@Field("faccountName") String username , @Field("faccountName") String password);

    /**
     * 获取个人信息
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/info/selectInfo")
    Observable<BaseResponse<PersonInfo>> getPersonalInfo(@Field("faccountId") int token);

    /**
     * 修改密码
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("updatePasswd")
    Observable<BaseResponse> updatePassword(@Field("faccountId") int token, @FieldMap Map<String, String> map);

    /**
     * 获取原银行卡信息
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/bankcard/selectInfo")
    Observable<BaseResponse<BankCardInfo>> getBankCardInfo(@Field("faccountId") int token);

    /**
     * 银行卡信息更改
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/bankcard/updateInfo")
    Observable<BaseResponse> updateBankCardInfo(@Field("faccountId") int token, @FieldMap Map<String, String> map);

    /**
     *
     * @param token
     * @param params
     * @return
     */
    @Multipart
    @POST("file/upload")
    Observable<BaseResponse> uploadAvatar(@Query("faccountId") int token, @PartMap Map<String, RequestBody> params);

    /**
     * 上传头像地址
     * @param token
     * @param address
     * @return
     */
    @FormUrlEncoded
    @POST("app/client/driver/liscense/user/info/updateImage")
    Observable<BaseResponse> uploadAvatarAddress(@Field("faccountId") int token, @Field("fuserAvatar") String address);
}
