package com.tengbo.module_main.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.commonlibrary.common.Event;
import com.tengbo.module_main.ui.home.MainActivity;
import com.tengbo.module_main.ui.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;


public class MainComponent implements IComponent {


    public static final String COMPONENT_NAME = "MainComponent";

    public static final String ACTION_OPEN_MAIN_ACTIVITY = "openMainActivity";

    public static final String ACTION_OPEN_LOGIN_ACTIVITY = "openLoginActivity";

    public static final String ACTION_CHANGE_TAB = "changeTab";

    public static final String PARAM_TAB_POSITION = "tabPosition";

    public static final String PARAM_IS_GOMain = "isGoMain";

    @Override
    public String getName() {
        return ComponentConfig.Main.COMPONENT_NAME;
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        if (!TextUtils.isEmpty(actionName)) {
            switch (actionName) {
                case ComponentConfig.Main.ACTION_OPEN_MAIN_ACTIVITY:
                    openActivity(cc, MainActivity.class);
                    break;

                case ComponentConfig.Main.ACTION_OPEN_LOGIN_ACTIVITY:
                    openActivity(cc, LoginActivity.class);
                    break;

                case ComponentConfig.Main.ACTION_CHANGE_TAB:
                    int changToTabIndex = cc.getParamItem(ComponentConfig.Main.PARAM_TAB_POSITION);
                    LogUtil.d("待切换的tab--" + changToTabIndex);
                    CC.sendCCResult(cc.getCallId(), CCResult.success());
                    EventBus.getDefault().post(new Event.ChangeTab(changToTabIndex));
                    break;
            }
        }
        return false;
    }


    private void openActivity(CC cc, Class clazz) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
    }

}
