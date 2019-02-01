package com.tengbo.commonlibrary.widget.takePhoto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tengbo.basiclibrary.utils.Android7FileProvider;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.R;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.net.ApiException;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.takePhoto.compress.LuBan;
import com.tengbo.commonlibrary.widget.takePhoto.crop.Crop;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.utils.ImageSelector;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.utils.ImageSelectorUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import utils.ToastUtils;

import static android.app.Activity.RESULT_OK;

/**
 * @author yk
 * @Description 拍照和详细选择控件，自定义是否裁剪，多选数量，返回经过压缩的文件
 */
public class TakePhotoDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_CAMERA = 0;
    private static final int REQUEST_CODE_GALLERY = 1;
    private TakePhotoCallBack listener;
    private int multiChooseSize = 3;
    private boolean isCrop = false;
    private static final String authority = BaseApplication.get().getApplicationInfo().packageName + ".fileprovider";
    private BaseActivity fragmentActivity;
    private File takePictureFile;
    private Uri cropOutPutUri;
    private Context context = BaseApplication.get();

    public static TakePhotoDialogFragment newInstance(int photoCount, Boolean isCrop) {
        TakePhotoDialogFragment fragment = new TakePhotoDialogFragment();
        Bundle args = new Bundle();
        args.putInt("multiChooseSize", photoCount);
        args.putBoolean("isCrop", isCrop);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            multiChooseSize = arguments.getInt("multiChooseSize");
            isCrop = arguments.getBoolean("isCrop");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentActivity = (BaseActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
        }


        setStyle(R.style.ActionSheetDialogStyle, 0);
        View view = inflater.inflate(R.layout.fragment_take_photo, null);
        view.findViewById(R.id.tv_open_gallery).setOnClickListener(this);
        view.findViewById(R.id.tv_open_camera).setOnClickListener(this);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (dialog.getWindow() != null){
                dialog.getWindow().setLayout((dm.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_open_camera) {
            openCamera();

        } else if (i == R.id.tv_open_gallery) {
            openGallery();
        }
    }

    private void dealPermissionDenied(boolean isNotAskAgain) {
        if (isNotAskAgain) {
            showExceptionDialog();
        } else {
            ToastUtils.show(BaseApplication.get(), "您拒绝了该权限");
        }
    }


    /**
     * 发生没有权限等异常时，显示一个提示dialog.
     */
    private void showExceptionDialog() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("您拒绝了相关权限，请到“设置”>“应用”>“权限”中配置权限。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }


    public void openGallery() {
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(multiChooseSize == 0)  //设置是否单选
                .setMaxSelectCount(multiChooseSize) // 图片的最大选择数量，小于等于0时，不限数量。
                .start(fragmentActivity, REQUEST_CODE_GALLERY); // 打开相册
    }

    public void openCamera() {
        //以当前时间为文件名
        String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA).format(new Date()) + ".jpg";

        //保存在公共sd卡中的picture目录吓
        takePictureFile = new File(getActivity().getApplication().getExternalCacheDir(),filename);

        LogUtil.d("camera path = " + takePictureFile.getAbsolutePath());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //用fileProvider生成content://替代file://Uri
        Uri contentUri = Android7FileProvider.getUriForFile(context, takePictureFile);

        LogUtil.d(contentUri.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    /**
     * 裁剪的图片保存在内部存储里面，随着app卸载消失
     *
     * @return 获取裁剪的路径
     */
    private Uri getCropUri() {
        String fileName = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA).format(new Date()) + "crop.jpg";
        File file = new File(Environment.getDownloadCacheDirectory(), fileName);
        return Uri.fromFile(file);
    }


    public void setOnResultListener(TakePhotoCallBack listener) {
        this.listener = listener;
    }


    public interface TakePhotoCallBack {
        void onSuccess(List<File> files);

        void onError();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("request--code--" + requestCode);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (isCrop) {
                        crop(Uri.fromFile(takePictureFile));
                    } else {
                        compressSignal(BaseApplication.get(), takePictureFile.getPath());
                    }
                } else {
                    dismiss();
                }
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imagesPath = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
                    if (imagesPath.size() > 0) {
                        if (multiChooseSize == 1 && isCrop) {
                            crop(Uri.fromFile(new File(imagesPath.get(0))));
                        } else {
                            compressMulit(BaseApplication.get(), imagesPath);
                        }
                    }
                } else {
                    dismiss();
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    compressSignal(BaseApplication.get(), cropOutPutUri.getPath());
                }
                break;

        }

    }

    private void crop(Uri inputUri) {
        cropOutPutUri = getCropUri();
        Crop.of(inputUri, cropOutPutUri).asSquare().start(fragmentActivity);
    }

    /**
     * @author yk
     * @Description 压缩单张图片
     */
    private void compressSignal(Context context, String path) {
        LuBan.get(context)
                .loadWithPath(path)
                .asObservable()
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<File>(fragmentActivity) {
                    @Override
                    protected void on_next(File file) {
                        int byteCount = BitmapFactory.decodeFile(file.getPath()).getByteCount();
                        LogUtil.d("压缩后图片大小--" + byteCount / 1024);
                        if (listener != null) {
                            ArrayList<File> files = new ArrayList<>();
                            files.add(file);
                            listener.onSuccess(files);
                        }
                    }

                    @Override
                    protected void on_error(ApiException e) {
                        super.on_error(e);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });

    }

    private void compressMulit(Context context, List<String> paths) {


        LuBan.get(context)
                .loadWithPaths(paths)
                .asFileList()
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<List<File>>() {
                    @Override
                    protected void on_next(List<File> files) {
                        if (listener != null) {
                            listener.onSuccess(files);
                        }
                    }

                    @Override
                    protected void on_error(ApiException e) {
                        super.on_error(e);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
