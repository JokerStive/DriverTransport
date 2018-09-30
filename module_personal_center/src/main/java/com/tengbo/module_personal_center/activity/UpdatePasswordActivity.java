package com.tengbo.module_personal_center.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.utils.ToastUtils;

import utils.RequestUtils;
import com.tengbo.commonlibrary.widget.TitleBar;

/**
 * author WangChenchen
 * 修改密码页面
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {


    // 旧密码输入框
    EditText etOldPassword;
    // 新密码输入框
    EditText etNewPassword;
    // 新密码再次输入框
    EditText etReNewPassword;
    // 验证码输入框
    EditText etValidCode;
    // 验证码视图
    ImageView ivValidCode;

    TitleBar titleBar;

    // 验证码生成工具类对象
    private CodeGeneUtils mCodeGeneUtils;


    @Override
    protected void initView() {

        titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etReNewPassword = findViewById(R.id.et_re_new_password);
        etValidCode = findViewById(R.id.et_valid_code);
        ivValidCode = findViewById(R.id.iv_valid_code);


        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.tv_refresh_valid_code).setOnClickListener(this);
        findViewById(R.id.iv_valid_code).setOnClickListener(this);


        // 获取验证码生成工具类对象
        mCodeGeneUtils = CodeGeneUtils.getInstance();
        // 初始化验证码
        ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
    }

    /**
     * 获取输入框文本，并去除两端空格
     *
     * @return
     */
    private String getText(EditText editText) {
        return editText.getText().toString().trim();
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
     * 处理按钮点击事件
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            // 处理提交
            // 获取各个输入框的值
            String oldPassword = getText(etOldPassword);
            String newPassword = getText(etNewPassword);
            String reNewPassword = getText(etReNewPassword);
            String inputValidCode = getText(etValidCode).toLowerCase();
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
            } else if (!newPassword.equals(reNewPassword)) {
                showToast("两次输入的新密码不一致");
                return;
            } else if (TextUtils.equals(newPassword, oldPassword)) {
                showToast("新密码不能和旧密码一致");
                return;
            } else if (TextUtils.isEmpty(inputValidCode)) {
                showToast("请先输入验证码");
                return;
            } else if (!inputValidCode.toLowerCase().equals(validCode)) {
                etValidCode.setText(null);
                refreshValidCode();
                showToast("验证码错误");
                return;
            }

            // 网络校验旧密码、修改密码
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oldPassword", oldPassword);
            jsonObject.put("newPassword", newPassword);
            jsonObject.put("accountId", User.getId());
            mSubscriptionManager.add(NetHelper.getInstance().getApi()
                    .updatePassword(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                    .compose(RxUtils.applySchedule())
                    .compose(RxUtils.handleResult())
                    .subscribe(new ProgressSubscriber<Object>() {
                        @Override
                        protected void on_next(Object o) {
                            ToastUtils.show(getApplicationContext(), "密码修改成功");
                            etOldPassword.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 300);

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof ApiException) {
                                ToastUtils.show(BaseApplication.get(), ((ApiException) e).getErrorMessage());
                                clearInput();
                            } else {
                                ToastUtils.show(BaseApplication.get(), e.getMessage());
                            }
                        }
                    }));

        }
        // 点击取消
        else if (id == R.id.btn_cancel) {
            finish();
        } else if (id == R.id.tv_refresh_valid_code || id == R.id.iv_valid_code) {
            refreshValidCode();
        }
    }

    private void clearInput() {
        refreshValidCode();
        etOldPassword.setText(null);
        etReNewPassword.setText(null);
        etNewPassword.setText(null);
    }

    /**
     * 刷新验证码
     */
    private void refreshValidCode() {
        etValidCode.setText("");
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
