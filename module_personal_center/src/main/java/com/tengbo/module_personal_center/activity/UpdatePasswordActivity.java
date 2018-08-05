package com.tengbo.module_personal_center.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.commonlibrary.base.BaseMvpActivity;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.R2;
import com.tengbo.module_personal_center.custom.view.NoEmojiEditText;
import com.tengbo.module_personal_center.presenter.UpdatePasswordPresenter;
import com.tengbo.module_personal_center.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码页面
 */
public class UpdatePasswordActivity extends BaseMvpActivity implements View.OnClickListener {

    @BindView(R2.id.neet_old_password)
    NoEmojiEditText neetOldPassword;
    @BindView(R2.id.neet_new_password)
    NoEmojiEditText neetNewPassword;
    @BindView(R2.id.neet_re_new_password)
    NoEmojiEditText neetReNewPassword;
    @BindView(R2.id.neet_valid_code)
    NoEmojiEditText neetValidCode;
    @BindView(R2.id.iv_valid_code)
    ImageView ivValidCode;
    @BindView(R2.id.tv_refresh_valid_code)
    TextView tvRefreshValidCode;

    private CodeGeneUtils mCodeGeneUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_password);
        super.onCreate(savedInstanceState);
        initPresent();
    }

    @Override
    protected void initPresent() {
        mPresent = new UpdatePasswordPresenter();
        mPresent.bindView(this);
    }


    /**
     * 初始化视图，在BaseMvpActivity中
     */
    @Override
    protected void initView() {
        mCodeGeneUtils = CodeGeneUtils.getInstance();
//        neetNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 初始化验证码
        ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
        SpannableString ss = new SpannableString("刷新");
        ss.setSpan(new UnderlineSpan(), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRefreshValidCode.setText(ss);
    }

    private String getText(NoEmojiEditText neet) {
        return neet.getText().toString().trim();
    }

    private void showToast(String text) {
        ToastUtils.show(this, text);
    }

    /**
     * 处理按钮点击事件
     */
    @OnClick({R2.id.btn_submit, R2.id.btn_cancel, R2.id.iv_valid_code, R2.id.tv_refresh_valid_code})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            // 处理提交
            // 获取各个输入框的值
            String oldPassword = getText(neetOldPassword);
            String newPassword = getText(neetNewPassword);
            String reNewPassword = getText(neetReNewPassword);
            String inputValidCode = getText(neetValidCode);
            String validCode = mCodeGeneUtils.getCode();
            // 判断各个输入框是否为空
            if (TextUtils.isEmpty(oldPassword)) {
                showToast("请先输入旧密码");
                return;
            } else if (TextUtils.isEmpty(newPassword)) {
                showToast("请先输入新密码");
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
            if(oldPassword.equals(newPassword))
            {
                showToast("新密码不能和旧密码相同");
                return;
            }

            // 判断验证码是否正确
            if (!inputValidCode.equals(validCode)) {
                showToast("验证码错误");
                return;
            }

            // 网络校验旧密码、修改密码


        } else if (id == R.id.btn_cancel) {
            // 处理取消，关闭本页面
            finish();
        } else if (id == R.id.iv_valid_code || id == R.id.tv_refresh_valid_code) {
            // 处理刷新验证码
            ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }
}
