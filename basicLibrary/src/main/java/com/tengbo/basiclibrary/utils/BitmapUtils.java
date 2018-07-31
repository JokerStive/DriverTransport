package com.tengbo.basiclibrary.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * C
 * 图片压缩
 */
public class BitmapUtils {

    private static int max_size = 1024;

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        if (filePath.startsWith("content")) {
            filePath = filePath.replace("content", "file");
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, 1080, 800);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        return bitmap;
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据图片本地路径转成二进制byte[]
     */
    public static byte[] bitmapToBytes(String iconPath) {
        Bitmap bm = getSmallBitmap(iconPath);
        byte[] b = null;
        int options_ = 100;
        if (bm != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, options_, bos);
            b = bos.toByteArray();
            while (b.length / 1024 > max_size) {
                bos.reset();
                options_ = Math.max(0, options_ - 10);
                bm.compress(Bitmap.CompressFormat.JPEG, options_, bos);
                b = bos.toByteArray();
                if (options_ == 0)//如果图片的质量已降到最低则，不再进行压缩
                    break;
            }
//            Logger.d("上传图片大小 = " + b.length / 1024 + "k");
        }
        return b;
    }


    /**
     * 创建文件的requestBody
     */
    public static Map<String, RequestBody> createRequestBodies(List<String> iconPaths) {
        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < iconPaths.size(); i++) {
            File file = new File(iconPaths.get(i));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            map.put("file=\"; filename=\"", requestBody);
        }
        return map;
    }

    public static MultipartBody filesToMultipartBody(JSONObject json, List<String> iconPaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object object = json.get(key);
                if (object != null) {
                    builder.addFormDataPart(key, object.toString());
                }
            } catch (JSONException e) {
            }
        }
        if (iconPaths != null) {
            for (String filePath : iconPaths) {
                byte[] pathString = BitmapUtils.bitmapToBytes(filePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), pathString);
                builder.addFormDataPart("", filePath, requestBody);
            }
        }


        builder.setType(MultipartBody.FORM);
        return builder.build();
    }


  /*
     * 根据文件的uri的到文件真实的路径
     */
    public static String getRealPathFromURI(Context context,Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);

            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
