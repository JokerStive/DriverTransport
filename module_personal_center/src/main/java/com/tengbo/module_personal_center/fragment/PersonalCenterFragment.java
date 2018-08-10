package com.tengbo.module_personal_center.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.PersonInfo;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.PersonInfoService;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.R2;
import com.tengbo.module_personal_center.activity.UpdateBankCardInfoActivity;
import com.tengbo.module_personal_center.activity.UpdatePasswordActivity;
import com.tengbo.module_personal_center.utils.ResponseCode;
import com.tengbo.module_personal_center.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心页面
 */
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R2.id.srl)
    SwipeRefreshLayout srl;

    @BindView(R2.id.civ_avatar)
    ImageView civ_avatar;
    @BindView(R2.id.tv_username)
    TextView tv_username;
    @BindView(R2.id.tv_phone_num)
    TextView tv_phone_num;

    public static PersonalCenterFragment newInstance() {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        Bundle args = new Bundle();
        personalCenterFragment.setArguments(args);
        return personalCenterFragment;
    }

    @Override
    protected void initView() {
        // 初始化SwipeRefreshLayout
        // 初始时不能下拉
        srl.setEnabled(false);
        // 进度条颜色
        srl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        // 刷新监听器
        srl.setOnRefreshListener(() -> {
            // 获取个人信息
            getPersonalInfo();
        });

        // 获取个人信息
        getPersonalInfo();

    }

    private void getPersonalInfo()
    {
        // 请求个人信息
        mSubscriptionManager.add(NetHelper.getInstance().getRetrofitBuilder(Config.BASE_URL).build().create(PersonInfoService.class)
                .getPersonalInfo(1)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<PersonInfo>(getActivity()) {

                    @Override
                    protected void on_next(PersonInfo personInfo) {
                        srl.setRefreshing(false);
                        srl.setEnabled(false);
                        if (!TextUtils.isEmpty(personInfo.fuserName))
                            tv_username.setText(personInfo.fuserName);
                        else
                            tv_username.setText("当前用户名不存在");

                        if (!TextUtils.isEmpty(personInfo.floginName))
                            tv_phone_num.setText(personInfo.floginName);
                        else
                            tv_phone_num.setText("当前用户没有手机号码");

                        if (!TextUtils.isEmpty(personInfo.fuserAvatar))
                            Glide.with(PersonalCenterFragment.this).load(personInfo.fuserAvatar)
                                    .into(civ_avatar);

                        Log.e("gg", "ppp " + personInfo.toString());
                    }

                    @Override
                    protected void on_error(ApiException e) {
                        super.on_error(e);
                        int errorCode = e.getErrorCode();
                        if (errorCode == ResponseCode.SYSTEM_ERROR) {
                            showToast("用户基本信息错误");
                            tv_username.setText("下拉重新获取");
                            srl.setEnabled(true);
                            srl.setRefreshing(false);
                        }
                        String errorMessage = e.getErrorMessage();
                        // TODO 处理具体错误
                        Log.e("gg", "per " + errorCode);
                        Log.e("gg", "per " + errorMessage);
                    }

                    @Override
                    protected void on_net_error(Throwable e) {
                        String message = e.getMessage();
                        String totalMsg = e.toString();
                        if (message.contains("404")) {
                            showToast("获取个人信息失败，请稍候再试");
                        }
                        if (totalMsg.contains("SocketTimeoutException")) {
                            showToast("请求超时，请稍候再试");
                        } else if (totalMsg.contains("ConnectException")) {
                            showToast("连接失败，请稍候再试");
                        }
                        tv_username.setText("下拉重新获取");
                        srl.setEnabled(true);
                        srl.setRefreshing(false);
                        ConnectException ea;
                        SocketTimeoutException eb;
                        Log.e("gg", "hahah " + e.getMessage());
                        Log.e("gg", "hahah " + e.getLocalizedMessage());
                        Log.e("gg", "hahah " + e.toString());
                    }
                }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_center;
    }

    private void showToast(String msg) {
        ToastUtils.show(getActivity(), msg);
    }

    @Override
    protected void getTransferData(Bundle arguments) {
    }

    /**
     * 处理点击事件
     *
     * @param view
     */
    @OnClick({R2.id.civ_avatar, R2.id.tv_update_bank_card_info, R2.id.tv_update_password, R2.id.tv_logout})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.civ_avatar) {
            // TODO 处理头像选择、上传
            showToast("头像");
        } else if (id == R.id.tv_update_bank_card_info) {
            // 跳转到银行卡信息更改页面
            startActivity(new Intent(getActivity(), UpdateBankCardInfoActivity.class));
        } else if (id == R.id.tv_update_password) {
            // 跳转到密码修改页面
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        } else if (id == R.id.tv_logout) {
            // TODO 处理退出登录
            User.saveToken("", "");
            User.saveAutoLogin(false);
            getActivity().finish();
            // 跳转道登录页
        }
    }
}
