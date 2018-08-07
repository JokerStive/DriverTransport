package com.tengbo.module_main.ui.login;

import android.os.Bundle;

import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_main.R;

public class HistoryOrderFragment extends BaseFragment {

    public static HistoryOrderFragment newInstance() {
        HistoryOrderFragment fragment = new HistoryOrderFragment();
        Bundle args = new Bundle();
//        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }
}
