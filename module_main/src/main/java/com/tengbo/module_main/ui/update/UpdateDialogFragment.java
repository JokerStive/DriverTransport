package com.tengbo.module_main.ui.update;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxFileCallBack;
import com.tamic.novate.util.FileUtil;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.module_main.R;
import com.tengbo.module_main.bean.UpdateInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import utils.ToastUtils;

/**
 * @author yk
 * @Description 更新弹窗
 */

public class UpdateDialogFragment extends DialogFragment {
    static String UPDATE_INFO = "update_info";
    TextView tvUpdateInfoDesc;
    TextView tvUpdateNow;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private UpdateInfo mUpdateInfo;
    private View ivClose;
    String BASE_URL = "http://wap.dl.pinyin.sogou.com/";


    public static UpdateDialogFragment newInstance(UpdateInfo info) {

        Bundle args = new Bundle();
        args.putSerializable(UPDATE_INFO, info);
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mUpdateInfo = (UpdateInfo) getArguments().getSerializable(UPDATE_INFO);
        if (mUpdateInfo == null) return;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvUpdateInfoDesc = view.findViewById(R.id.tv_update_desc);
        tvUpdateNow = view.findViewById(R.id.tv_update_now);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tv_progress);
        ivClose = view.findViewById(R.id.iv_close);


        String forcedUpdate = "";
        if (mUpdateInfo.isFocusUpdate()) {
            ivClose.setVisibility(View.GONE);
            forcedUpdate = "您需要升级后才能使用<br/>";
        }
        setCancelable(!mUpdateInfo.isFocusUpdate());
        Spanned spanned = Html.fromHtml("发现新版本" + "<font color = '#F7631A'>" + mUpdateInfo.getUpdateDescription() + "</font><br/>"
                + forcedUpdate + mUpdateInfo.getUpdateDescription());
        tvUpdateInfoDesc.setText(spanned);
        tvUpdateNow.setOnClickListener(v -> {
            startLoad();
        });
        ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void startLoad() {
        if (mUpdateInfo.isFocusUpdate()) {
            startLoadApk();
        } else {
            startLoadApkBackground();
        }


    }

    /**
     * 后台下载apk
     */
    private void startLoadApkBackground() {
        Intent intent = new Intent(getActivity(), DownLoadService.class);
        String url = mUpdateInfo.getUpdateUrl();
        intent.putExtra("url", url);
        getActivity().startService(intent);
        ToastUtils.show(BaseApplication.get(), "安装包正在后台下载中,请稍候");
        dismiss();
    }


    /**
     * 强制更新下载apk
     */
    private void startLoadApk() {
        tvUpdateNow.setVisibility(View.GONE);
        tvProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = mUpdateInfo.getUpdateUrl();
        DownloadManager.getInstance().download(url, new DownLoadCallBack() {

            private String result;

            @Override
            public void onSuccess(File file) {
                AppUtils.installApp(file);
                LogUtil.d("安装apk--" + file.getPath());
            }

            @Override
            public void onProgress(int progress, int total) {
                LogUtil.i("progress--" + progress + "total--" + total);
                result = (float) (progress / total * 100) + "%";
                progressBar.setMax(total);
                progressBar.setProgress(progress);
                tvProgress.setText(result);
            }

            @Override
            public void onFail(java.lang.Throwable e) {
                LogUtil.e("error----" + e.getMessage(), e);
            }
        });
    }


}
