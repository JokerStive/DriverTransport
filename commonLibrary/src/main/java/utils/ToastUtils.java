package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author WangChenchen
 * @date 2016/11/11
 */

public class ToastUtils {
    public static void show(Context context, String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
