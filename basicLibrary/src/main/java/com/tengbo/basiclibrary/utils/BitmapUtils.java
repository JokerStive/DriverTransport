package com.tengbo.basiclibrary.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;


import com.blankj.utilcode.util.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 图片压缩工具
 *
 * @Autor yk
 * @Description
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
        }
        return b;
    }




}
