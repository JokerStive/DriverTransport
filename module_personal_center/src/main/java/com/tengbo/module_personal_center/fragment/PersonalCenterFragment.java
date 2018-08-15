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
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.takePhoto.TakePhotoDialogFragment;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.R2;
import com.tengbo.module_personal_center.activity.UpdateBankCardInfoActivity;
import com.tengbo.module_personal_center.activity.UpdatePasswordActivity;
import com.tengbo.module_personal_center.utils.Constant;
import com.tengbo.module_personal_center.utils.DialogUtils;
import com.tengbo.module_personal_center.utils.ResponseCode;
import com.tengbo.module_personal_center.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

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
            getPersonalInfo(new Subscriber<PersonInfo>() {

                @Override
                public void onNext(PersonInfo personInfo) {
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

                    Log.e("avatar", "avatar url " + Config.FILE_BASE_URL + personInfo.fuserAvatar);
                    if (!TextUtils.isEmpty(personInfo.fuserAvatar))
                        Glide.with(PersonalCenterFragment.this).load(Config.FILE_BASE_URL + personInfo.fuserAvatar)
                                .into(civ_avatar);

                    Log.e("gg", "ppp " + personInfo.toString());
                }

                @Override
                public void onError(Throwable e) {
                    String message = e.getMessage();
                    String totalMsg = e.toString();
                    if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                            || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                        showDialog("获取数据失败\n下拉重新获取", false);
                    }
                    Log.e("gg", "hahah " + e.getMessage());
                    Log.e("gg", "hahah " + e.getLocalizedMessage());
                    Log.e("gg", "hahah " + e.toString());
                }

                @Override
                public void onCompleted() {

                }
            });
        });

        // 获取个人信息
        getPersonalInfo(new ProgressSubscriber<PersonInfo>(getActivity()) {

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

                Log.e("avatar", "avatar url " + Config.FILE_BASE_URL + personInfo.fuserAvatar);
                if (!TextUtils.isEmpty(personInfo.fuserAvatar))
                    Glide.with(PersonalCenterFragment.this).load(Config.FILE_BASE_URL + personInfo.fuserAvatar)
                            .into(civ_avatar);

                Log.e("gg", "ppp " + personInfo.toString());
            }

            @Override
            protected void on_error(ApiException e) {
                super.on_error(e);
                int errorCode = e.getErrorCode();
                if (errorCode == ResponseCode.SYSTEM_ERROR)
                    showDialog("获取数据失败\n下拉重新获取", false);
                String errorMessage = e.getErrorMessage();
                // TODO 处理具体错误
                Log.e("gg", "per " + errorCode);
                Log.e("gg", "per " + errorMessage);
            }

            @Override
            protected void on_net_error(Throwable e) {
                String message = e.getMessage();
                String totalMsg = e.toString();
                if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                        || totalMsg.contains("ConnectException")) {
                    showDialog("获取数据失败\n下拉重新获取", false);
                }
                Log.e("gg", "hahah " + e.getMessage());
                Log.e("gg", "hahah " + e.getLocalizedMessage());
                Log.e("gg", "hahah " + e.toString());
            }
        });

    }

    private void showDialog(String msg, boolean isSuccess) {
        int imgId = R.mipmap.right;
        if (!isSuccess)
            imgId = R.mipmap.wrong;
        DialogUtils.show(getActivity(), imgId, msg);
        srl.setEnabled(true);
        srl.setRefreshing(false);
    }

    private void getPersonalInfo(Subscriber subscriber) {
        // 请求个人信息
        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                .getPersonalInfo(Constant.faccountId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(subscriber));
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
            TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(1, false);
            takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
                @Override
                public void onSuccess(List<File> files) {
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                    File file = files.get(0);
                    Log.e("avatar", file.getAbsolutePath());
                    if (file != null && !TextUtils.isEmpty(file.getAbsolutePath())) {
                        Log.e("avatar", "if");
                        // 上传
                        Map<String, RequestBody> params = new HashMap<>();
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        params.put("file\";filename=\"" + file.getName(), requestFile);
                        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.IMAGE_BASE_URL)
                                .uploadAvatar(Constant.faccountId, params)
                                .compose(RxUtils.applySchedule())
                                .subscribe(new ProgressSubscriber<BaseResponse>(getActivity()) {

                                    @Override
                                    protected void on_next(BaseResponse baseResponse) {
                                        Log.e("gg", "pwd " + baseResponse.getCode());
                                        Log.e("gg", "pwd " + baseResponse.getMessage());
                                        Log.e("gg", "pwd " + baseResponse.getData());
                                        int code = baseResponse.getCode();
                                        switch (code) {
                                            case ResponseCode.STATUS_OK: // 请求成功
                                                Log.e("avatar", baseResponse.getData().toString());
                                                String str = baseResponse.getData().toString();
                                                String[] split = str.split("=");
                                                String addr = split[1];
                                                String address = addr.substring(0, addr.length() - 1);
                                                Log.e("avatar", "address = " + address);
                                                showToast("头像上传成功");
                                                // 显示头像
                                                Glide.with(PersonalCenterFragment.this).load(Config.FILE_BASE_URL + address)
                                                        .into(civ_avatar);
                                                // 上传头像地址
                                                uploadAvatarAddress(address);
                                                break;

                                            case ResponseCode.USER_NOT_EXIST: // 用户名不存在
                                                showDialog("用户名不存在", false);
                                                break;

                                            case ResponseCode.USER_BASIC_INFO_ERROR: // 账号被禁用
                                                showDialog("账号被禁用", false);
                                                break;

                                            case ResponseCode.SYSTEM_ERROR: // 系统错误
                                                showDialog("系统错误，请稍候再试", false);
                                                break;
                                        }
                                    }

                                    @Override
                                    protected void on_error(ApiException e) {
                                        int errorCode = e.getErrorCode();
                                        String errorMessage = e.getErrorMessage();
                                        // TODO 处理具体错误
                                        Log.e("gg", "eee " + errorCode);
                                        Log.e("gg", "eee " + errorMessage);
                                    }

                                    @Override
                                    protected void on_net_error(Throwable e) {
                                        String message = e.getMessage();
                                        String totalMsg = e.toString();
                                        if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                                || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                                            showDialog("头像上传失败\n请稍候再试", false);
                                        }
                                        Log.e("gg", "hahaha " + e.getMessage());
                                        Log.e("gg", "hahaha " + e.getLocalizedMessage());
                                        Log.e("gg", "hahaha " + e.toString());
                                    }
                                }));
                    }
                }

                @Override
                public void onError() {
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                }
            });
            takePhotoDialogFragment.show(getFragmentManager(), "avatar");
        } else if (id == R.id.tv_update_bank_card_info) {
            // 跳转到银行卡信息更改页面
            startActivity(new Intent(getActivity(), UpdateBankCardInfoActivity.class));
        } else if (id == R.id.tv_update_password) {
            // 跳转到密码修改页面
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        } else if (id == R.id.tv_logout) {
            // TODO 处理退出登录
            DialogUtils.show(getActivity(), "提示", "确定退出登录?", () -> {
                        User.saveToken("", "");
                        User.saveAutoLogin(false);
                        getActivity().finish();
                        // 跳转到登录页
                    }
            );
        }
    }

    private void uploadAvatarAddress(String address) {

        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                .uploadAvatarAddress(Constant.faccountId, address)
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<BaseResponse>(getActivity()) {

                    @Override
                    protected void on_next(BaseResponse baseResponse) {
                        Log.e("gg", "pwd " + baseResponse.getCode());
                        Log.e("gg", "pwd " + baseResponse.getMessage());
                        Log.e("gg", "pwd " + baseResponse.getData());
                        int code = baseResponse.getCode();
                        switch (code) {
                            case ResponseCode.STATUS_OK: // 请求成功
                                Log.e("avatar", baseResponse.getData().toString());
                                showDialog("图片地址上传成功", true);
                                break;

                            case ResponseCode.SYSTEM_ERROR: // 系统错误
                                showDialog("系统错误，请稍候再试", false);
                                break;
                        }
                    }

                    @Override
                    protected void on_error(ApiException e) {
                        int errorCode = e.getErrorCode();
                        String errorMessage = e.getErrorMessage();
                        // TODO 处理具体错误
                        Log.e("gg", "eee " + errorCode);
                        Log.e("gg", "eee " + errorMessage);
                    }

                    @Override
                    protected void on_net_error(Throwable e) {
                        String message = e.getMessage();
                        String totalMsg = e.toString();
                        if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                            showDialog("头像地址上传失败\n请稍候再试", false);
                        }
                        Log.e("gg", "hahaha " + e.getMessage());
                        Log.e("gg", "hahaha " + e.getLocalizedMessage());
                        Log.e("gg", "hahaha " + e.toString());
                    }
                }));
    }
}
