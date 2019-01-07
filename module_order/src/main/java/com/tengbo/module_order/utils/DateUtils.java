package com.tengbo.module_order.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Created by WangChenchen on 2016/12/29.
 */
public class DateUtils {

    public static String FORMAT = "yyyy-MM-dd HH:mm:ss"; // 时间格式化格式

    public static String getNowtime() {
        return new SimpleDateFormat(FORMAT, Locale.CHINA).format(new Date());
    }


    /**
     * @return 当前时间毫秒数
     */
    public static long getNowtimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * String类型的时间转成毫秒数
     *
     * @param StringTime yyyy-MM-dd HH-mm-ss格式的时间
     * @return 毫秒数String
     */
    public static String String2Long(String StringTime) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT, Locale.CHINA);
        try {
            return String.valueOf(format.parse(StringTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
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
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
            returnValue = df.format(date);
        }
        return (returnValue);
    }


    /**iso时间格式转换成utc8
     * @param iso iso时间
     * @return 东八区时间
     */
    public static String iso2Utc(String iso) {
        if(TextUtils.isEmpty(iso)){
            return "";
        }
        try {
            DateFormat outputFormat = new SimpleDateFormat(FORMAT, Locale.CHINA);
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            Date date;
            date = inputFormat.parse(iso);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
