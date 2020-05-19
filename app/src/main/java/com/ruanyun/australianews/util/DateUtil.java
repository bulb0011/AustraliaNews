package com.ruanyun.australianews.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Z on 2017/12/18.
 */

public class DateUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat DATE_FORMAT_MONTH_DAY = new SimpleDateFormat("MM-dd");
    private static SimpleDateFormat DATE_FORMAT_MONTH_DAY_HOURS_MINUTE = new SimpleDateFormat("MM月dd日HH:mm");
    private static SimpleDateFormat DATE_FORMAT_YEAR_MONTH_DAY = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 日期逻辑
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr) {
        //现在时间
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        //发表时间
        Date date = strToDate(dateStr, DATE_FORMAT);
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time < 60) { // 1分钟内
            return sb.append(time + "秒前").toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60+"分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 +"小时前").toString();
        }else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString();
        }else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString();
        }else if (time >= 3600 * 72) {
            return dateToString(dateStr, DATE_FORMAT);
        }
        return dateToString(dateStr, DATE_FORMAT);
    }

    /**
     * 日期字符串转换为Date
     * @param dateStr
     * @param format
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;
        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }
        return date;
    }

    /**
     * 日期转换为字符串
     * @param timeStr
     * @param format MM-dd
     * @return
     */
    public static String dateToString(String timeStr, String format) {
        // 判断是否是今年
        Date date = strToDate(timeStr, format);
        int year = new Date().getYear();
        // 如果是今年的话，才去“xx月xx日”日期格式
        if (date.getYear() == year) {
            return DATE_FORMAT_MONTH_DAY.format(date);
        }

        return DATE_FORMAT_YEAR_MONTH_DAY.format(date);
    }

    /**
     * 日期转换为字符串
     * @param timeStr
     * @param format MM月dd日HH:mm
     * @return
     */
    public static String date2String(String timeStr, String format) {
        // 判断是否是今年
        Date date = strToDate(timeStr, format);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        // 如果是今年的话，才去“xx月xx日”日期格式
        if (calendar.get(Calendar.YEAR) == year) {
            return DATE_FORMAT_MONTH_DAY_HOURS_MINUTE.format(date);
        }

        return DATE_FORMAT_YEAR_MONTH_DAY.format(date);
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTimeYMD() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dateFormat.format(new Date());
    }

    /**
     * 获取指定格式日期，如果是本年，则不显示年份
     * @param timeStr 日期
     * @param format 指定格式  如 yyyy-MM-dd HH:mm
     * @return
     */
    public static String getSpecificTime(String timeStr, String format) {
        if(!CommonUtil.isNotEmpty(timeStr)){
            return "";
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);

        // 判断是否是今年
        Date date = null;
        try {
            date = timeFormat.parse(timeStr);
            int year = new Date().getYear();
            // 如果是今年的话，才去“xx月xx日”日期格式
            if (date.getYear() == year) {
                int index = format.indexOf("M");
                dateFormat = new SimpleDateFormat(format.substring(index,format.length()), Locale.CHINA);
                return dateFormat.format(date);
            }else {
                return dateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    public static String getCurrentTime(String format){
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(new Date());
    }

}
