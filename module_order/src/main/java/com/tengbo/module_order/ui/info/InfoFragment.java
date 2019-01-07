package com.tengbo.module_order.ui.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.adyl.pushlibrary.MessageManager;
import com.adyl.pushlibrary.PushCallBack;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.MessageAdapter;
import com.tengbo.module_order.bean.Message;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.net.ApiOrder;

import java.util.ArrayList;
import java.util.List;

import utils.RequestUtils;

public class InfoFragment extends BaseFragment {

    private RecyclerView mRvInfo;
    //    private List<Message> infoList = new ArrayList<>();
    private MessageAdapter mAdapter;
    private SwipeRefreshLayout srl;
    private BaseListResponse<Message> mListResponse;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    protected void initView() {
        srl = mRootView.findViewById(R.id.srl);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo(1);
            }
        });
        mRvInfo = mRootView.findViewById(R.id.rv_info);
        mRvInfo.setLayoutManager(new LinearLayoutManager(_mActivity));

        mAdapter = new MessageAdapter(new ArrayList<>());
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.order_layout_empty, null);
        mAdapter.setEmptyView(emptyView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                TakeStepPictureActivity.open(_mActivity,"TY87745211111","装货");
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mListResponse != null) {
                    if (mListResponse.isEnd()) {
                        mAdapter.loadMoreEnd();
                    } else {
                        getInfo(mListResponse.getPage() + 1);
                    }
                }
            }
        }, mRvInfo);
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
        getInfo(1);
    }

    private void initPush() {
        MessageManager.getInstance().register(getClass().getName(), new PushCallBack() {
            @Override
            public void onMessageReceive(String message) {
                super.onMessageReceive(message);
                Message info = new Message();
                info.setContent(message);
                message.indexOf("");
                mRvInfo.scrollToPosition(0);
                mAdapter.addData(0, info);
            }

            @Override
            public void onNotificationClick() {
                super.onNotificationClick();
            }

            @Override
            public void onNotificationReceive() {
                super.onNotificationReceive();
            }
        });
    }


    private void getInfo(int page) {
        startProgress();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", page);
        mSubscriptionManager.add(NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getMessageList(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<BaseListResponse<Message>>() {
                    @Override
                    protected void on_next(BaseListResponse<Message> response) {
                        stopProgress();
                        mListResponse = response;
                        if (mListResponse.getPage() == 1) {
                            mAdapter.setNewData(mListResponse.getRows());
                        } else {
                            mAdapter.loadMoreComplete();
                            mAdapter.addData(mListResponse.getRows());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        stopProgress();
                    }
                })

        );
    }

    public void startProgress() {
        if (!srl.isRefreshing()) {
            srl.setRefreshing(true);
        }
    }

    public void stopProgress() {
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegister(getClass().getName());
    }
}
