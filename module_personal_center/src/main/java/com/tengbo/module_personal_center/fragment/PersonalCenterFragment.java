package com.tengbo.module_personal_center.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.bumptech.glide.Glide;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.commonBean.Account;
import com.tengbo.commonlibrary.commonBean.FileUrl;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.takePhoto.TakePhotoDialogFragment;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.activity.BankCardListActivity;
import com.tengbo.module_personal_center.activity.UpdatePasswordActivity;
import com.tengbo.module_personal_center.utils.DialogUtils;
import com.tengbo.module_personal_center.utils.ToastUtils;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;
import utils.RetrofitUtils;

/**
 * author WangChenchen
 * 个人中心页面
 */
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "PersonalCenterFragment";


    // 圆形头像图片视图
    ImageView civAvatar;
    // 用户名文本视图
    TextView tvUsername;

    ProgressBar progressBar;
    private String oldUserAvatar;
    private String newAvatar;

    /**
     * 创建PersonalCenterFragment对象
     */
    public static PersonalCenterFragment newInstance() {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        Bundle args = new Bundle();
        personalCenterFragment.setArguments(args);
        return personalCenterFragment;
    }

    /**
     * 初始化视图，在BaseFragment中调用
     */
    @Override
    protected void initView() {
        tvUsername = mRootView.findViewById(R.id.tv_username);
        civAvatar = mRootView.findViewById(R.id.civ_avatar);
        progressBar = mRootView.findViewById(R.id.progressBar);
        mRootView.findViewById(R.id.tv_update_bank_card).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_update_password).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_logout).setOnClickListener(this);
        civAvatar.setOnClickListener(this);

        tvUsername.setText(User.getName());

        oldUserAvatar = User.getAvatar();
        Glide.with(this).load(oldUserAvatar).into(civAvatar);
    }


    private void showDialog(String msg, boolean isSuccess) {
        // 设置要显示的图标sourceId
        int imgId = R.drawable.right;
        if (!isSuccess)
            imgId = R.drawable.wrong;
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.show(getActivity(), imgId, msg);
    }

    /**
     * 获取布局Id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_center;
    }


    /**
     * 获取参数
     *
     * @param arguments
     */
    @Override
    protected void getTransferData(Bundle arguments) {
    }

    /**
     * 处理点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        // 换头像
        if (id == R.id.civ_avatar) {
            TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(1, false);
            takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
                @Override
                public void onSuccess(List<File> files) {
                    // 图片获取成功，关闭拍照和选择图片对话框
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                    uploadImage(files);

                }

                @Override
                public void onError() {
                    // 关闭拍照和选择图片对话框
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                }
            });

            takePhotoDialogFragment.show(mSupportFragmentManager, TAG);
        }

        // 点击银行卡信息更改
        else if (id == R.id.tv_update_bank_card) {
            startActivity(new Intent(getActivity(), BankCardListActivity.class));
        }

        // 点击密码修改
        else if (id == R.id.tv_update_password) {
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        }
        // 点击退出登录
        else if (id == R.id.tv_logout) {
            DialogUtils utils = new DialogUtils();
            utils.show(getActivity(), "提示", "确定退出登录?", () -> {
                        User.clear();
                        CC.obtainBuilder(ComponentConfig.Main.COMPONENT_NAME)
                                .setActionName(ComponentConfig.Main.ACTION_OPEN_LOGIN_ACTIVITY)
                                .build().call();
                        Objects.requireNonNull(getActivity()).finish();
                    }
            );
        }
    }

    /**
     * @param files .
     */
    private void uploadImage(List<File> files) {

        progressBar.setVisibility(View.VISIBLE);

        //图片上传中不可点击
        civAvatar.setEnabled(false);

        // 获取图片文件对象
        File file = files.get(0);
        String absolutePath = file.getAbsolutePath();
        // 判断图片是否为空
        if (!TextUtils.isEmpty(absolutePath)) {

            Glide.with(PersonalCenterFragment.this).load(file)
                    .into(civAvatar);

            // 上传头像
            mSubscriptionManager.add(uploadAvatarObservable(file)
                    .flatMap(new Func1<BaseResponse<FileUrl>, Observable<BaseResponse>>() {

                        @Override
                        public Observable<BaseResponse> call(BaseResponse<FileUrl> response) {
                            newAvatar = response.getData().getFileUrl();
                            LogUtil.d(newAvatar);
                            return updateUserAvatarObservable(newAvatar);
                        }
                    })
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<BaseResponse>() {
                        @Override
                        protected void on_next(BaseResponse o) {
                            civAvatar.setEnabled(true);
                            ToastUtils.show(_mActivity.getApplicationContext(), "头像修改成功");
                            progressBar.setVisibility(View.INVISIBLE);
                            User.saveAvatar(newAvatar);
                        }


                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            civAvatar.setEnabled(true);
                            Glide.with(PersonalCenterFragment.this).load(oldUserAvatar).into(civAvatar);
                            ToastUtils.show(_mActivity.getApplicationContext(), "上传头像失败，请换个姿势再试！");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }));
        }
    }


    public Observable<BaseResponse<FileUrl>> uploadAvatarObservable(File file) {
        Map<String, RequestBody> params = RetrofitUtils.getRequestBodyMap(file);
        return NetHelper.getInstance().getApi()
                .uploadFiles(params);
    }


    public Observable<BaseResponse> updateUserAvatarObservable(String avatar) {
        Account account = new Account();
        account.setUserId(User.getUserId());
        account.setUserAvatar(avatar);
        return NetHelper.getInstance().getApi()
                .updateUserInfo(account);
    }


}
