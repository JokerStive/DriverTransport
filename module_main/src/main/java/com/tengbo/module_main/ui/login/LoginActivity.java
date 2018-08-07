package com.tengbo.module_main.ui.login;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.Token;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_main.R;
import com.tengbo.module_main.ui.home.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.ToastUtils;

public class LoginActivity extends BaseActivity {
    EditText etUsername;
    EditText etPassword;
    EditText etVerification;
    ImageView ivCreateVerification;
    TextView tvAutoLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null && !TextUtils.isEmpty(User.getAccessToken())) {
            goMainActivity();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etVerification = findViewById(R.id.et_verification);
        ivCreateVerification = findViewById(R.id.iv_create_verification);
        tvAutoLogin = findViewById(R.id.tv_auto_login);

        refreshVerification();

    }

    public void login(View v) {
        login();
    }

    public void refreshVerificationCode(View v) {
        refreshVerification();
    }


    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String verification = etVerification.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, "请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }

        if (TextUtils.isEmpty(verification)) {
            ToastUtils.show(this, "请输入验证码");
            return;
        }

        String realVerification = CodeGeneUtils.getInstance().getCode();
        if (!TextUtils.equals(verification, realVerification)) {
            ToastUtils.show(this, "验证码错误，请重新输入");
            return;
        }
//        goMainActivity();


        NetHelper.getInstance().getApi()
                .login(username, password)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Token>(this) {
                    @Override
                    protected void on_next(Token token) {
                        User.saveToken(token.getAccessToken(), token.getRefreshToken());
                        User.saveAutoLogin(tvAutoLogin.isSelected());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });


    }

    private void goMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void refreshVerification() {
        ivCreateVerification.setImageBitmap(CodeGeneUtils.getInstance().createBitmap());
    }
}
