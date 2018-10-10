package com.tengbo.module_order.net;


import com.tengbo.commonlibrary.commonBean.Account;
import com.tengbo.commonlibrary.commonBean.BankCardInfo;
import com.tengbo.commonlibrary.commonBean.FileUrl;
import com.tengbo.commonlibrary.commonBean.LoginInfo;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.module_order.bean.Order;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiOrder {

    /**
     * 上传文件
     */
    @Multipart
    @POST("file/upload")
    Observable<BaseResponse<FileUrl>> uploadFiles(@PartMap Map<String, RequestBody> param);

    /**
     * 获取当前订单
     */
    @POST("app/order/getDriverOrders")
    Observable<BaseResponse<List<Order>>> getDriverOrders(@Body RequestBody body);
}
