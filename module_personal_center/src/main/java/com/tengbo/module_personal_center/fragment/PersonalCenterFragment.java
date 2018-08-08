package com.tengbo.module_personal_center.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.PersonInfo;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.PersonInfoService;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.R2;
import com.tengbo.module_personal_center.activity.UpdateBankCardInfoActivity;
import com.tengbo.module_personal_center.activity.UpdatePasswordActivity;
import com.tengbo.module_personal_center.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心页面
 */
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R2.id.civ_avatar)
    ImageView civ_avatar;
    @BindView(R2.id.tv_username)
    TextView tv_username;
    @BindView(R2.id.tv_phone_num)
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
      /*  NetHelper.getInstance().getRetrofitBuilder().build()
                .create(PersonInfoService.class)
                .postPersonInfo(User.getAccessToken())
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<PersonInfo>(getActivity()) {
                    @Override
                    protected void on_next(PersonInfo personInfo) {
                        tv_username.setText(personInfo.fuserName);
                        tv_phone_num.setText(personInfo.faccountName);
                        Glide.with(PersonalCenterFragment.this)
                                .load(personInfo.fuserAvatar)
                                .into(civ_avatar);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }
                });*/
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
    @OnClick({R2.id.civ_avatar, R2.id.tv_update_bank_card_info, R2.id.tv_update_password, R2.id.tv_logout})
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
