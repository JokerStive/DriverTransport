package com.tengbo.module_order.ui.processing;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.module_order.bean.Node;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Step;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Step> createSteps(List<Node> nodes) {
        List<Step>  steps = new ArrayList<>();
//        for(Node node:nodes){
//            //系统节点，自带步骤
//            if(node.getNodeType()==1){
//                steps.addAll(node.getSteps());
//            }
//            //自动通过节点，构造步骤
//            else if(node.getNodeType()==2){
//
//
//            }
//        }
        return steps;
    }
}
