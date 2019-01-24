package com.tengbo.module_personal_center.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.BankCardInfo;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.adapter.BankCardListAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import utils.CommonDialog;
import utils.RequestUtils;
import com.tengbo.commonlibrary.widget.TitleBar;

/**
 * 银行卡列表页
 *
 * @Autor yk
 * @Description
 */
public class BankCardListActivity extends BaseActivity {

    private TitleBar titleBar;
    private SwipeRefreshLayout srl;
    private RecyclerView rvBankCardList;
    private List<BankCardInfo> mBankCardInfoList = new ArrayList<>();
    private BankCardListAdapter mAdapter;
    private User user = new User();

    @Override
    protected int getLayoutId() {
        return R.layout.center_activity_bank_card_list;
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        srl = findViewById(R.id.srl);
        rvBankCardList = findViewById(R.id.rv_bank_card_list);

        getBankCardInfo();
        initListener();
        initRv();

    }


    private void initRv() {
        rvBankCardList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new BankCardListAdapter(mBankCardInfoList);
        View footer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.center_footer_bank_card_list, null);
        footer.findViewById(R.id.rl_footer).setOnClickListener(v -> openAdd());
        mAdapter.addFooterView(footer);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BankCardInfo bankCardInfo = (BankCardInfo) adapter.getItem(position);
                assert bankCardInfo != null;
                if (bankCardInfo.getIsTrade() != 1) {
                    new CommonDialog(BankCardListActivity.this)
                            .setNotice("确定把该银行卡设置为默认银行卡？")
                            .setPositiveText("确定")
                            .setNegativeText("取消")
                            .setOnPositiveClickListener(new CommonDialog.OnPositiveClickListener() {
                                @Override
                                public void onPositiveClick() {
                                    setDefaultBankCard(position, bankCardInfo);
                                }
                            })
                            .show();
                }
            }
        });
        rvBankCardList.setAdapter(mAdapter);
    }


    private void initListener() {
        titleBar.setOnBackClickListener(this::finish);
        srl.setOnRefreshListener(this::getBankCardInfo);
    }


    /**
     * 获取银行卡列表信息
     */
    private void getBankCardInfo() {
        // 获取原银行卡信息
        srl.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idNumber", user.getIdNumber());
        RequestBody requestBody = RequestUtils.createRequestBody(jsonObject.toJSONString());
        mSubscriptionManager.add(NetHelper.getInstance().getApi()
                .getBankCardInfos(requestBody)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<List<BankCardInfo>>() {
                    @Override
                    protected void on_next(List<BankCardInfo> bankCardInfos) {
                        srl.setRefreshing(false);
                        handleOriginBankCardInfo(bankCardInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        srl.setRefreshing(false);
                    }
                }));
    }


    /**
     * 设置默认银行卡
     *
     * @param position
     * @param bankCardInfo 银行卡信息
     */
    private void setDefaultBankCard(int position, BankCardInfo bankCardInfo) {
        if (bankCardInfo == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bankcardId", bankCardInfo.getBankcardId());
        jsonObject.put("cardCode", bankCardInfo.getCardCode());
        jsonObject.put("idNumber", user.getIdNumber());
        jsonObject.put("isTrade", 1);
        RequestBody requestBody = RequestUtils.createRequestBody(jsonObject.toJSONString());
        mSubscriptionManager.add(NetHelper.getInstance().getApi()
                .updateBankCardInfo(requestBody)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>(BankCardListActivity.this) {
                    @Override
                    protected void on_next(Object object) {
                        getBankCardInfo();
                    }

                }));

    }

    /**
     * 显示银行卡列表
     *
     * @param bankCardInfos 银行卡列表数据
     */
    private void handleOriginBankCardInfo(List<BankCardInfo> bankCardInfos) {
        mBankCardInfoList = setDefaultData(bankCardInfos);
        if (mBankCardInfoList.size() == 0) {
            openAdd();
        }
        mAdapter.setNewData(mBankCardInfoList);
    }

    /**
     * 把默认银行卡放置到数据的第一位
     *
     * @param bankCardInfos 银行卡列表数据
     * @return 编辑过的数据
     */
    private List<BankCardInfo> setDefaultData(List<BankCardInfo> bankCardInfos) {
        int targetPosition = -1;
        for (int i = 0; i < bankCardInfos.size(); i++) {
            if (bankCardInfos.get(i).getIsTrade() == 1) {
                targetPosition = i;

                break;
            }
        }
        if (targetPosition != -1) {
            BankCardInfo bankCardInfo = bankCardInfos.get(targetPosition);
            bankCardInfos.remove(targetPosition);
            bankCardInfos.add(0, bankCardInfo);
        }
        return bankCardInfos;
    }


    /**
     * 打开添加银行卡页面
     */
    private void openAdd() {
        startActivityForResult(new Intent(BankCardListActivity.this, AddBankCardInfoActivity.class), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            getBankCardInfo();
        }
    }
}
