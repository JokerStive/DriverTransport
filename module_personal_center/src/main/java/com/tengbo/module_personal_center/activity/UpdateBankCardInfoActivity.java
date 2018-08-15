package com.tengbo.module_personal_center.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.commonBean.BankCardInfo;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.R2;
import com.tengbo.module_personal_center.custom.view.NoEmojiEditText;
import com.tengbo.module_personal_center.utils.BankCardValidUtils;
import com.tengbo.module_personal_center.utils.Constant;
import com.tengbo.module_personal_center.utils.DialogUtils;
import com.tengbo.module_personal_center.utils.IdCardValidUtils;
import com.tengbo.module_personal_center.utils.ResponseCode;
import com.tengbo.module_personal_center.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class UpdateBankCardInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R2.id.srl)
    SwipeRefreshLayout srl;

    @BindView(R2.id.tv_origin_bank_card_info)
    TextView tv_origin_bank_card_info;
    @BindView(R2.id.rl_bank_card_info)
    RelativeLayout rl_bank_card_info;

    @BindView(R2.id.tv_bank_name)
    TextView tv_bank_name;
    @BindView(R2.id.tv_account_name)
    TextView tv_account_name;
    @BindView(R2.id.tv_bank_card_num)
    TextView tv_bank_card_num;

    @BindView(R2.id.neet_password)
    NoEmojiEditText neet_password;
    @BindView(R2.id.neet_id_card_num)
    NoEmojiEditText neet_id_card_num;
    @BindView(R2.id.neet_bank_of_account)
    NoEmojiEditText neet_bank_of_account;
    @BindView(R2.id.neet_name_of_account)
    NoEmojiEditText neet_name_of_account;
    @BindView(R2.id.neet_bank_card_num)
    NoEmojiEditText neet_bank_card_num;
    @BindView(R2.id.neet_valid_code)
    NoEmojiEditText neet_valid_code;
    @BindView(R2.id.iv_valid_code)
    ImageView iv_valid_code;

    CodeGeneUtils mCodeGeneUtils;
    String username = "";
    String oldBankCardNum = "";

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
            getOriginBankCardInfo(new Subscriber<BankCardInfo>() {

                @Override
                public void onNext(BankCardInfo bankCardInfo) {
                    srl.setRefreshing(false);
                    srl.setEnabled(false);
                    username = bankCardInfo.fuserName;
                    if (!TextUtils.isEmpty(bankCardInfo.fcardCode)) {
                        tv_origin_bank_card_info.setVisibility(View.VISIBLE);
                        rl_bank_card_info.setVisibility(View.VISIBLE);
                        oldBankCardNum = bankCardInfo.fcardCode;
                        // 存在原银行卡信息
                        tv_account_name.setText(username);
                        tv_bank_name.setText(BankCardValidUtils.getDetailNameOfBank(oldBankCardNum));
                        StringBuilder stringBuilder = new StringBuilder();
                        int oldBankCardNumLegnth = oldBankCardNum.length();
                        for (int i = 0; i < oldBankCardNumLegnth; i += 4) {
                            if (i + 4 < oldBankCardNumLegnth)
                                stringBuilder.append(oldBankCardNum.substring(i, i + 4));
                            else
                                stringBuilder.append(oldBankCardNum.substring(i, oldBankCardNumLegnth));
                            stringBuilder.append(" ");
                        }
                        tv_bank_card_num.setText("银行卡卡号 : " + stringBuilder.toString());
                    } else {
                        // 不存在原银行卡信息
                        // 隐藏原银行卡信息视图
                        tv_origin_bank_card_info.setVisibility(View.GONE);
                        rl_bank_card_info.setVisibility(View.GONE);
                    }

                    Log.e("gg", "ppp " + bankCardInfo.toString());
                }

                @Override
                public void onError(Throwable e) {
                    String message = e.getMessage();
                    String totalMsg = e.toString();
                    if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                            || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                        showDialog("获取数据失败\n下拉重新获取", false);
                        tv_origin_bank_card_info.setVisibility(View.GONE);
                        rl_bank_card_info.setVisibility(View.GONE);
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
        mCodeGeneUtils = CodeGeneUtils.getInstance();
        // 初始化验证码
        iv_valid_code.setImageBitmap(mCodeGeneUtils.createBitmap());
        // TODO 获取原银行卡信息
        getOriginBankCardInfo(new ProgressSubscriber<BankCardInfo>(this) {

                    @Override
                    protected void on_next(BankCardInfo bankCardInfo) {
                        username = bankCardInfo.fuserName;
                        if (!TextUtils.isEmpty(bankCardInfo.fcardCode)) {
                            tv_origin_bank_card_info.setVisibility(View.VISIBLE);
                            rl_bank_card_info.setVisibility(View.VISIBLE);
                            oldBankCardNum = bankCardInfo.fcardCode;
                            // 存在原银行卡信息
                            tv_account_name.setText(username);
                            tv_bank_name.setText(BankCardValidUtils.getDetailNameOfBank(oldBankCardNum));
                            StringBuilder stringBuilder = new StringBuilder();
                            int oldBankCardNumLegnth = oldBankCardNum.length();
                            for (int i = 0; i < oldBankCardNumLegnth; i += 4) {
                                if (i + 4 < oldBankCardNumLegnth)
                                    stringBuilder.append(oldBankCardNum.substring(i, i + 4));
                                else
                                    stringBuilder.append(oldBankCardNum.substring(i, oldBankCardNumLegnth));
                                stringBuilder.append(" ");
                            }
                            tv_bank_card_num.setText("银行卡卡号 : " + stringBuilder.toString());
                        } else {
                            // 不存在原银行卡信息
                            // 隐藏原银行卡信息视图
                            tv_origin_bank_card_info.setVisibility(View.GONE);
                            rl_bank_card_info.setVisibility(View.GONE);
                        }

                        Log.e("gg", "ppp " + bankCardInfo.toString());
                    }

                    @Override
                    protected void on_error(ApiException e) {
                        super.on_error(e);
                        int errorCode = e.getErrorCode();
                        if (errorCode == ResponseCode.SYSTEM_ERROR) {
                            showDialog("获取数据失败\n下拉重新获取", false);
                            tv_origin_bank_card_info.setVisibility(View.GONE);
                            rl_bank_card_info.setVisibility(View.GONE);
                        }
                        String errorMessage = e.getErrorMessage();
                        // TODO 处理具体错误
                        Log.e("gg", "he " + errorCode);
                        Log.e("gg", "he " + errorMessage);
                    }

                    @Override
                    protected void on_net_error(Throwable e) {
                        String message = e.getMessage();
                        String totalMsg = e.toString();
                        if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                            showDialog("获取数据失败\n下拉重新获取", false);
                            tv_origin_bank_card_info.setVisibility(View.GONE);
                            rl_bank_card_info.setVisibility(View.GONE);
                        }
                        Log.e("gg", "hahah " + e.getMessage());
                        Log.e("gg", "hahah " + e.getLocalizedMessage());
                        Log.e("gg", "hahah " + e.toString());
                    }
                });


        // 根据输入银行卡号自动填写开户行
        neet_bank_card_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cardNum = neet_bank_card_num.getText().toString().trim();
                int cardNumLength = cardNum.length();
                if (cardNumLength <= 5) {
                    neet_bank_of_account.setText("");
                } else if (cardNumLength == 6) {
                    String nameOfBank = BankCardValidUtils.getNameOfBank(cardNum);
                    neet_bank_of_account.setText(nameOfBank);
                } else if (cardNumLength == 8) {
                    String nameOfBank = BankCardValidUtils.getNameOfBank(cardNum);
                    if (!TextUtils.isEmpty(nameOfBank)) {
                        neet_bank_of_account.setText(nameOfBank);
                    }
                } else if (cardNumLength > 15) {
                    String nameOfBank = BankCardValidUtils.getDetailNameOfBank(cardNum);
                    if (!TextUtils.isEmpty(nameOfBank)) {
                        neet_bank_of_account.setText(nameOfBank);
                    }
                }
            }
        });
    }

    private void showDialog(String msg, boolean isSuccess)
    {
        int imgId = R.mipmap.right;
        if(!isSuccess)
            imgId = R.mipmap.wrong;
        DialogUtils.show(this, imgId, msg);
        srl.setEnabled(true);
        srl.setRefreshing(false);
    }

    private void getOriginBankCardInfo(Subscriber subscriber)
    {
        // 获取原银行卡信息
        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                .getBankCardInfo(Constant.faccountId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(subscriber));
    }

    private String getText(NoEmojiEditText neet) {
        return neet.getText().toString().trim();
    }

    private void showToast(String text) {
        ToastUtils.show(this, text);
    }

    /**
     * 处理点击事件
     *
     * @param view
     */
    @OnClick({R2.id.btn_submit, R2.id.btn_cancel, R2.id.iv_valid_code, R2.id.tv_refresh_valid_code, R2.id.tv_back})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_back) {
            // 关闭本页面
            finish();
        } else if (id == R.id.btn_submit) {
            // 处理提交
            // 获取各个输入框的值
            String password = getText(neet_password);
            String idCardNum = getText(neet_id_card_num);
            String nameOfAccount = getText(neet_name_of_account);
            String bankCardNum = getText(neet_bank_card_num);
            String inputValidCode = getText(neet_valid_code).toLowerCase();
            String validCode = mCodeGeneUtils.getCode().toLowerCase();

            // 判断各个输入框是否为空
            if (TextUtils.isEmpty(password)) {
                showToast("请先输入密码");
                return;
            } else if (TextUtils.isEmpty(idCardNum)) {
                showToast("请先输入身份证号");
                return;
            } else if (TextUtils.isEmpty(nameOfAccount)) {
                showToast("请先输入开户人姓名");
                return;
            } else if (TextUtils.isEmpty(bankCardNum)) {
                showToast("请先输入银行卡号");
                return;
            } else if (TextUtils.isEmpty(inputValidCode)) {
                showToast("请先输入验证码");
                return;
            }

            // 判断密码位数
            if (password.length() < 6) {
                showToast("密码错误，登录密码最少6位");
                return;
            }

            // 判断身份证是否合法
            if (idCardNum.length() != 15 && idCardNum.length() != 18) {
                showToast("身份证号码不正确");
                return;
            }
            if (!IdCardValidUtils.isIDCard(idCardNum)) {
                showToast("身份证号码不正确");
                return;
            }

            // 判断开户人姓名是否是登录用户
            if (!nameOfAccount.equals(username)) {
                showToast("开户人姓名必须和用户名相同");
                return;
            }

            // 判断银行卡号是否和原卡号相同
            if (bankCardNum.equals(oldBankCardNum)) {
                showToast("新的银行卡号和原卡号相同");
                return;
            }

            // 判断银行卡位数
            if (bankCardNum.length() < 15) {
                showToast("银行卡号不正确");
                return;
            } else if (!BankCardValidUtils.checkBankCard(bankCardNum)) {
                showToast("银行卡号不正确");
                return;
            }

            // 判断验证码是否正确
            if (!inputValidCode.equals(validCode)) {
                showToast("验证码错误");
                return;
            }

            // 网络校验密码、身份证号、修改银行卡信息
            Map<String, String> map = new HashMap<>();
            map.put("floginPwd", password);
            map.put("fidNumber", idCardNum);
            map.put("fuserName", nameOfAccount);
            map.put("fcardCode", bankCardNum);
            mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                    .updateBankCardInfo(Constant.faccountId, map)
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<BaseResponse>(this) {

                        @Override
                        protected void on_next(BaseResponse baseResponse) {
                            Log.e("gg", "pwd " + baseResponse.getCode());
                            Log.e("gg", "pwd " + baseResponse.getMessage());
                            Log.e("gg", "pwd " + baseResponse.getData());
                            int code = baseResponse.getCode();
                            switch (code) {
                                case ResponseCode.STATUS_OK: // 请求成功
                                    showDialog("银行卡信息修改成功\n请注意后续付款情况", true);
                                    oldBankCardNum = bankCardNum;
                                    tv_origin_bank_card_info.setVisibility(View.VISIBLE);
                                    rl_bank_card_info.setVisibility(View.VISIBLE);
                                    tv_account_name.setText(username);
                                    tv_bank_name.setText(BankCardValidUtils.getDetailNameOfBank(oldBankCardNum));
                                    StringBuilder stringBuilder = new StringBuilder();
                                    int oldBankCardNumLegnth = oldBankCardNum.length();
                                    for (int i = 0; i < oldBankCardNumLegnth; i += 4) {
                                        if (i + 4 < oldBankCardNumLegnth)
                                            stringBuilder.append(oldBankCardNum.substring(i, i + 4));
                                        else
                                            stringBuilder.append(oldBankCardNum.substring(i, oldBankCardNumLegnth));
                                        stringBuilder.append(" ");
                                    }
                                    tv_bank_card_num.setText("银行卡卡号 : " + stringBuilder.toString());
                                    break;

                                case ResponseCode.USER_NOT_EXIST: // 用户名不存在
                                    showDialog("账户不存在", false);
                                    break;

                                case ResponseCode.PASSWORD_ERROR: // 密码错误
                                    showDialog("密码错误", false);
                                    break;

                                case ResponseCode.SYSTEM_ERROR: // 系统错误
                                    showDialog("系统错误，请稍候再试", false);
                                    break;

                                case ResponseCode.USER_ID_CARD_NUM_ERROR:// 开户人同用户的身份证号不一致
                                    showDialog("开户人同用户的身份证号不一致", false);
                                    break;
                            }
                            refreshValidCode();
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
                                showDialog("银行卡信息更改失败\n请稍候再试", false);
                            }
                            Log.e("gg", "hahah " + e.getMessage());
                            Log.e("gg", "hahah " + e.getLocalizedMessage());
                            Log.e("gg", "hahah " + e.toString());
                        }
                    }));

        } else if (id == R.id.btn_cancel) {
            // 处理取消，关闭本页面
            finish();
        } else if (id == R.id.iv_valid_code || id == R.id.tv_refresh_valid_code) {
            // 处理刷新验证码
            refreshValidCode();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_bank_card_info;
    }

    private void refreshValidCode() {
        neet_valid_code.setText("");
        iv_valid_code.setImageBitmap(mCodeGeneUtils.createBitmap());
    }
}
