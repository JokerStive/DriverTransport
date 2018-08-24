package com.tengbo.module_personal_center.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.R2;
import com.tengbo.module_personal_center.custom.view.NoEmojiEditText;
import com.tengbo.module_personal_center.utils.Constant;
import com.tengbo.module_personal_center.utils.DialogUtils;
import com.tengbo.module_personal_center.utils.LogUtils;
import com.tengbo.module_personal_center.utils.ResponseCode;
import com.tengbo.module_personal_center.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author WangChenchen
 * 修改密码页面
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UpdatePasswordActivity";

    // 旧密码输入框
    @BindView(R2.id.neet_old_password)
    NoEmojiEditText neetOldPassword;
    // 新密码输入框
    @BindView(R2.id.neet_new_password)
    NoEmojiEditText neetNewPassword;
    // 新密码再次输入框
    @BindView(R2.id.neet_re_new_password)
    NoEmojiEditText neetReNewPassword;
    // 验证码输入框
    @BindView(R2.id.neet_valid_code)
    NoEmojiEditText neetValidCode;
    // 验证码视图
    @BindView(R2.id.iv_valid_code)
    ImageView ivValidCode;

    // 验证码生成工具类对象
    private CodeGeneUtils mCodeGeneUtils;

    /**
     * 初始化视图，在BaseActivity中
     */
    @Override
    protected void initView() {
        // 获取验证码生成工具类对象
        mCodeGeneUtils = CodeGeneUtils.getInstance();
        // 初始化验证码
        ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
    }

    /**
     * 获取输入框文本，并去除两端空格
     *
     * @param neet 输入框
     * @return
     */
    private String getText(NoEmojiEditText neet) {
        return neet.getText().toString().trim();
    }

    /**
     * 显示吐司
     *
     * @param text
     */
    private void showToast(String text) {
        ToastUtils.show(this, text);
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
        DialogUtils.show(this, imgId, msg);
    }

    /**
     * 处理按钮点击事件
     */
    @OnClick({R2.id.btn_submit, R2.id.btn_cancel, R2.id.iv_valid_code, R2.id.tv_refresh_valid_code, R2.id.tv_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        // 点击返回图标
        if (id == R.id.tv_back) {
            // 关闭本页面
            finish();
        }
        // 点击提交
        else if (id == R.id.btn_submit) {
            // 处理提交
            // 获取各个输入框的值
            String oldPassword = getText(neetOldPassword);
            String newPassword = getText(neetNewPassword);
            String reNewPassword = getText(neetReNewPassword);
            String inputValidCode = getText(neetValidCode).toLowerCase();
            String validCode = mCodeGeneUtils.getCode().toLowerCase();
            // 判断各个输入框是否为空
            if (TextUtils.isEmpty(oldPassword)) {
                showToast("请先输入旧密码");
                return;
            } else if (oldPassword.length() < 6) {
                // 判断旧密码是否小于6位
                showToast("旧密码最少6位");
                return;
            } else if (TextUtils.isEmpty(newPassword)) {
                showToast("请先输入新密码");
                return;
            } else if (newPassword.length() < 6) {
                // 判断新密码是否小于6位
                showToast("新密码最少6位");
                return;
            } else if (TextUtils.isEmpty(reNewPassword)) {
                showToast("请再次输入新密码");
                return;
            } else if (TextUtils.isEmpty(inputValidCode)) {
                showToast("请先输入验证码");
                return;
            }

            // 判断两次新密码输入是否一致
            if (!newPassword.equals(reNewPassword)) {
                showToast("两次输入的新密码不一致");
                return;
            }

            // 判断新、旧密码是否相同
            if (oldPassword.equals(newPassword)) {
                showToast("新密码不能和旧密码相同");
                return;
            }

            // 判断验证码是否正确
            if (!inputValidCode.equals(validCode)) {
                showToast("验证码错误");
                return;
            }

            // 网络校验旧密码、修改密码
            // 请求个人信息
            Map<String, String> map = new HashMap<>();
            map.put("oldFLoginPwd", oldPassword);
            map.put("newFLoginPwd", newPassword);
            mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.LOGIN_BASE_URL)
                    .updatePassword(Constant.faccountId, map)
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<BaseResponse>(this) {

                        @Override
                        protected void on_next(BaseResponse baseResponse) {
                            LogUtils.e(TAG, "updatePassword on_next " + baseResponse.getCode());
                            LogUtils.e(TAG, "updatePassword on_next " + baseResponse.getMessage());
                            LogUtils.e(TAG, "updatePassword on_next " + baseResponse.getData());
                            int code = baseResponse.getCode();
                            switch (code) {
                                case ResponseCode.STATUS_OK: // 请求成功
                                    showDialog("密码修改成功", true);
                                    break;

                                case ResponseCode.PASSWORD_ERROR: // 密码错误
                                    showDialog("密码错误", false);
                                    break;

                                case ResponseCode.SYSTEM_ERROR: // 系统错误
                                    showDialog("系统错误，请稍候再试", false);
                                    break;
                            }
                            // 刷新验证码
                            refreshValidCode();
                        }

                        @Override
                        protected void on_error(ApiException e) {
                            int errorCode = e.getErrorCode();
                            String errorMessage = e.getErrorMessage();
                            // 处理具体错误
                            LogUtils.e(TAG, "updatePassword on_error " + errorCode);
                            LogUtils.e(TAG, "updatePassword on_error " + errorMessage);
                        }

                        @Override
                        protected void on_net_error(Throwable e) {
                            String message = e.getMessage();
                            String totalMsg = e.toString();
                            if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                    || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                                showDialog("密码修改失败\n请稍候再试", false);
                            }
                            LogUtils.e(TAG, "updatePassword on_net_error " + e.getMessage());
                            LogUtils.e(TAG, "updatePassword on_net_error " + e.getLocalizedMessage());
                            LogUtils.e(TAG, "updatePassword on_net_error " + e.toString());
                        }
                    }));

        }
        // 点击取消
        else if (id == R.id.btn_cancel) {
            // 处理取消，关闭本页面
            finish();
        }
        // 点击验证码图片或者刷新
        else if (id == R.id.iv_valid_code || id == R.id.tv_refresh_valid_code) {
            // 处理刷新验证码
            refreshValidCode();
        }
    }

    /**
     * 刷新验证码
     */
    private void refreshValidCode() {
        neetValidCode.setText("");
        ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
    }

    /**
     * 获取布局Id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }
}
