package com.tengbo.module_personal_center.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.activity.UpdateBankCardInfoActivity;
import com.tengbo.module_personal_center.activity.UpdatePasswordActivity;
import com.tengbo.module_personal_center.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心页面
 */
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.civ_avatar)
    ImageView civ_avatar;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;

    public static PersonalCenterFragment newInstance() {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        Bundle args = new Bundle();
        personalCenterFragment.setArguments(args);
        return personalCenterFragment;
    }

    @Override
    protected void initView() {
        // 请求个人信息
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
    }

    /**
     * 处理点击事件
     *
     * @param view
     */
    @OnClick({R.id.civ_avatar, R.id.tv_update_bank_card_info, R.id.tv_update_password, R.id.tv_logout})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.civ_avatar) {
            // 处理头像选择、上传
            ToastUtils.show(getActivity(), "头像");
        } else if (id == R.id.tv_update_bank_card_info) {
            // 跳转到银行卡信息更改页面
            startActivity(new Intent(getActivity(), UpdateBankCardInfoActivity.class));
        } else if (id == R.id.tv_update_password) {
            // 跳转到密码修改页面
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        } else if (id == R.id.tv_logout) {
            // 处理退出登录
            getActivity().finish();
        }
    }
}
