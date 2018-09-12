package com.tengbo.commonlibrary.widget.takePhoto.custom_camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tengbo.basiclibrary.utils.LogUtil;

import java.io.IOException;
import java.util.List;

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
    private SurfaceHolder mHolder;
    private Camera mCamera;

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
        initView();
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
    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.d("surfaceView is created");
        if (mCamera == null) {
            mCamera = Camera.open();  //开启相机

            try {
                mCamera.setPreviewDisplay(mHolder); //相机画面展示到surfaceView上
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d("surfaceView is changed");
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d("surfaceView is destroy");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mHolder = null;
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
        if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

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
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }


        return result;
    }


    public void takePicture() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                LogUtil.d("take picture success--" + data.length);
            }
        });
    }

}
