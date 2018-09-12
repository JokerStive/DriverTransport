package com.tengbo.module_order.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.InfoAdapter;
import com.tengbo.module_order.bean.Info;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends BaseFragment {

    private RecyclerView mRvInfo;
    private List<Info> infoList = new ArrayList<>();
    private InfoAdapter mAdapter;

    public static InfoFragment newInstance(){
        return new InfoFragment();
    }

    @Override
    protected void initView() {
        mRvInfo = mRootView.findViewById(R.id.rv_info);
        mRvInfo.setLayoutManager(new LinearLayoutManager(_mActivity));

        mAdapter = new InfoAdapter(infoList);
        mRvInfo.setAdapter(mAdapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getInfo();
    }

    private void getInfo() {
        for (int i = 0; i < 30; i++) {
            Info info = new Info();
            infoList.add(info);
        }
        mAdapter.replaceAll(infoList);
    }
}
