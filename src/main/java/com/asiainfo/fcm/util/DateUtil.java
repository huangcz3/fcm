package com.asiainfo.fcm.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    public static String getCurrentMonthYYYYMMString() {
        return LocalDate.now().format(monthFormatter);
    }

    public static String getNextMonthYYYYMMString() {
        return LocalDate.now().plusMonths(1).format(monthFormatter);
    }

    public static String getNextYYYYMMDDString() {
        return LocalDate.now().plusDays(1).format(dayFormatter);
    }

    public static String getTimeYYYYMMDDHHDDSS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getTimemillisecond(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return df.format(new Date());
    }

    /**
     * 将字符串格式yyyyMMdd的字符串转为日期，格式"yyyy-MM-dd"
     *
     * @param date 日期字符串
     * @return 返回格式化的日期
     */
    public static String strToDateFormat(String date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        Date newDate = formatter.parse(date);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }

    /**
     * 将时间戳转为日期，格式"yyyy-MM-dd HH:MM:SS"
     *
     * @param str_num 时间戳
     * @return 返回格式化的日期
     */
    public static String dateToStamp(String str_num) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(toLong(str_num)));
            return date;
        } else {
            String date = sdf.format(new Date(toLong(str_num) * 1000L));
            return date;
        }
    }

    /**
     * 将时间戳转为日期，格式"yyyy-MM-dd HH:MM:SS"
     *
     * @param time 时间戳
     * @return 返回格式化的日期
     */
    public static Long StringToLong(String time) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(time).getTime();
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    public static String getCurrentYYYYMMDDString() {
        return LocalDate.now().format(dayFormatter);
    }
}
