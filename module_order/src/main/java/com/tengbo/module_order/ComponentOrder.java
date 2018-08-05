package com.tengbo.module_order;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.module_order.history.HistoryOrderFragment;

public class ComponentOrder implements IComponent {

    @Override
    public String getName() {
        return ComponentConfig.OrderComponentConfig.COMPONENT_NAME;
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        switch (actionName) {
            case ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT:
                returnFragment(cc);
                break;
        }
        return false;
    }

    private void returnFragment(CC cc) {
        CC.sendCCResult(cc.getCallId(), CCResult.success(ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT, HistoryOrderFragment.newInstance())
        );
    }
}
