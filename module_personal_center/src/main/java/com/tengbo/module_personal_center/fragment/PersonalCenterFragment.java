package com.tengbo.module_personal_center.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
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
import com.tengbo.module_personal_center.utils.LogUtils;
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
 * author WangChenchen
 * 个人中心页面
 */
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "PersonalCenterFragment";

    // 下拉刷新控件
    @BindView(R2.id.srl)
    SwipeRefreshLayout srl;

    // 圆形头像图片视图
    @BindView(R2.id.civ_avatar)
    ImageView civAvatar;
    // 用户名文本视图
    @BindView(R2.id.tv_username)
    TextView tvUsername;
    // 手机号文本视图
    @BindView(R2.id.tv_phone_num)
    TextView tvPhoneNum;

    /**
     * 创建PersonalCenterFragment对象
     *
     * @return
     */
    public static PersonalCenterFragment newInstance() {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        Bundle args = new Bundle();
        personalCenterFragment.setArguments(args);
        return personalCenterFragment;
    }

    /**
     * 初始化视图，在BaseFragment中调用
     */
    @Override
    protected void initView() {
        // 初始化SwipeRefreshLayout
        // 初始时不能下拉
        srl.setEnabled(false);
        // 设置下拉刷新控件进度条颜色
        srl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        // 下拉刷新监听器
        srl.setOnRefreshListener(() -> {
            // 获取个人信息
            getPersonalInfo(new Subscriber<PersonInfo>() {

                @Override
                public void onNext(PersonInfo personInfo) {
                    // 处理个人信息
                    handlePersonalInfo(personInfo);

                    LogUtils.e(TAG, "getPersonalInfo onNext " + personInfo.toString());
                }

                @Override
                public void onError(Throwable e) {
                    String message = e.getMessage();
                    String totalMsg = e.toString();
                    if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                            || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                        // 显示提示对话框
                        showDialog("获取数据失败\n下拉重新获取", false);
                    }
                    LogUtils.e(TAG, "getPersonalInfo onNext " + e.getMessage());
                    LogUtils.e(TAG, "getPersonalInfo onNext " + e.getLocalizedMessage());
                    LogUtils.e(TAG, "getPersonalInfo onNext " + e.toString());
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
                // 处理个人信息
                handlePersonalInfo(personInfo);

                LogUtils.e(TAG, "getPersonalInfo on_next " + personInfo.toString());
            }

            @Override
            protected void on_error(ApiException e) {
                super.on_error(e);
                int errorCode = e.getErrorCode();
                if (errorCode == ResponseCode.SYSTEM_ERROR)
                    // 显示提示对话框
                    showDialog("获取数据失败\n下拉重新获取", false);
                String errorMessage = e.getErrorMessage();
                // 处理具体错误
                LogUtils.e(TAG, "getPersonalInfo on_error " + errorCode);
                LogUtils.e(TAG, "getPersonalInfo on_error " + errorMessage);
            }

            @Override
            protected void on_net_error(Throwable e) {
                String message = e.getMessage();
                String totalMsg = e.toString();
                if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                        || totalMsg.contains("ConnectException")) {
                    showDialog("获取数据失败\n下拉重新获取", false);
                }
                LogUtils.e(TAG, "getPersonalInfo on_net_error " + e.getMessage());
                LogUtils.e(TAG, "getPersonalInfo on_net_error " + e.getLocalizedMessage());
                LogUtils.e(TAG, "getPersonalInfo on_net_error " + e.toString());
            }
        });

    }

    /**
     * 处理个人信息
     *
     * @param personInfo 个人信息
     */
    private void handlePersonalInfo(PersonInfo personInfo) {
        // 停止刷新
        srl.setRefreshing(false);
        // 设置下拉刷新控件不可用
        srl.setEnabled(false);
        // 怕暖用户名是否为空
        if (!TextUtils.isEmpty(personInfo.fuserName))
            // 设置并显示用户名
            tvUsername.setText(personInfo.fuserName);
        else
            tvUsername.setText("当前用户名不存在");

        // 判断手机号是否为空
        if (!TextUtils.isEmpty(personInfo.floginName))
            // 设置并显示手机号
            tvPhoneNum.setText(personInfo.floginName);
        else
            tvPhoneNum.setText("当前用户没有手机号码");

        /**
         * 判断头像路径是否为空
         */
        if (!TextUtils.isEmpty(personInfo.fuserAvatar))
            // 设置头像
            Glide.with(PersonalCenterFragment.this).load(Config.FILE_BASE_URL + personInfo.fuserAvatar)
                    .into(civAvatar);
    }

    /**
     * 显示提示对话框
     *
     * @param msg       对话框显示文本
     * @param isSuccess 用于选择要显示的图标，true->R.mipmap.right， false->R.mipmap.wrong
     */
    private void showDialog(String msg, boolean isSuccess) {
        // 设置要显示的图标sourceId
        int imgId = R.mipmap.right;
        if (!isSuccess)
            imgId = R.mipmap.wrong;
        DialogUtils.show(getActivity(), imgId, msg);
        // 设置下拉刷新控件可用
        srl.setEnabled(true);
        // 停止刷新
        srl.setRefreshing(false);
    }

    /**
     * 获取个人信息
     *
     * @param subscriber
     */
    private void getPersonalInfo(Subscriber subscriber) {
        // 获取个人信息
        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                .getPersonalInfo(Constant.faccountId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(subscriber));
    }

    /**
     * 获取布局Id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_center;
    }

    /**
     * 显示吐司
     *
     * @param msg
     */
    private void showToast(String msg) {
        ToastUtils.show(getActivity(), msg);
    }

    /**
     * 获取参数
     *
     * @param arguments
     */
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
        // 点击头像图片视图
        if (id == R.id.civ_avatar) {
            // 处理头像选择、上传
            // 获取拍照和选择图片对话框对象
            TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(1, false);
            takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
                @Override
                public void onSuccess(List<File> files) {
                    // 图片获取成功，关闭拍照和选择图片对话框
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                    // 获取图片文件对象
                    File file = files.get(0);
                    LogUtils.e(TAG, "select Image Success file path " + file.getAbsolutePath());
                    // 判断图片是否为空
                    if (file != null && !TextUtils.isEmpty(file.getAbsolutePath())) {
                        // 上传
                        // 构建请求体
                        Map<String, RequestBody> params = new HashMap<>();
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        params.put("file\";filename=\"" + file.getName(), requestFile);
                        // 上传头像
                        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.IMAGE_BASE_URL)
                                .uploadAvatar(Constant.faccountId, params)
                                .compose(RxUtils.applySchedule())
                                .subscribe(new ProgressSubscriber<BaseResponse>(getActivity()) {

                                    @Override
                                    protected void on_next(BaseResponse baseResponse) {
                                        LogUtils.e(TAG, "uploadAvatar on_next " + baseResponse.getCode());
                                        LogUtils.e(TAG, "uploadAvatar on_next " + baseResponse.getMessage());
                                        LogUtils.e(TAG, "uploadAvatar on_next " + baseResponse.getData());
                                        // 获取返回码
                                        int code = baseResponse.getCode();
                                        switch (code) {
                                            case ResponseCode.STATUS_OK: // 请求成功
                                                // 获取返回数据，分割字符串，获得头像在文件服务器的地址，以备上传后端
                                                String str = baseResponse.getData().toString();
                                                String[] split = str.split("=");
                                                String addr = split[1];
                                                String address = addr.substring(0, addr.length() - 1);
                                                showToast("头像上传成功");
                                                // 显示头像
                                                Glide.with(PersonalCenterFragment.this).load(Config.FILE_BASE_URL + address)
                                                        .into(civAvatar);
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
                                        // 处理具体错误
                                        LogUtils.e(TAG, "eee " + errorCode);
                                        LogUtils.e(TAG, "eee " + errorMessage);
                                    }

                                    @Override
                                    protected void on_net_error(Throwable e) {
                                        String message = e.getMessage();
                                        String totalMsg = e.toString();
                                        if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                                || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                                            // 显示提示对话框
                                            showDialog("头像上传失败\n请稍候再试", false);
                                        }
                                        LogUtils.e(TAG, "hahaha " + e.getMessage());
                                        LogUtils.e(TAG, "hahaha " + e.getLocalizedMessage());
                                        LogUtils.e(TAG, "hahaha " + e.toString());
                                    }
                                }));
                    }
                }

                @Override
                public void onError() {
                    // 关闭拍照和选择图片对话框
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                }
            });
            // 显示拍照和选择图片对话框
            takePhotoDialogFragment.show(getFragmentManager(), TAG);
        }
        // 点击银行卡信息更改
        else if (id == R.id.tv_update_bank_card_info) {
            // 跳转到银行卡信息更改页面
            startActivity(new Intent(getActivity(), UpdateBankCardInfoActivity.class));
        }
        // 点击密码修改
        else if (id == R.id.tv_update_password) {
            // 跳转到密码修改页面
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        }
        // 点击退出登录
        else if (id == R.id.tv_logout) {
            // 处理退出登录
            DialogUtils.show(getActivity(), "提示", "确定退出登录?", () -> {
                        User.saveToken("", "");
                        User.saveAutoLogin(false);
                        getActivity().finish();
                        // 跳转到登录页
                    }
            );
        }
    }

    /**
     * 上传头像在文件服务器的地址到后端
     *
     * @param address
     */
    private void uploadAvatarAddress(String address) {

        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                .uploadAvatarAddress(Constant.faccountId, address)
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<BaseResponse>(getActivity()) {

                    @Override
                    protected void on_next(BaseResponse baseResponse) {
                        LogUtils.e(TAG, "uploadAvatarAddress on_next " + baseResponse.getCode());
                        LogUtils.e(TAG, "uploadAvatarAddress on_next " + baseResponse.getMessage());
                        LogUtils.e(TAG, "uploadAvatarAddress on_next " + baseResponse.getData());
                        // 获取返回码
                        int code = baseResponse.getCode();
                        switch (code) {
                            case ResponseCode.STATUS_OK: // 请求成功
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
                        // 处理具体错误
                        LogUtils.e(TAG, "uploadAvatarAddress on_error " + errorCode);
                        LogUtils.e(TAG, "uploadAvatarAddress on_error " + errorMessage);
                    }

                    @Override
                    protected void on_net_error(Throwable e) {
                        String message = e.getMessage();
                        String totalMsg = e.toString();
                        if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                            // 显示提示对话框
                            showDialog("头像地址上传失败\n请稍候再试", false);
                        }
                        LogUtils.e(TAG, "uploadAvatarAddress on_net_error " + e.getMessage());
                        LogUtils.e(TAG, "uploadAvatarAddress on_net_error " + e.getLocalizedMessage());
                        LogUtils.e(TAG, "uploadAvatarAddress on_net_error " + e.toString());
                    }
                }));
    }
}
