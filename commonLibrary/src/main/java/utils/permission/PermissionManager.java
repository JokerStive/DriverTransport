package utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermissionManager {
    private static final int REQUEST_CODE = 2018;
    private HashMap<String, Integer> hashmap;
    private Activity targetActivity;
    private List<String> currentRequestPermissions = new ArrayList<>();
    private RequestPermissionResult listener;

    private static class Holder {
        public static final PermissionManager MANAGER = new PermissionManager();
    }

    private static Context sContext;

    private PermissionManager() {
    }

    /**
     * 单例获取对象
     */
    public static PermissionManager getInstance(Context context) {
        sContext = context.getApplicationContext();
        return Holder.MANAGER;
    }


    /**
     * @param target      需要申请的activity，fragment
     * @param permissions 多个权限集
     */
    public void execute(@NonNull Object target, String... permissions) {
        initTargetActivity(target);
        List<String> lists = new ArrayList<>();
        for (String permission : permissions) {
            if (!isGranted(permission) && !isRevoked(permission)) {
                lists.add(permission);
            }
        }
        if ( needRequestPermission() ||  lists.size() == 0) {
            if (listener != null) {
                listener.onGranted();
            }
            return;
        }
        String[] p = new String[lists.size()];
        requestPermissions(targetActivity, lists.toArray(p));
    }


    private void initTargetActivity(@NonNull Object target) {
        if (target instanceof Activity) {
            targetActivity = (Activity) target;
        } else if (target instanceof Fragment) {
            targetActivity = ((Fragment) target).getActivity();
        } else {
            throw new RuntimeException("the target must be Activity or Fragment");
        }


    }

    /**
     * 执行请求一个权限
     *
     * @param target
     * @param permission
     * @return
     */
    public PermissionManager execute(@NonNull Object target, String permission) {
        if (!needRequestPermission() && listener!=null){
            listener.onGranted();
            return this;
        }
        initTargetActivity(target);
        if (!isGranted(permission) && !isRevoked(permission)) {
            currentRequestPermissions.clear();
            currentRequestPermissions.add(permission);
            requestPermissions(targetActivity, permission);
        } else if (listener != null ) {
            listener.onGranted();
        }

        return this;
    }


    public interface RequestPermissionResult {
        void onGranted();

        void onDenied(boolean isNotAskAgain);
    }

    public PermissionManager setOnRequestPermissionResult(RequestPermissionResult listener) {
        this.listener = listener;
        return this;
    }

    /**
     * /**
     * 判断是不是授权
     */
    private boolean isGranted(@NonNull String permission) {
        return needRequestPermission() && ContextCompat.checkSelfPermission(sContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断是不是在包中申明
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean isRevoked(@NonNull String permission) {
        return needRequestPermission() && sContext.getPackageManager().isPermissionRevokedByPolicy(permission, sContext.getPackageName());
    }

    /**
     * 判断是不是M及以上版本
     */
    private boolean needRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 请求的方法
     */
    private void requestPermissions(@NonNull Activity activity, String... permissions) {
        if (needRequestPermission()) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
        }
    }


    public boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, String permission, Builder builder) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            builder.showDialog(permission);
            return true;
        }
        return false;
    }


    /**
     * 权限调用返回方法，在activity或者fragment的onRequestPermissionResult中调用此方法，把结果交给permissionManager处理
     *
     * @param requestCode  .i
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (listener == null) return;
        if (requestCode == REQUEST_CODE && grantResults != null && grantResults.length > 0) {
            permissionResult(permissions, grantResults);
        } else {
            listener.onDenied(true);
        }

    }


    /**
     * @param permissions
     * @param grantResults
     */
    private void permissionResult(String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(targetActivity, permissions[i])) {
                    listener.onDenied(true);
                } else {
                    listener.onDenied(false);
                }
                return;
            }

        }
        listener.onGranted();
    }


    /**
     * 请求权限后，通过返回值判断是不是授权
     */
    public boolean getGrantedInfo(String permission) {
        if (!needRequestPermission()) {
            return true;
        }
        return (hashmap != null && hashmap.get(permission) != null && hashmap.get(permission) == PackageManager.PERMISSION_GRANTED) || isGranted(permission);
    }

    /**
     * 内部类，通过建造者模式传递数据，显示对话框
     */
    public class Builder {
        private String message;
        private String title;
        private int icon;
        private String ok;
        private String cancel;
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public String getMessage() {
            return message;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public int getIcon() {
            return icon;
        }

        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public String getOk() {
            return ok;
        }

        public Builder setOk(String ok) {
            this.ok = ok;
            return this;
        }

        public String getCancel() {
            return cancel;
        }

        public Builder setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public void showDialog(final String p) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message)
                    .setIcon(icon)
                    .setTitle(title)
                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(activity, p);
                        }
                    }).setNegativeButton(cancel, null)
                    .create().show();
        }
    }

}