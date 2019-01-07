package com.tengbo.module_order.component;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.module_order.ui.info.InfoFragment;
import com.tengbo.module_order.ui.duty.DutyListFragment;
import com.tengbo.module_order.ui.history.HistoryOrderListFragment;
import com.tengbo.module_order.ui.processing.ProcessingOrderFragment;
import com.tengbo.module_order.ui.task.OrderListFragment;

import java.util.HashMap;
import java.util.Map;

public class ComponentOrder implements IComponent {


    @Override
    public String getName() {
        return ComponentConfig.Order.COMPONENT_NAME;
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        if (actionName.equals(ComponentConfig.Order.ACTION_GET_HOME_PAGE_FRAGMENTS)) {
            returnAllFragment(cc);
        }

        return false;
    }

    private void returnAllFragment(CC cc) {
        Map<String, Object> fragmentMap = new HashMap<>();
        fragmentMap.put(ComponentConfig.Order.ACTION_GET_HISTORY_FRAGMENT, HistoryOrderListFragment.newInstance());
        fragmentMap.put(ComponentConfig.Order.ACTION_GET_INFO_FRAGMENT, InfoFragment.newInstance());
        fragmentMap.put(ComponentConfig.Order.ACTION_GET_PROCESSING_FRAGMENT, ProcessingOrderFragment.newInstance());
        fragmentMap.put(ComponentConfig.Order.ACTION_GET_TASK_FRAGMENT, OrderListFragment.newInstance());
        fragmentMap.put(ComponentConfig.Order.ACTION_GET_DUTY_FRAGMENT, DutyListFragment.newInstance());
        CC.sendCCResult(cc.getCallId(), CCResult.success(fragmentMap));

    }

    private void dealGetFragmentRequest(CC cc, String actionName) {
        if (TextUtils.equals(actionName, ComponentConfig.Order.ACTION_GET_HISTORY_FRAGMENT)) {
            returnFragment(cc, actionName, HistoryOrderListFragment.newInstance());
        } else if (TextUtils.equals(actionName, ComponentConfig.Order.ACTION_GET_PROCESSING_FRAGMENT)) {
            returnFragment(cc, actionName, ProcessingOrderFragment.newInstance());
        } else if (TextUtils.equals(actionName, ComponentConfig.Order.ACTION_GET_INFO_FRAGMENT)) {
            returnFragment(cc, actionName, InfoFragment.newInstance());
        } else if (TextUtils.equals(actionName, ComponentConfig.Order.ACTION_GET_TASK_FRAGMENT)) {
            returnFragment(cc, actionName, OrderListFragment.newInstance());
        }

    }

    private void returnFragment(CC cc, String actionName, Fragment fragment) {
        CC.sendCCResult(cc.getCallId(), CCResult.success(actionName, fragment));
    }
}
