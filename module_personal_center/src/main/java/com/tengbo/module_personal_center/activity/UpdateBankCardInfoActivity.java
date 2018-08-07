package com.tengbo.module_personal_center.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.basiclibrary.utils.CodeGeneUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.custom.view.NoEmojiEditText;
import com.tengbo.module_personal_center.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdateBankCardInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_bank_of_account)
    EditText et_bank_of_account;
    @BindView(R.id.et_name_of_account)
    EditText et_name_of_account;
    @BindView(R.id.et_bank_card_num)
    EditText et_bank_card_num;

    @BindView(R.id.neet_password)
    NoEmojiEditText neet_password;
    @BindView(R.id.neet_id_card_num)
    NoEmojiEditText neet_id_card_num;
    @BindView(R.id.neet_bank_of_account)
    NoEmojiEditText neet_bank_of_account;
    @BindView(R.id.neet_name_of_account)
    NoEmojiEditText neet_name_of_account;
    @BindView(R.id.neet_bank_card_num)
    NoEmojiEditText neet_bank_card_num;
    @BindView(R.id.neet_valid_code)
    NoEmojiEditText neet_valid_code;
    @BindView(R.id.iv_valid_code)
    ImageView iv_valid_code;
    @BindView(R.id.tv_refresh_valid_code)
    TextView tv_refresh_valid_code;

    CodeGeneUtils mCodeGeneUtils;
    String username = "";
    String oldBankCardNum = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_bank_card_info;
    }

    @Override
    protected void initView() {
        mCodeGeneUtils = CodeGeneUtils.getInstance();
        // 初始化验证码
        iv_valid_code.setImageBitmap(mCodeGeneUtils.createBitmap());
        SpannableString ss = new SpannableString(getString(R.string.refresh));
        ss.setSpan(new UnderlineSpan(), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_refresh_valid_code.setText(ss);
    }

    private String getText(NoEmojiEditText neet) {
        return neet.getText().toString().trim();
    }

    private void showToast(String text) {
        ToastUtils.show(this, text);
    }

    /**
     * 处理点击事件
     * @param view
     */
    @OnClick({R.id.btn_submit, R.id.btn_cancel, R.id.iv_valid_code, R.id.tv_refresh_valid_code})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_submit)
        {
            // 处理提交
            // 获取各个输入框的值
            String password = getText(neet_password);
            String idCardNum = getText(neet_id_card_num);
            String bankOfAccount = getText(neet_bank_of_account);
            String nameOfAccount = getText(neet_name_of_account);
            String bankCardNum = getText(neet_bank_card_num);
            String inputValidCode = getText(neet_valid_code).toLowerCase();
            String validCode = mCodeGeneUtils.getCode().toLowerCase();

            // 判断各个输入框是否为空
            if(TextUtils.isEmpty(password))
            {
                showToast("请先输入密码");
                return;
            }else if(TextUtils.isEmpty(idCardNum))
            {
                showToast("请先输入身份证号");
                return;
            }else if(TextUtils.isEmpty(bankOfAccount))
            {
                showToast("请先输入开户银行");
                return;
            }else if(TextUtils.isEmpty(nameOfAccount))
            {
                showToast("请先输入开户人姓名");
                return;
            }else if(TextUtils.isEmpty(bankCardNum))
            {
                showToast("请先输入银行卡号");
                return;
            }else if(TextUtils.isEmpty(inputValidCode))
            {
                showToast("请先输入验证码");
                return;
            }

            // 判断开户人姓名是否是登录用户
            if(!nameOfAccount.equals(username))
            {
                showToast("开户人姓名必须和用户名相同");
                refreshValidCode();
                return;
            }

            // 判断银行卡号是否和原卡号相同
            if(bankCardNum.equals(oldBankCardNum))
            {
                showToast("新的银行卡号和原卡号相同");
                refreshValidCode();
                return;
            }

            // 判断验证码是否正确
            if(!inputValidCode.equals(validCode))
            {
                showToast("验证码错误");
                refreshValidCode();
                return;
            }

            // 网络校验密码、身份证号、修改银行卡信息

        }else if(id == R.id.btn_cancel)
        {
            // 处理取消，关闭本页面
            finish();
        }else if(id == R.id.iv_valid_code || id == R.id.tv_refresh_valid_code)
        {
            // 处理刷新验证码
            refreshValidCode();
        }
    }

    private void refreshValidCode()
    {
        neet_valid_code.setText("");
        iv_valid_code.setImageBitmap(mCodeGeneUtils.createBitmap());
    }
}
