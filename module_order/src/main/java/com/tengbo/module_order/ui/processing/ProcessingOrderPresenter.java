package com.tengbo.module_order.ui.processing;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.bean.DutyTask;
import com.tengbo.module_order.bean.Node;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Scheme;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepOperation;
import com.tengbo.module_order.bean.Task;
import com.tengbo.module_order.bean.Trouble;
import com.tengbo.module_order.net.ApiOrder;

import java.util.LinkedList;
import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.functions.Func1;
import utils.BeanPropertiesUtil;
import utils.RequestUtils;
import utils.RetrofitUtils;

public class ProcessingOrderPresenter extends BasePresenter<ProcessingOrderContract.View> implements ProcessingOrderContract.Presenter {


    @Override
    public void getProcessingOrder() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("driverId", User.getIdNumber());
        jsonObject.put("orderStatus", 1);
        jsonObject.put("page", 1);
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverOrders(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<BaseListResponse<Order>>() {
                    @Override
                    protected void on_next(BaseListResponse<Order> listResponse) {
                        List<Order> rows = listResponse.getRows();
                        Order order = null;
                        if (rows.size() > 0) {
                            order = rows.get(0);
                            getSteps(order.getOrderCode());
                        }
                        mView.getProcessingOrderSuccess(order);
                    }
                });
    }


    public Observable<BaseListResponse<Order>> processingOrderObserable() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("driverId", User.getIdNumber());
        jsonObject.put("orderStatus", 4);
        jsonObject.put("page", 1);
        return NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverOrders(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult());
    }


    @Override
    public void getSteps(String orderCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", orderCode);
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getOrderSteps(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Scheme>(mView) {
                    @Override
                    protected void on_next(Scheme scheme) {
                        List<Step> steps;
                        try {
                            steps = createSteps(orderCode, scheme.getSchemeInstance());
                            mView.showSteps(steps);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void uploadCacheStep(StepOperation stepOperation) {
        long operateTimeMillis = stepOperation.getOperateTimeMillis();
        List<String> pictureLocalPaths = stepOperation.getPictureLocalPaths();
        if (operateTimeMillis == 0 || pictureLocalPaths == null) {
            throw new RuntimeException("数据有误，请检查");
        } else {
            picturesObservable(pictureLocalPaths)
                    .flatMap(new Func1<List<String>, Observable<?>>() {
                        @Override
                        public Observable<?> call(List<String> urls) {
                            stepOperation.setOperatedTimeLength(System.currentTimeMillis() - operateTimeMillis);
                            stepOperation.setAttachs(urls);
                            return stepInfoObservable(stepOperation);
                        }
                    })
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<Object>(mView) {
                        @Override
                        protected void on_next(Object o) {
                            mView.uploadCacheStepSuccess(stepOperation);
                        }
                    });
        }

    }


    private Observable<List<String>> picturesObservable(List<String> paths) {
        MultipartBody multipartBody = RetrofitUtils.createMultipartBody(paths);
        return NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .uploadMultiFiles(multipartBody)
                .compose(RxUtils.handleResult());

    }

    private Observable<Object> stepInfoObservable(StepOperation stepOperation) {
        return NetHelper.getInstance().getRetrofit()
                .create(ApiOrder.class)
                .setOrderStepOperation(stepOperation)
                .compose(RxUtils.handleResult());


    }

    private Observable<Object> troubleObservable(Trouble trouble) {
        return NetHelper.getInstance().getRetrofit()
                .create(ApiOrder.class)
                .addDriverTrouble(trouble)
                .compose(RxUtils.handleResult());


    }


    /**
     * 上传自动通过步骤信息
     *
     * @param stepOperation 步骤信息
     */
    @Override
    public void uploadAutoStep(boolean isCached, StepOperation stepOperation) {
        long operateTimeMillis = stepOperation.getOperateTimeMillis();
        if (operateTimeMillis != 0) {
            stepOperation.setOperatedTimeLength(System.currentTimeMillis() - stepOperation.getOperateTimeMillis());
        }
        NetHelper.getInstance().getRetrofit().create(ApiOrder.class)

                .setAutoStepOperation(stepOperation)
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>() {
                    @Override
                    protected void on_next(Object o) {
                        LogUtil.d("自动步骤上传成功");
                        mView.uploadAutoStepSuccess(isCached, stepOperation);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (operateTimeMillis != 0) {
                            stepOperation.save();
                            mView.uploadStepFail(stepOperation.getPosition());
                            LogUtil.d("自动通过步骤上传失败，已保存--" + stepOperation.getPosition());
                        }
                    }
                });

    }

    @Override
    public void uploadCacheTrouble(Trouble trouble) {
        long operateTimeMillis = trouble.getOperateTimeMillis();
        List<String> pictureLocalPaths = trouble.getPictureLocalPaths();
        if (operateTimeMillis == 0 || pictureLocalPaths == null) {
            throw new RuntimeException("数据有误，请检查");
        } else {
            picturesObservable(pictureLocalPaths)
                    .flatMap(new Func1<List<String>, Observable<?>>() {
                        @Override
                        public Observable<?> call(List<String> urls) {
                            trouble.setTroubleTimeLength(System.currentTimeMillis() - operateTimeMillis);
                            trouble.setAttachs(urls);
                            return troubleObservable(trouble);
                        }
                    })
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<Object>(mView) {
                        @Override
                        protected void on_next(Object o) {
                            mView.uploadCacheTroubleSuccess();
                        }

                    });
        }
    }

    @Override
    public void getDutyTask() {
        Task task = new Task();
        task.setDriverId(User.getIdNumber());
        task.setOperationTime("");
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDutyTasks(task)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<DutyTask>(mView) {
                    @Override
                    protected void on_next(DutyTask dutyTask) {
                        mView.getDutyTaskSuccess(dutyTask);
                    }
                });
    }

    @Override
    public void getAndUpdateTrouble(String orderCode) {
        getCurrentTroubleObservable(orderCode)
                .flatMap(trouble -> updateTroubleObservable(orderCode, trouble.getTroubleId()))
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>() {
                    @Override
                    protected void on_next(Object o) {
                        mView.updateTroubleSuccess();
                    }
                });
    }

    private Observable<Trouble> getCurrentTroubleObservable(String orderCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", orderCode);
        return NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getCurrentTrouble(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult());

    }

    private Observable<Object> updateTroubleObservable(String orderCode, String troubleCode) {
        Trouble trouble = new Trouble();
        trouble.setOrderCode(orderCode);
        trouble.setTroubleId(troubleCode);
        trouble.setTroubleStatus(2);
        return NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .updateDriverTrouble(trouble)
                .compose(RxUtils.handleResult());

    }


    private List<Step> createSteps(String orderCode, List<Node> nodes) throws Exception {
        List<Step> steps = new LinkedList<>();
        LogUtil.d("node个数--"+nodes.size());
        for (Node node : nodes) {
            //如果是自动触发类型的，构造一个步骤
            if (node.getNodeType() == 2) {
                Step step = new Step();
                BeanPropertiesUtil.copyProperties(node, step);
                step.setOrderCode(orderCode);
                step.setStepName("自动触发");
                step.setStepStatus(node.getNodeStatus() == 1 ? 0 : 1);
                step.setProcessed(step.getStepStatus() == 1);
                steps.add(step);
            }

            for (Step step : node.getSteps()) {
                int processNum = step.getProcessNumber();
                BeanPropertiesUtil.copyProperties(node, step);
                step.setOrderCode(orderCode);
                step.setProcessNumber(processNum);
                step.setProcessed(step.getStepStatus() == 1);
                steps.add(step);
            }
        }

        LogUtil.d("step个数--"+nodes.size());
        return steps;
    }


}
