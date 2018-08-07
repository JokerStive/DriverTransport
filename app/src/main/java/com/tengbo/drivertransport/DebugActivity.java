package com.tengbo.drivertransport;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.orhanobut.logger.Logger;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.ComponentConfig;

public class DebugActivity extends BaseActivity{

//    @BindView(R.id.btn_personal_center)
//    Button btn_personal_center;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_debug;
    }

    Fragment fragment;
    IComponentCallback fragmentCallback = (cc, result) -> {
        if (result.isSuccess()) {
            Fragment fragment = result.getDataItem(ComponentConfig.PersonalCenterComponentConfig.ACTION_GET_PERSONAL_CENTER_FRAGMENT);
            if (fragment != null) {
                showFragment(fragment);
            }
        } else {
            showResult(result);
        }
    };

    private void showResult(CCResult result) {
        Toast.makeText(CC.getApplication(), result.toString(), Toast.LENGTH_SHORT).show();
    }

    private void showFragment(Fragment fragment) {
        Log.e("snsd", ""+(fragment == null));
        if (fragment != null) {
            this.fragment = fragment;
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.fl_fragment_container, fragment);
            trans.commit();
        }
    }


    //    @OnClick({R.id.btn_personal_center,})
    public void personalCenter(View view) {
        CC.obtainBuilder(ComponentConfig.PersonalCenterComponentConfig.COMPONENT_NAME)
                .setActionName(ComponentConfig.PersonalCenterComponentConfig.ACTION_GET_PERSONAL_CENTER_FRAGMENT)
                .build()
                .callAsyncCallbackOnMainThread(fragmentCallback);

//
//        CCResult ccResult = CC.obtainBuilder(ComponentConfig.OrderComponentConfig.COMPONENT_NAME)
//                .setActionName(ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT)
//                .build()
//                .call();
//        if (ccResult.isSuccess()) {
//            Fragment f = ccResult.getDataItem(ComponentConfig.PersonalCenterComponentConfig.ACTION_GET_PERSONAL_CENTER_FRAGMENT);
//            getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, f).commit();
//        }



//        CC.obtainBuilder("UpdatePasswordComponent")
//                .setActionName("openUpdatePasswordActivity")
//                .build()
//                .call();
    }

    @Override
    protected void initView() {

    }
}
