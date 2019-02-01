package com.tengbo.module_order.net;


import com.tengbo.commonlibrary.commonBean.FileUrl;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.module_order.bean.CheckRecord;
import com.tengbo.module_order.bean.Dictionary;
import com.tengbo.module_order.bean.DutyRecord;
import com.tengbo.module_order.bean.DutyTask;
import com.tengbo.module_order.bean.GPSPoints;
import com.tengbo.module_order.bean.Message;
import com.tengbo.module_order.bean.Mission;
import com.tengbo.module_order.bean.Node;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Scheme;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepOperation;
import com.tengbo.module_order.bean.Task;
import com.tengbo.module_order.bean.Trouble;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiOrder {

    /**
     * /**
     * 获取司机当前订单
     */
    @POST("app/order/getDriverOrders")
    Observable<BaseResponse<BaseListResponse<Order>>> getDriverOrders(@Body RequestBody body);


    /**
     * 获取司机历史订单
     */
    @POST("app/order/getDriverHistoryOrders")
    Observable<BaseResponse<BaseListResponse<Order>>> getDriverHistoryOrders(@Body RequestBody body);


    /**
     * 根据状态获取单个订单详细信息
     */
    @POST("app/order/getDriverOrderInfo")
    Observable<BaseResponse<Order>> getDriverOrderInfo(@Body RequestBody body);


    /**
     * 设置司机订单状态
     */
    @POST("app/order/setDriverOrderStatus")
    Observable<BaseResponse<Order>> setDriverOrderStatus(@Body RequestBody body);


    /**
     * 获取司机执行步骤
     */
    @POST("order/getOrderSteps")
    Observable<BaseResponse<Scheme>> getOrderSteps(@Body RequestBody body);


    /**
     * 设置步骤操作信息
     */
    @POST("app/order/setOrderStepOperation")
    Observable<BaseResponse<Object>> setOrderStepOperation(@Body StepOperation operation);


    /**
     * 设置自动出发步骤操作信息
     */
    @POST("app/order/setTriggerNodeInfo")
    Observable<BaseResponse<Object>> setAutoStepOperation(@Body StepOperation operation);


    /**
     * 增加节点故障信息
     */
    @POST("app/order/addDriverTrouble")
    Observable<BaseResponse<Object>> addDriverTrouble(@Body Trouble trouble);

    /**
     * 查询当前节点故障信息
     */
    @POST("order/getCurrentTrouble")
    Observable<BaseResponse<Trouble>> getCurrentTrouble(@Body RequestBody body);

    /**
     * 修改节点故障信息
     */
    @POST("app/order/updateDriverTrouble")
    Observable<BaseResponse<Object>> updateDriverTrouble(@Body Trouble trouble);


    /**
     * 查询订单任务信息
     */
    @POST("app/order/getOrderMissions")
    Observable<BaseResponse<List<Mission>>> getOrderMissions(@Body RequestBody body);


    /**
     * 添加行车三检记录
     */
    @POST("app/vehicle/addvehicleCheckRecord")
    Observable<BaseResponse<Object>> addCheckRecord(@Body CheckRecord checkRecord);


    /**
     * 添加行车三检记录
     */
    @POST("vehicle/addvehicleCheckRecord")
    Observable<BaseResponse<Object>> testAddCheckRecord(@Url String url, @Body CheckRecord checkRecord);

    /**
     * 查询步骤操作纪录
     */
    @POST("order/getOrderStepOperationInfo")
    Observable<BaseResponse<List<Node>>> getStepHistoryRecord(@Body RequestBody body);

    /**
     * 查询步骤操作纪录详情
     */
    @POST("order/getStepDetails")
    Observable<BaseResponse<Step>> getStepRecordDetail(@Body RequestBody body);


    /**
     * 上传车辆GPS轨迹信息
     */
    @POST("service/gis/addGpsPoints")
    Observable<BaseResponse<Object>> addGPSPoints(@Body GPSPoints gpsPoints);


    /**
     * 查询司机值班任务
     */
    @POST("app/duty/getDutyTasks")
    Observable<BaseResponse<DutyTask>> getDutyTasks(@Body Task task);

    /**
     * 查询司机值班任务
     */
    @POST()
    Observable<BaseResponse<DutyTask>> testGetDutyTasks(@Url String url, @Body Task task);


    /**
     * 添加司机值班任务
     */
    @POST("app/duty/addDutyTask")
    Observable<BaseResponse<Object>> addDutyTasks(@Body Task task);



    /**
     * 修改司机值班任务
     */
    @POST("app/duty/updateDutyTask")
    Observable<BaseResponse<Object>> updateDutyTasks(@Body Task task);




    /**
     * 查询值班纪录
     */
    @POST("duty/getDutyRecord")
    Observable<BaseResponse<DutyRecord>> getDutyRecord(@Body Task task);




    /**
     * 查字典
     */
    @POST("dictionary/getDictionaryItem")
    Observable<BaseResponse<List<Dictionary>>> getDictionaryItem(@Body RequestBody body);


    /**
     * 获取消息列表
     */
    @POST("message/getMessageList")
    Observable<BaseResponse<BaseListResponse<Message>>> getMessageList(@Body RequestBody body);


    /**
     * 上传文件
     */
    @POST("file/upload/batch")
    Observable<BaseResponse<List<String>>> uploadMultiFiles(@Body MultipartBody multipartBody);


}
