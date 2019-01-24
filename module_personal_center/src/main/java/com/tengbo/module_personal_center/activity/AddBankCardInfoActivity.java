package com.tengbo.module_personal_center.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.adyl.ocr.ui.camera.CameraActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.tamic.novate.util.FileUtil;
import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.utils.BankCardValidUtils;
import com.tengbo.module_personal_center.utils.ToastUtils;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.RequestUtils;

import com.tengbo.commonlibrary.widget.TitleBar;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * author WangChenchen
 * 银行卡信息更改页面
 */
public class AddBankCardInfoActivity extends BaseActivity implements View.OnClickListener {


    // 验证码生成工具类对象
    CodeGeneUtils mCodeGeneUtils;
    TitleBar titleBar;
    EditText et_password;
    TextView tv_belong_bank;
    TextView tv_name;
    TextView tv_id_number;
    EditText et_bank_card_id;
    EditText et_valid_code;
    ImageView iv_valid_code;
    User user = new User();

    /**
     *
     */
    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        et_password = findViewById(R.id.et_password);
        tv_belong_bank = findViewById(R.id.tv_belong_bank);
        tv_name = findViewById(R.id.tv_name);
        tv_id_number = findViewById(R.id.tv_id_number);
        et_bank_card_id = findViewById(R.id.et_bank_card_id);
        et_valid_code = findViewById(R.id.et_valid_code);
        iv_valid_code = findViewById(R.id.iv_valid_code);

        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.tv_refresh_valid_code).setOnClickListener(this);
        findViewById(R.id.iv_valid_code).setOnClickListener(this);


        titleBar.setOnBackClickListener(this::finish);


        //显示姓名
        tv_name.setText(User.getName());

        //显示身份证
        tv_id_number.setText(User.getIdNumber());

        // 获取验证码生成类对象
        mCodeGeneUtils = CodeGeneUtils.getInstance();

        // 初始化验证码
        iv_valid_code.setImageBitmap(mCodeGeneUtils.createBitmap());

        // 根据输入银行卡号自动填写开户行
        et_bank_card_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cardNum = s.toString();
                String result = TextUtils.isEmpty(BankCardValidUtils.getBankNameWithId(cardNum)) ? "所属银行" : BankCardValidUtils.getBankNameWithId(cardNum);
                tv_belong_bank.setText(result);
            }
        });
    }


    /**
     * 处理点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_submit) {

            String password = et_password.getText().toString().trim();
            String newBankCardNum = et_bank_card_id.getText().toString().trim();
            String inputValidCode = et_valid_code.getText().toString().trim().toLowerCase();
            String validCode = mCodeGeneUtils.getCode().toLowerCase();

            if (TextUtils.isEmpty(newBankCardNum)) {
                showToast("请先输入银行卡号");
                return;
            } else if (newBankCardNum.length() < 15 && !BankCardValidUtils.checkBankCard(newBankCardNum)) {
                showToast("银行卡号不正确");
                return;
            } else if (TextUtils.isEmpty(tv_belong_bank.getText().toString())) {
                showToast("银行卡输入有误，请检查");
                return;
            } else if (TextUtils.isEmpty(password)) {
                showToast("请先输入密码");
                return;
            } else if (password.length() < 6) {
                showToast("密码错误，登录密码最少6位");
                return;
            } else if (TextUtils.isEmpty(inputValidCode)) {
                showToast("请先输入验证码");
                return;
            } else if (!inputValidCode.toLowerCase().equals(validCode)) {
                et_valid_code.setText(null);
                refreshValidCode();
                showToast("验证码错误");
                return;
            }

            // 网络校验密码、身份证号、修改银行卡信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idNumber", User.getIdNumber());
            jsonObject.put("cardCode", newBankCardNum);
            jsonObject.put("isTrade", 0);
            RequestBody requestBody = RequestUtils.createRequestBody(jsonObject.toJSONString());
            mSubscriptionManager.add(NetHelper.getInstance().getApi()
                    .addBankCardInfo(requestBody)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<Object>() {
                        @Override
                        protected void on_next(Object o) {
                            ToastUtils.show(getApplicationContext(), "新增银行卡成功");
                            tv_name.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 300);
                            setResult(200);

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof ApiException) {
                                ToastUtils.show(getApplicationContext(), ((ApiException) e).getErrorMessage());
                                clearInput();
                            } else {
                                ToastUtils.show(BaseApplication.get(), e.getMessage());
                            }
                        }
                    }));


        }
        // 点击取消
        else if (id == R.id.btn_cancel) {
            // 处理取消，关闭本页面
                finish();
        }
        // 点击验证码图片或者刷新
        else if (id == R.id.tv_refresh_valid_code || id == R.id.iv_valid_code) {
            // 处理刷新验证码

                refreshValidCode();
        }
    }






    /**
     * 用明文ak，sk初始化
     */
    String token;

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(getApplicationContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                token = result.getAccessToken();
                LogUtil.d("token已经获取成功");
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, getApplicationContext(), "0lWO6xHhyMNKQVgITugBMSiN", "XTjVDh5KUdpU5fk2yrkOFgxf9ZY0e9sT");
    }



    /**
     * 修改因银行卡请求后，制空所有输入框
     */
    private void clearInput() {
        refreshValidCode();
        tv_belong_bank.setText(null);
        et_bank_card_id.setText(null);
        et_password.setText(null);
    }

    private void showToast(String s) {
        ToastUtils.show(this, s);
    }

    /**
     * 获取布局Id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_bank_card_info;
    }

    /**
     * 刷新验证码
     */
    private void refreshValidCode() {
        et_valid_code.setText("");
        iv_valid_code.setImageBitmap(mCodeGeneUtils.createBitmap());
    }



}
