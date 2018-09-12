package com.tengbo.module_order.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WangChenchen on 2016/12/29.
 */
public class DateUtils {

    public static String FORMATTIMESTR = "yyyy年MM月dd日 HH:mm:ss"; // 时间格式化格式

    /**
     * 返回此时时间
     *
     * @return String: XXX年XX月XX日 XX:XX:XX
     */
    public static String getNowtime() {
        return new SimpleDateFormat(FORMATTIMESTR).format(new Date());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }



}
