package com.tengbo.module_main.ui.login;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.Token;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.takePhoto.TakePhotoDialogFragment;
import com.tengbo.module_main.R;
import com.tengbo.module_main.ui.home.MainActivity;


import java.io.File;
import java.util.List;

import rx.Subscription;
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
//        goMainActivity();
        if (intent == null && !TextUtils.isEmpty(User.getAccessToken())) {
            goMainActivity();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etVerification = findViewById(R.id.et_verification);
        ivCreateVerification = findViewById(R.id.iv_create_verification);
        tvAutoLogin = findViewById(R.id.tv_auto_login);
        tvAutoLogin.setSelected(true);
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setBackground(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setPressedBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue_deep))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 20))
                .create()
        );
        refreshVerification();


        ImageView ivLogo = findViewById(R.id.iv_logo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(1, true);
                takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
                    @Override
                    public void onSuccess(List<File> files) {
                        takePhotoDialogFragment.dismiss();
                        File file = files.get(0);
                        LogUtil.d(file.getPath());
                        Glide.with(LoginActivity.this).load(file)
                                .into(ivLogo);
                    }

                    @Override
                    public void onError() {
                        takePhotoDialogFragment.dismiss();
                        ToastUtils.show(BaseApplication.get(), "加载失败");
                    }
                });

                takePhotoDialogFragment.show(getSupportFragmentManager(), "");
            }
        });

    }

    public void login(View v) {
        login();
    }


    public void isAutoLogin(View v) {
        tvAutoLogin.setSelected(!tvAutoLogin.isSelected());
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
            refreshVerification();
            return;
        }

        NetHelper.getInstance().getApi(Config.BASE_URL)
                .login(username, password)
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<Token>(this) {
                    @Override
                    protected void on_next(Token token) {
                        LogUtil.d("success");
                    }
                });

        return;
//        Subscription subscription = NetHelper.getInstance().getApi()
//                .login(username, password)
//                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new ProgressSubscriber<Token>(this) {
//                    @Override
//                    protected void on_next(Token token) {
//                        Logger.d(token.getAccessToken() + "-------" + token.getRefreshToken());
//                        User.saveToken(token.getAccessToken(), token.getRefreshToken());
//                        User.saveAutoLogin(tvAutoLogin.isSelected());
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    protected void on_error(ApiException e) {
//                        super.on_error(e);
//                        refreshVerification();
//                    }
//                });
//        mSubscriptionManager.add(subscription);
    }

    private void goMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void refreshVerification() {
        etVerification.setText(null);
        ivCreateVerification.setImageBitmap(CodeGeneUtils.getInstance().createBitmap());
    }



}
