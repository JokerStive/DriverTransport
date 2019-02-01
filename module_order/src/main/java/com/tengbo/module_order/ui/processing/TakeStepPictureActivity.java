package com.tengbo.module_order.ui.processing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adyl.locationLibrary.SignalLocation;
import com.baidu.location.BDLocation;
import com.blankj.utilcode.util.FileUtils;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.basiclibrary.utils.ViewUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.takePhoto.compress.LuBan;
import com.tengbo.commonlibrary.widget.takePhoto.custom_camera.CameraSurfaceView;
import com.tengbo.module_order.R;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.FileResult;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import rx.Observable;
import rx.functions.Func1;

import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

public class TakeStepPictureActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "custom_camera";
    public static final String STEP_PICTURE_PATH = "step_picture_path";
    public static final String ORDER_ID = "orderId";
    public static final String STEP_NAME = "stepName";
    public static final String CACHE_NAME = "/adyl";
    public static final int REQUEST_CODE = 10010;
    private TextView tvOrderNum;
    private CameraSurfaceView surfaceView;
    private ImageView ivTakePicture;
    public byte[] pictureBytes;
    private View rlAfterTakePicture;
    private View rlTakePicture;
    private TextView tvStepAddress;
    private TextView tvStepName;
    private TextView tvStepTime;
    private String orderId;
    private String stepName;
