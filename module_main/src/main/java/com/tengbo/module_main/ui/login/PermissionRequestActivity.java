package com.tengbo.module_main.ui.login;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.module_main.R;

import utils.permission.PermissionManager;

public class PermissionRequestActivity extends BaseActivity {


    private String[] needPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,

            Manifest.permission.ACCESS_COARSE_LOCATION,

            Manifest.permission.ACCESS_FINE_LOCATION,

            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            Manifest.permission.CALL_PHONE,

            Manifest.permission.ACCESS_COARSE_LOCATION,

            Manifest.permission.CAMERA,


    };

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_permission_request;
    }

    @Override
    protected void initView() {
        PermissionManager.getInstance(getApplicationContext())
                .setOnRequestPermissionResult(new PermissionManager.RequestPermissionResult() {
                    @Override
                    public void onGranted() {
                        startActivity(new Intent(PermissionRequestActivity.this, LoginActivity.class));
                    }

                    @Override
                    public void onDenied(boolean isNotAskAgain) {
                        if (isNotAskAgain) {

                        } else {
                            finish();
                        }
                    }
                })
                .execute(PermissionRequestActivity.this, needPermissions);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance(getApplicationContext())
                .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
