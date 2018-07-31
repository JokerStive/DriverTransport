package com.tengbo.module_exapmle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.tengbo.module_exapmle.demo.DemoActivity;

public class DemoComponent implements IComponent {
    @Override
    public boolean onCall(CC cc) {

        Context context = cc.getContext();
        String actionName = cc.getActionName();
        if(TextUtils.equals(actionName,"startActivity")){
            int requestCode = cc.getParamItem("requestCode");
            if (requestCode==3){
                Intent intent = new Intent(context, DemoActivity.class);
                Log.d("yk","start");
                if (!(context instanceof Activity)) {
                    //调用方没有设置context或app间组件跳转，context为application
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
                //发送组件调用的结果（返回信息）
                CC.sendCCResult(cc.getCallId(), CCResult.success());
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "y";
    }
}
