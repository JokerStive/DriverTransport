package com.tengbo.commonlibrary.widget.takePhoto.custom_camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.tengbo.basiclibrary.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 显示相机的类
 *
 * @Autor yk
 * @Description
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private final Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private Camera mCamera;
    private String mPicturePath;
    private MediaActionSound mediaActionSound;
    private OnSurfaceCreated listener;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMatrix();
        init();
    }


    /**
     * 获取屏幕的宽高
     */
    private void getScreenMatrix() {
        WindowManager WM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        assert WM != null;
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }


    /**
     * 绑定holder
     */
    private void init() {
        mediaActionSound = new MediaActionSound();
        getHolder().addCallback(this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isActivated()) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_UP) {
                        mCamera.autoFocus(callback);
                    }
                    return true;
                }
                return false;
            }
        });
    }


    Camera.AutoFocusCallback callback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                mCamera.cancelAutoFocus();
            }
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.d("surfaceView is created");
        if (mCamera == null) {

            LogUtil.d("mCamera is  null ");
            mCamera = Camera.open();  //开启相机
            try {
                mCamera.setPreviewDisplay(holder); //相机画面展示到surfaceView上
                setCameraParams();
                if (listener != null) {
                    listener.surfaceCreated();
                }
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public interface OnSurfaceCreated {
        void surfaceCreated();
    }

    public void setOnSurfaceCreated(OnSurfaceCreated created) {
        this.listener = created;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        LogUtil.d("surfaceView is changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d("surfaceView is destroy");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera Camera) {
        if (success) {
            LogUtil.d("autoFocus is success");
        }
    }


    /**
     * @Desc 设置相机的参数
     */
    private void setCameraParams() {

        Camera.Parameters parameters = mCamera.getParameters();

        //获取摄像头支持的图片尺寸
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

        //设置拍照尺寸
        Camera.Size takePictureSuitSize = getProperPreviewSize(supportedPictureSizes, false);
        parameters.setPictureSize(takePictureSuitSize.width, takePictureSuitSize.height);
        LogUtil.d("拍照的width=" + takePictureSuitSize.width + "  拍照的height=" + takePictureSuitSize.height);


        //设置预览尺寸
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSuitSize = getProperPreviewSize(supportedPreviewSizes, true);
        LogUtil.d("预览的width=" + previewSuitSize.width + "  预览的height=" + previewSuitSize.height);
        parameters.setPreviewSize(previewSuitSize.width, previewSuitSize.height);

        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

        mCamera.setParameters(parameters);

    }


    public void setCameraDisplayOrientation(Activity activity) {
        Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    /**
     * 返回最佳的尺寸，如果没有匹配到，就返回中间值，因为是从大到小排序的
     *
     * @return 最适合的尺寸
     */
    private Camera.Size getProperPreviewSize(List<Camera.Size> supportedSizes, boolean isPreview) {
        int tempWidth;
        int tempHeight;
        if (isPreview) {
            tempHeight = mScreenWidth;
            tempWidth = mScreenHeight;
        } else {
            tempHeight = mScreenHeight;
            tempWidth = mScreenWidth;
        }
        for (Camera.Size size : supportedSizes) {
            if ((size.height == tempHeight)  && (size.width==tempWidth)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) tempWidth) / tempHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : supportedSizes) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }


    public void takePicture(Camera.PictureCallback callback) {
        mCamera.takePicture(null, null, callback);
        mediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
    }


    public void startPreview() {
        mCamera.startPreview();
    }


}
