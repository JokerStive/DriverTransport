package com.tengbo.module_personal_center.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tengbo.module_personal_center.utils.LogUtils;
import com.tengbo.module_personal_center.utils.ResponseCode;
import com.tengbo.module_personal_center.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * author WangChenchen
 * 银行卡信息更改页面
 */
public class UpdateBankCardInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UpdateBankCardInfo";

    // 下拉刷新控件
    @BindView(R2.id.srl)
    SwipeRefreshLayout srl;

    // 原银行卡信息文本视图，用于隐藏和显示控制
    @BindView(R2.id.tv_origin_bank_card_info)
    TextView tvOriginBankCardInfo;
    // 原银行卡信息布局，用于隐藏和显示控制
    @BindView(R2.id.rl_bank_card_info)
    RelativeLayout rlBankCardInfo;


    // 原银行名文本视图
    @BindView(R2.id.tv_bank_name)
    TextView tvBankName;
    // 原银行卡开户姓名文本视图
    @BindView(R2.id.tv_account_name)
    TextView tvAccountName;
    // 原银行卡号文本视图
    @BindView(R2.id.tv_bank_card_num)
    TextView tvBankCardNum;

    // 密码输入框
    @BindView(R2.id.neet_password)
    NoEmojiEditText neetPassword;
    // 身份证号输入框
    @BindView(R2.id.neet_id_card_num)
    NoEmojiEditText neetIdCardNum;
    // 开户行输入框
    @BindView(R2.id.neet_bank_of_account)
    NoEmojiEditText neetBankOfAccount;
    // 开户姓名输入框
    @BindView(R2.id.neet_name_of_account)
    NoEmojiEditText neetNameOfAccount;
    // 银行卡号输入框
    @BindView(R2.id.neet_bank_card_num)
    NoEmojiEditText neetBankCardNum;
    // 验证码输入框
    @BindView(R2.id.neet_valid_code)
    NoEmojiEditText neetValidCode;
    // 验证码图片视图
    @BindView(R2.id.iv_valid_code)
    ImageView ivValidCode;

    // 验证码生成工具类对象
    CodeGeneUtils mCodeGeneUtils;
    // 原银行卡开户名
    String username = "";
    // 原银行卡号
    String oldBankCardNum = "";

    @Override
    protected void initView() {
        // 初始化SwipeRefreshLayout
        // 初始时不能下拉
        srl.setEnabled(false);
        // 下拉刷新控件进度条颜色
        srl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        // 下拉刷新监听器
        srl.setOnRefreshListener(() -> {
            // 获取原银行卡信息
            getOriginBankCardInfo(new Subscriber<BankCardInfo>() {

                @Override
                public void onNext(BankCardInfo bankCardInfo) {
                    // 请求结束停止刷新
                    srl.setRefreshing(false);
                    // 设置下拉刷新控件不可下拉
                    srl.setEnabled(false);
                    // 处理原银行卡信息
                    handleOriginBankCardInfo(bankCardInfo);

                    LogUtils.e(TAG, "SwipeRefresh getOriginBankCardInfo onNext " + bankCardInfo.toString());
                }

                @Override
                public void onError(Throwable e) {
                    String message = e.getMessage();
                    String totalMsg = e.toString();
                    if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                            || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                        // 显示提示对话框
                        showDialog("获取数据失败\n下拉重新获取", false);
                        // 隐藏原银行卡信息布局
                        hideOriginBankCarInfoLayout();
                    }
                    LogUtils.e(TAG, "SwipeRefresh getOriginBankCardInfo onError " + e.getMessage());
                    LogUtils.e(TAG, "SwipeRefresh getOriginBankCardInfo onError " + e.getLocalizedMessage());
                    LogUtils.e(TAG, "SwipeRefresh getOriginBankCardInfo onError " + e.toString());
                }

                @Override
                public void onCompleted() {

                }
            });
        });
        // 获取验证码生成类对象
        mCodeGeneUtils = CodeGeneUtils.getInstance();
        // 初始化验证码
        ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
        // 获取原银行卡信息
        getOriginBankCardInfo(new ProgressSubscriber<BankCardInfo>(this) {

            @Override
            protected void on_next(BankCardInfo bankCardInfo) {
                // 处理原银行卡信息
                handleOriginBankCardInfo(bankCardInfo);
                LogUtils.e(TAG, "getOriginBankCardInfo on_next " + bankCardInfo.toString());
            }

            @Override
            protected void on_error(ApiException e) {
                super.on_error(e);
                int errorCode = e.getErrorCode();
                if (errorCode == ResponseCode.SYSTEM_ERROR) {
                    // 显示提示对话框
                    showDialog("获取数据失败\n下拉重新获取", false);
                    // 隐藏原银行卡信息布局
                    hideOriginBankCarInfoLayout();
                }
                String errorMessage = e.getErrorMessage();
                // 处理具体错误
                LogUtils.e(TAG, "getOriginBankCardInfo on_error " + errorCode);
                LogUtils.e(TAG, "getOriginBankCardInfo on_error " + errorMessage);
            }

            @Override
            protected void on_net_error(Throwable e) {
                String message = e.getMessage();
                String totalMsg = e.toString();
                if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                        || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                    // 显示提示对话框
                    showDialog("获取数据失败\n下拉重新获取", false);
                    // 隐藏原银行卡信息布局
                    hideOriginBankCarInfoLayout();
                }
                LogUtils.e(TAG, "getOriginBankCardInfo on_net_error " + e.getMessage());
                LogUtils.e(TAG, "getOriginBankCardInfo on_net_error " + e.getLocalizedMessage());
                LogUtils.e(TAG, "getOriginBankCardInfo on_net_error " + e.toString());
            }
        });


        // 根据输入银行卡号自动填写开户行
        neetBankCardNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 获取已经输入的文本
                String cardNum = neetBankCardNum.getText().toString().trim();
                // 获取已经输入的文本长度
                int cardNumLength = cardNum.length();
                if (cardNumLength <= 5) {
                    // 如果长度不超过5，则设置开户行输入框文本为空
                    neetBankOfAccount.setText("");
                } else if (cardNumLength == 6) {
                    // 如果长度为6，则用银行卡校验工具类获取银行名并设置到开户行输入框
                    String nameOfBank = BankCardValidUtils.getNameOfBank(cardNum);
                    if (!TextUtils.isEmpty(nameOfBank))
                        neetBankOfAccount.setText(nameOfBank);
                } else if (cardNumLength == 8) {
                    // 如果长度为8，则用银行卡校验工具类获取银行名并设置到开户行输入框
                    String nameOfBank = BankCardValidUtils.getNameOfBank(cardNum);
                    if (!TextUtils.isEmpty(nameOfBank))
                        neetBankOfAccount.setText(nameOfBank);
                } else if (cardNumLength > 15) {
                    // 如果长度大于15，则用银行卡校验工具类获取银行名并设置到开户行输入框
                    String nameOfBank = BankCardValidUtils.getDetailNameOfBank(cardNum);
                    if (!TextUtils.isEmpty(nameOfBank))
                        neetBankOfAccount.setText(nameOfBank);
                }
            }
        });
    }

    /**
     * 隐藏原银行卡信息布局
     */
    private void hideOriginBankCarInfoLayout() {
        tvOriginBankCardInfo.setVisibility(View.GONE);
        rlBankCardInfo.setVisibility(View.GONE);
    }

    /**
     * 处理原银行卡信息
     *
     * @param bankCardInfo 原银行卡信息
     */
    private void handleOriginBankCardInfo(BankCardInfo bankCardInfo) {
        // 给原银行卡开户姓名赋值，用于对输入的新银行卡开户姓名校验
        username = bankCardInfo.fuserName;
        // 首先判断获取到的原银行卡号是否为空
        if (!TextUtils.isEmpty(bankCardInfo.fcardCode)) {
            // 原银行卡号不为空，显示原银行卡信息布局
            tvOriginBankCardInfo.setVisibility(View.VISIBLE);
            rlBankCardInfo.setVisibility(View.VISIBLE);
            // 给原银行卡号赋值，用于对输入的新银行卡号校验
            oldBankCardNum = bankCardInfo.fcardCode;
            // 设置显示原银行卡信息
            tvAccountName.setText(username);
            tvBankName.setText(BankCardValidUtils.getDetailNameOfBank(oldBankCardNum));
            // 对原银行卡号进行处理，四位一空格，格式化后显示
            StringBuilder stringBuilder = new StringBuilder();
            int oldBankCardNumLegnth = oldBankCardNum.length();
            for (int i = 0; i < oldBankCardNumLegnth; i += 4) {
                if (i + 4 < oldBankCardNumLegnth)
                    stringBuilder.append(oldBankCardNum.substring(i, i + 4));
                else
                    stringBuilder.append(oldBankCardNum.substring(i, oldBankCardNumLegnth));
                stringBuilder.append(" ");
            }
            tvBankCardNum.setText("银行卡卡号 : " + stringBuilder.toString());
        } else {
            // 不存在原银行卡信息
            // 隐藏原银行卡信息布局
            tvOriginBankCardInfo.setVisibility(View.GONE);
            rlBankCardInfo.setVisibility(View.GONE);
        }
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
        // 设置下拉刷新控件可用
        srl.setEnabled(true);
        // 停止刷新
        srl.setRefreshing(false);
    }

    /**
     * 获取原银行卡信息
     *
     * @param subscriber
     */
    private void getOriginBankCardInfo(Subscriber subscriber) {
        // 获取原银行卡信息
        mSubscriptionManager.add(NetHelper.getInstance().getApi(Config.BASE_URL)
                .getBankCardInfo(Constant.faccountId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(subscriber));
    }

    /**
     * 获取输入框的文本，并去除两端的空格
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
     * 处理点击事件
     *
     * @param view
     */
    @OnClick({R2.id.btn_submit, R2.id.btn_cancel, R2.id.iv_valid_code, R2.id.tv_refresh_valid_code, R2.id.tv_back})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        // 点击返回图标
        if (id == R.id.tv_back) {
            // 关闭本页面
            finish();
        }
        // 点击提交
        else if (id == R.id.btn_submit) {
            // 处理提交
            // 获取各个输入框的值
            String password = getText(neetPassword);
            String idCardNum = getText(neetIdCardNum);
            String nameOfAccount = getText(neetNameOfAccount);
            String bankCardNum = getText(neetBankCardNum);
            String inputValidCode = getText(neetValidCode).toLowerCase();
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
            if (!nameOfAccount.equals(username) && !TextUtils.isEmpty(username)) {
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
                            LogUtils.e(TAG, "updateBankCardInfo on_next " + baseResponse.getCode());
                            LogUtils.e(TAG, "updateBankCardInfo on_next " + baseResponse.getMessage());
                            LogUtils.e(TAG, "updateBankCardInfo on_next " + baseResponse.getData());
                            // 获取返回码
                            int code = baseResponse.getCode();
                            switch (code) {
                                case ResponseCode.STATUS_OK: // 请求成功
                                    showDialog("银行卡信息修改成功\n请注意后续付款情况", true);
                                    // 设置原银行卡号，用于更新原银行卡信息布局，并用于用户再次提交修改银行卡信息校验
                                    oldBankCardNum = bankCardNum;
                                    // 显示原银行卡信息布局
                                    tvOriginBankCardInfo.setVisibility(View.VISIBLE);
                                    rlBankCardInfo.setVisibility(View.VISIBLE);
                                    // 更新原银行卡开户名
                                    tvAccountName.setText(username);
                                    // 更新原银行卡开户行
                                    tvBankName.setText(BankCardValidUtils.getDetailNameOfBank(oldBankCardNum));
                                    // 更新原银行卡号，对银行卡号进行处理，四位一空格，格式化后显示
                                    StringBuilder stringBuilder = new StringBuilder();
                                    int oldBankCardNumLegnth = oldBankCardNum.length();
                                    for (int i = 0; i < oldBankCardNumLegnth; i += 4) {
                                        if (i + 4 < oldBankCardNumLegnth)
                                            stringBuilder.append(oldBankCardNum.substring(i, i + 4));
                                        else
                                            stringBuilder.append(oldBankCardNum.substring(i, oldBankCardNumLegnth));
                                        stringBuilder.append(" ");
                                    }
                                    tvBankCardNum.setText("银行卡卡号 : " + stringBuilder.toString());
                                    break;

                                case ResponseCode.USER_NOT_EXIST: // 账户不存在
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
                            // 处理具体错误
                            LogUtils.e(TAG, "updateBankCardInfo on_error " + errorCode);
                            LogUtils.e(TAG, "updateBankCardInfo on_error " + errorMessage);
                        }

                        @Override
                        protected void on_net_error(Throwable e) {
                            String message = e.getMessage();
                            String totalMsg = e.toString();
                            if (message.contains("404") || totalMsg.contains("SocketTimeoutException")
                                    || totalMsg.contains("ConnectException") || totalMsg.contains("RuntimeException")) {
                                showDialog("银行卡信息更改失败\n请稍候再试", false);
                            }
                            LogUtils.e(TAG, "updateBankCardInfo on_net_error " + e.getMessage());
                            LogUtils.e(TAG, "updateBankCardInfo on_net_error " + e.getLocalizedMessage());
                            LogUtils.e(TAG, "updateBankCardInfo on_net_error " + e.toString());
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
        neetValidCode.setText("");
        ivValidCode.setImageBitmap(mCodeGeneUtils.createBitmap());
    }
}
