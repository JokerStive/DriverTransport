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
                setCameraParams(mCamera, mScreenWidth, mScreenHeight);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    private void setCameraParams(Camera camera, int width, int height) {

        Camera.Parameters parameters = camera.getParameters();

        //获取摄像头支持的图片尺寸
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

        //获取合适的分辨率
        Camera.Size properSize = getProperSize(supportedPictureSizes, ((float) height) / width);

        if (properSize == null) {
            properSize = parameters.getPictureSize();
        }

        //根据合适的尺寸设置surfaceView的尺寸
        float w = properSize.width;
        float h = properSize.height;
        parameters.setPictureSize(properSize.width, properSize.height);
        this.setLayoutParams(new RelativeLayout.LayoutParams((int) (height * (h / w)), height));


        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(supportedPreviewSizes, ((float) height) / width);
        if (null != preSize) {
            LogUtil.d("preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }

        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

        //自动曝光
//        parameters.setAutoExposureLock(true);
        //自动白平衡
//        parameters.setAutoWhiteBalanceLock(true);

        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);

    }


    /**
     * @Desc 获取最合适的分辨率
     */
    private Camera.Size getProperSize(List<Camera.Size> supportedPictureSizes, float ratio) {
        Camera.Size result = null;
        for (Camera.Size size : supportedPictureSizes) {
            if (ratio == size.height / size.width) {
                result = size;
            }
        }

        if (null == result) {
            for (Camera.Size size : supportedPictureSizes) {
                float curRatio = ((float) size.height) / size.width;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }


        return result;
    }


    public void takePicture(Camera.PictureCallback callback) {
        mCamera.takePicture(null, null, callback);
        mediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
    }


    public void startPreview() {
        mCamera.startPreview();
    }


}
