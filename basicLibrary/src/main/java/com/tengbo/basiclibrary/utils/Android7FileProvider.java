package com.tengbo.basiclibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 适配7.0生成content://的工具类
 *
 * @Autor yk
 * @Description
 */
public class Android7FileProvider {

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private static Uri getUriForFile24(Context context, File file) {
        return FileProvider.getUriForFile(context,
                context.getPackageName() + ".android7.fileProvider",
                file);
    }


    public static Intent setIntentDataAndType(Context context,
                                              Intent intent,
                                              String type,
                                              File file,
                                              boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }

        return intent;
    }
}
