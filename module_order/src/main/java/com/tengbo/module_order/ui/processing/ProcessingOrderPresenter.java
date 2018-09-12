package com.tengbo.module_order.ui.processing;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.module_order.bean.Order;

public class ProcessingOrderPresenter extends BasePresenter<ProcessingOrderContract.View>  implements ProcessingOrderContract.Presenter {
    @Override
    public void getOrder(int orderId) {
        LogUtil.d("开始加载当前任务数据--");
        mView.showOrder(new Order());
    }

    @Override
    public void passAction(int actionId, int position) {
        mView.passActionSuccess(position);
    }
}
