package com.tengbo.module_order.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.adyl.pushlibrary.MessageManager;
import com.adyl.pushlibrary.PushCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.InfoAdapter;
import com.tengbo.module_order.bean.Message;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends BaseFragment {

    private RecyclerView mRvInfo;
    private List<Message> infoList = new ArrayList<>();
    private InfoAdapter mAdapter;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    protected void initView() {
        mRvInfo = mRootView.findViewById(R.id.rv_info);
        mRvInfo.setLayoutManager(new LinearLayoutManager(_mActivity));

        mAdapter = new InfoAdapter(infoList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                TakeStepPictureActivity.open(_mActivity,"TY87745211111","装货");
            }
        });
        mRvInfo.setAdapter(mAdapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPush();
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

    private void initPush() {
        MessageManager.getInstance().register(getClass().getName(), new PushCallBack() {
            @Override
            public void onMessageReceive(String message) {
                super.onMessageReceive(message);
                Message info = new Message();
                info.setFrom("后台推送");
                info.setTo(message);
                message.indexOf("");
                mRvInfo.scrollToPosition(0);
                mAdapter.addData(0, info);
            }

            @Override
            public void onNotificationClick() {
                super.onNotificationClick();
                Message info = new Message();
                info.setFrom("后台推送");
                info.setTo("通知被点击了");
                mRvInfo.scrollToPosition(0);
                mAdapter.addData(0, info);
            }

            @Override
            public void onNotificationReceive() {
                super.onNotificationReceive();
                Message info = new Message();
                info.setFrom("后台推送");
                info.setTo("通知来了");
                mRvInfo.scrollToPosition(0);
                mAdapter.addData(0, info);
            }
        });
    }


    private void getInfo() {
        for (int i = 0; i < 30; i++) {
            Message info = new Message();
            info.setFrom("司机小王");
            info.setTo("你的车有故障");
            infoList.add(info);
        }
        mAdapter.replaceAll(infoList);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegister(getClass().getName());
    }
}
