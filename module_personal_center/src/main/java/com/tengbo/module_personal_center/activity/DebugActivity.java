package com.tengbo.module_personal_center.activity;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.fragment.PersonalCenterFragment;

public class DebugActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_debug;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, PersonalCenterFragment.newInstance()).commit();
    }
}
