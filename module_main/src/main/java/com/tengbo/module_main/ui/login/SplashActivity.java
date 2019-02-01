package com.tengbo.module_main.ui.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.module_main.R;

import utils.permission.PermissionManager;

public class SplashActivity extends BaseActivity {

    private PermissionManager permissionManager;

    private String[] needPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,

            Manifest.permission.ACCESS_COARSE_LOCATION,

            Manifest.permission.ACCESS_FINE_LOCATION,

            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            Manifest.permission.CALL_PHONE,

            Manifest.permission.CAMERA,


    };

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_permission_request;
    }

    @Override
    protected void initView() {
        permissionManager = new PermissionManager();
        permissionManager.setOnRequestPermissionResult(new PermissionManager.RequestPermissionResult() {
                    @Override
                    public void onGranted() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onDenied(boolean isNotAskAgain) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setMessage("您拒绝了使用该APP必要的权限，请在：设置->应用管理->司机运输 中开启权限后重新打开")
                                .setTitle("警告")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create().show();
                    }
                })
                .execute(SplashActivity.this, needPermissions);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
