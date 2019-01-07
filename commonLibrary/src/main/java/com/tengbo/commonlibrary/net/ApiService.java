package com.tengbo.commonlibrary.net;


import com.tengbo.commonlibrary.commonBean.Account;
import com.tengbo.commonlibrary.commonBean.BankCardInfo;
import com.tengbo.commonlibrary.commonBean.FileUrl;
import com.tengbo.commonlibrary.commonBean.LoginInfo;
import com.tengbo.commonlibrary.commonBean.PersonInfo;
import com.tengbo.commonlibrary.commonBean.Token;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiService {


    /**
     * 登录
     */
    @POST("auth/login")
    Observable<BaseResponse<LoginInfo>> login(@Body RequestBody body);



//    /**
//     * 登录
//     */
//    @POST()
//    Observable<BaseResponse<LoginInfo>> testLogin(@Url String url,@Body RequestBody body);


    /**
     * 修改密码
     */
    @POST("service/permission/updatePassword")
    Observable<BaseResponse<Object>> updatePassword(@Body RequestBody body);

    /**
     * 获取原银行卡信息
     */
    @POST("user/getUserBankcard")
    Observable<BaseResponse<List<BankCardInfo>>> getBankCardInfos(@Body RequestBody body);


    /**
     * 修改银行卡
     */
    @POST("user/updateUserBankcard")
    Observable<BaseResponse<Object>> updateBankCardInfo(@Body RequestBody body);

    /**
     * 新增银行卡
     */
    @POST("user/addUserBankcard")
    Observable<BaseResponse<Object>> addBankCardInfo(@Body RequestBody body);

    /**
     * 上传文件
     */
    @Multipart
    @POST("file/upload")
    Observable<BaseResponse<FileUrl>> uploadFiles(@PartMap Map<String, RequestBody> param);

    /**
     * 修改用户信息
     */
    @POST("user/updateUserSuInfo")
    Observable<BaseResponse> updateUserInfo(@Body Account account);
}