//    private MediaScannerConnection conn;


    @Override
    protected int getLayoutId() {
        return R.layout.order_activity_take_action_picture;
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        orderId = intent.getStringExtra(ORDER_ID);
        stepName = intent.getStringExtra(STEP_NAME);
    }

    @Override
    protected void initView() {
        tvOrderNum = findViewById(R.id.tv_order_num);
        tvStepAddress = findViewById(R.id.tv_step_address);
        tvStepName = findViewById(R.id.tv_step_name);
        tvStepTime = findViewById(R.id.tv_step_time);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setOnSurfaceCreated(() -> surfaceView.setCameraDisplayOrientation(TakeStepPictureActivity.this));


        ivTakePicture = findViewById(R.id.iv_take_picture);
        rlTakePicture = findViewById(R.id.rl_take);
        rlAfterTakePicture = findViewById(R.id.rl_after_take);
        ivTakePicture.setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_take_again).setOnClickListener(this);

        if (TextUtils.isEmpty(stepName)) {
            tvStepName.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(orderId)) {
            tvOrderNum.setVisibility(View.GONE);
        }
        tvStepName.setText(stepName);
        tvOrderNum.setText(orderId);

        getLocation();
    }

    private void getLocation() {
        SignalLocation signalLocation = new SignalLocation(getApplicationContext());
        signalLocation.setOnLocateListener(new SignalLocation.onLocateListener() {
            @Override
            public void onLocate(BDLocation location) {
                String addrStr = location.getAddrStr();
                tvStepAddress.setText(addrStr);
            }
        });
        signalLocation.startLocate();
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) {
            return;
        }
        int id = v.getId();
        if (id == R.id.iv_take_picture) {
            surfaceView.takePicture(callback);
        } else if (id == R.id.tv_cancel) {
            finish();
        } else if (id == R.id.tv_take_again) {
            refreshInterface(true);
            tvStepTime.setText(null);
            surfaceView.startPreview();

        } else if (id == R.id.tv_confirm) {
            compressAndSavePicture(pictureBytes);
        }
    }

    Camera.PictureCallback callback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String time = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss", Locale.CHINA).format(new Date());
            tvStepTime.setText(time);
            pictureBytes = data;
            refreshInterface(false);
        }
    };


    private void refreshInterface(boolean isTakePicture) {
        rlTakePicture.setVisibility(isTakePicture ? View.VISIBLE : View.GONE);
        rlAfterTakePicture.setVisibility(!isTakePicture ? View.VISIBLE : View.GONE);
    }

    /**
     * @param data
     */
    private void compressAndSavePicture(byte[] data) {
        try {
            //给图片添加水印
            Observable.just("")
                    .map(new Func1<String, File>() {
                        @Override
                        public File call(String s) {
                            File file = saveFile(data);
                            assert file != null;
                            return LuBan.get(getApplicationContext()).firstCompress(file);

                        }
                    })
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<File>(this) {
                        @Override
                        protected void on_next(File file) {
                            backTransferData(file);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回并传递数据
     *
     * @param file 经过压缩后最终的图片文件
     */
    private void backTransferData(File file) {
        Intent intent = new Intent();
        intent.putExtra(STEP_PICTURE_PATH, file.getAbsolutePath());
        setResult(200, intent);
        finish();
    }


    /**
     * 返回并传递数据
     */
    private void backTransferData(String path) {
        Intent intent = new Intent();
        intent.putExtra(STEP_PICTURE_PATH, path);
        setResult(200, intent);
        finish();
    }


    /**
     * 添加水印并保存到相册
     *
     * @param data 拍照返回的数据
     * @return 保存后的文件
     */
    private File saveFile(byte[] data) {
        Bitmap watermarkBitmap = null;
        BufferedOutputStream bos = null;
        try {
            watermarkBitmap = createWatermark(data);
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA).format(new Date()) + ".jpg";
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + CACHE_NAME);
            Log.i(TAG, "自定义相机存储目录" + dir.getAbsolutePath());
            if (!dir.exists()) {
                boolean newFile = dir.createNewFile();
            }
            File file = new File(dir.getParent(), filename);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            assert watermarkBitmap != null;
            watermarkBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中

            //旋转90
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
            exifInterface.saveAttributes();

            insertToAlbum(file.getAbsolutePath());
            return file;

        } catch (Exception e) {
            LogUtil.d(e.getMessage());
        } finally {
            try {
                if (bos != null) {
                    bos.flush();//输出
                    bos.close();//关闭
                }

                if (watermarkBitmap != null) {
                    watermarkBitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 添加水印
     *
     * @param data 图片字节数据
     * @return 添加水印后的图片
     */
    private Bitmap createWatermark(byte[] data) {
        Bitmap bitmap = null;
        try {
            String markText = orderId+"\n" + stepName +"\n"+
                    tvStepAddress.getText().toString() + "\n" +
                    tvStepTime.getText().toString();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // 获取图片的宽高
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            // 创建一个和图片一样大的背景图
            Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            // 画背景图
            canvas.drawBitmap(bitmap, 0, 0, null);


            //-------------开始绘制蒙版-------------------------------
            Paint maskPaint = new Paint(); //蒙版的画笔
            maskPaint.setColor(Color.parseColor("#13000000"));
            Rect rect = new Rect(0, 0, UiUtils.dp2px(getApplicationContext(), 250), bitmap.getWidth());
            canvas.drawRect(rect, maskPaint);

            //-------------开始绘制文字-------------------------------
            if (!TextUtils.isEmpty(markText)) {
                int screenWidth = getScreenWidth();
                float textSize = UiUtils.dp2px(getApplicationContext(), 10) * bitmapWidth / screenWidth;
                // 创建画笔
                TextPaint mPaint = new TextPaint();

                // 文字矩阵区域
                Rect textBounds = new Rect();

                // 水印的字体大小
                mPaint.setTextSize(textSize);
                // 文字阴影
                mPaint.setShadowLayer(0.5f, 0f, 1f, Color.YELLOW);
                // 抗锯齿
                mPaint.setAntiAlias(true);
                // 水印的区域
                mPaint.getTextBounds(markText, 0, markText.length(), textBounds);
                // 水印的颜色
                mPaint.setColor(Color.WHITE);
                StaticLayout layout = new StaticLayout(markText, 0, markText.length(), mPaint, (int) (bitmapWidth - textSize),
                        Layout.Alignment.ALIGN_NORMAL, 1.5F, 0.5F, true);
                // 文字开始的坐标
                float textX = UiUtils.dp2px(getApplicationContext(), 8) * bitmapWidth / screenWidth;
                // 画文字
                canvas.rotate(-90);
                canvas.translate(-bitmapHeight + textX, textX);
                layout.draw(canvas);

            }

            //保存所有元素
            canvas.save();
            canvas.restore();

            return bmp;

        } catch (Exception ignored) {

        }

        return bitmap;
    }

    /**
     * 保存图片到相册并通知
     *
     * @param absolutePath 图片的路径
     */
    private void insertToAlbum(String absolutePath) {
        try {
            MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), absolutePath, FileUtils.getFileName(absolutePath), null);
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{absolutePath}, new String[]{"image/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } catch (FileNotFoundException e) {

        }
    }


}
