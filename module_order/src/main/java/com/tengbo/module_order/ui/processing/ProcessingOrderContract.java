package com.tengbo.module_order.ui.processing;

import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;
import com.tengbo.module_order.bean.DutyTask;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepOperation;
import com.tengbo.module_order.bean.Trouble;

import java.util.List;

public interface ProcessingOrderContract {

    interface View extends IView<ProcessingOrderContract.Presenter> {
        void getProcessingOrderSuccess(Order order);

        void uploadCacheStepSuccess(StepOperation operation);

        void uploadCacheTroubleSuccess();

        void showSteps(List<Step> steps);

        void getDutyTaskSuccess(DutyTask dutyTask);

        void uploadStepFail(int position);

        void uploadAutoStepSuccess(boolean isCached, StepOperation operation);

        void updateTroubleSuccess();
    }

    interface Presenter extends IPresenter<ProcessingOrderContract.View> {
        void getProcessingOrder();

        void getSteps(String orderCode);

        void uploadCacheStep(StepOperation stepCache);

        void uploadAutoStep(boolean isCached, StepOperation stepOperation);

        void uploadCacheTrouble(Trouble trouble);

        void getDutyTask();

        void getAndUpdateTrouble(String orderCode);
    }

}
