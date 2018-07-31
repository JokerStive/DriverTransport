package com.tengbo.commonlibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.tengbo.basiclibrary.imageUtils.ImageLoader;
import com.tengbo.commonlibrary.R;

import java.util.List;
import java.util.jar.Manifest;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * 底部弹出拍照或相册选择图片
 */
@RuntimePermissions
public class TakePhotoDialogFragment extends DialogFragment implements View.OnClickListener {


    private int REQUEST_CODE_CAMERA=0;
    private int REQUEST_CODE_GALLERY=1;
    private TakePhotoCallBack listener;
    private int photoCount;
//    private FunctionConfig mConfig;

    public static TakePhotoDialogFragment newInstance(int photoCount) {
        TakePhotoDialogFragment fragment = new TakePhotoDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("photoCount", photoCount);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            photoCount = getArguments().getInt("photoCount");
        }
//        mConfig = new FunctionConfig.Builder()
//                .setMutiSelectMaxSize(photoCount)
//                .setEnableCamera(true)
//                .setEnableCrop(true)
//                .setCropReplaceSource(false)
//                .build();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window!=null)
        window.setGravity(Gravity.BOTTOM);


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
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (dialog.getWindow()!=null)
            dialog.getWindow().setLayout((dm.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_open_camera) {
            openCamera();

        } else if (i == R.id.tv_open_gallery) {
            openGallery();

        }
    }

    @NeedsPermission({})
    private void openGallery() {
//        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new GalleryFinal.OnHanlderResultCallback() {
//            @Override
//            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//                if (listener!=null  && reqeustCode==REQUEST_CODE_GALLERY && resultList!=null && resultList.size()>0){
//                   listener.onGallerySuccess(resultList);
//                }
//            }
//
//            @Override
//            public void onHanlderFailure(int requestCode, String errorMsg) {
//
//            }
//        });

    }

    @NeedsPermission({android.Manifest.permission.CAMERA})
    private void openCamera() {
//        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, new GalleryFinal.OnHanlderResultCallback() {
//            @Override
//            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//                if(listener!=null && reqeustCode==REQUEST_CODE_CAMERA && resultList!=null && resultList.size()>0){
//                    listener.onCameraSuccess(resultList.get(0));
//                }
//            }
//
//            @Override
//            public void onHanlderFailure(int requestCode, String errorMsg) {
//
//            }
//        });
    }


    public void setOnResultListener(TakePhotoCallBack listener) {
        this.listener = listener;
    }


    public interface TakePhotoCallBack {
//        void onGallerySuccess(List<PhotoInfo>  resultList);
//        void onCameraSuccess(PhotoInfo  result);
    }

    @OnPermissionDenied(android.Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(getActivity(), "权限被拒绝了", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(android.Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(getActivity(),"不再询问", Toast.LENGTH_SHORT).show();
    }


}
