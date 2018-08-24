package com.tengbo.module_personal_center.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.module_personal_center.fragment.PersonalCenterFragment;

/**
 * author WangChenchen
 * 个人中心组件
 */
public class PersonalCenterComponent implements IComponent {
    @Override
    public String getName() {
        return ComponentConfig.PersonalCenterComponentConfig.COMPONENT_NAME;
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        switch (actionName) {
            case ComponentConfig.PersonalCenterComponentConfig.ACTION_GET_PERSONAL_CENTER_FRAGMENT:
                CC.sendCCResult(cc.getCallId(), CCResult.success(ComponentConfig.PersonalCenterComponentConfig.ACTION_GET_PERSONAL_CENTER_FRAGMENT, PersonalCenterFragment.newInstance()));
                break;
        }
        return false;
    }
}
