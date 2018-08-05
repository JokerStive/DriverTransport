package com.tengbo.module_personal_center.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.tengbo.module_personal_center.activity.UpdatePasswordActivity;

public class UpdatePasswordComponent implements IComponent {
    @Override
    public String getName() {
        // 调用端obtainBuilder所需字段
        return "UpdatePasswordComponent";
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        switch (actionName)
        {
            case "openUpdatePasswordActivity": // 调用端设置actionName
                openUpdatePasswordActivity(cc);
                break;
        }

        return false;
    }

    /**
     * 打开密码修改页面
     * @param cc
     */
    private void openUpdatePasswordActivity(CC cc){
        Context context = cc.getContext();
        Intent intent = new Intent(context, UpdatePasswordActivity.class);
        if (!(context instanceof Activity)) {
            //调用方没有设置context或app间组件跳转，context为application
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
    }
}
