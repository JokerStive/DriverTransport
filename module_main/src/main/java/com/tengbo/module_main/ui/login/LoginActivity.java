package com.tengbo.module_main.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.Account;
import com.tengbo.commonlibrary.commonBean.Token;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.LogInterceptor;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_main.R;
import com.tengbo.commonlibrary.commonBean.LoginInfo;
import com.tengbo.module_main.ui.home.MainActivity;


import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import rx.Subscription;
import utils.RequestUtils;
import utils.ToastUtils;


/**
 * @author yk_de
 * @Description 1、app启动后加载该页面intent
 * 2、退出登录启动该页面，清空所有用户信息
 * 3、refreshToken失效后，清空双token
 */
public class LoginActivity extends BaseActivity {
    EditText etUsername;
    EditText etPassword;
    EditText etVerification;
    ImageView ivCreateVerification;
    private boolean isGoMainActivity;


    /**
     * @param savedInstanceState 、
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(User.getAccessToken()) && !TextUtils.isEmpty(User.getRefreshToken())) {
            goMainActivity();
        } else {
            isGoMainActivity = TextUtils.isEmpty(User.getRefreshToken());
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


        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setBackground(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setPressedBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue_deep))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 20))
                .create()
        );
        refreshVerification();

    }

    public void login(View v) {
        login();
    }


    public void refreshVerificationCode(View v) {
        refreshVerification();
    }


    /**
     *
     */
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
        if (!TextUtils.equals(verification.toLowerCase(), realVerification)) {
            ToastUtils.show(this, "验证码错误");
            refreshVerification();
            return;
        }

        //获取激光注册id，可以指定推送到设备
        String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
        LogUtil.d(registrationID);

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("registrationId", registrationID);
//        String url = "http://192.168.1.22:80/api/auth/login";
        Subscription subscription = NetHelper.getInstance().getApi()
                .login(RequestUtils.createRequestBody(map))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<LoginInfo>(this) {
                    @Override
                    protected void on_next(LoginInfo loginInfo) {
                        saveData(loginInfo);

                    }

                    @Override
                    protected void on_error(ApiException e) {
                        super.on_error(e);
                        refreshVerification();
                    }
                });
        mSubscriptionManager.add(subscription);
    }


    private void saveData(LoginInfo loginInfo) {
        Token token = loginInfo.getToken();
        Account user = loginInfo.getUser();

        if (token != null) {
            LogUtil.d(token.getAccessToken() + "-------" + token.getRefreshToken());
            User.saveAccessToken(token.getAccessToken());
            User.saveRefreshToken(token.getRefreshToken());
        }

        if (user != null) {
            User.putAvatarPath(user.getUserAvatar());
            User.putAccount(user);
        }

        if (isGoMainActivity) {
            goMainActivity();
        } else {
            finish();
        }
    }

    private void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void refreshVerification() {
        etVerification.setText(null);
        ivCreateVerification.setImageBitmap(CodeGeneUtils.getInstance().createBitmap());
    }


}
