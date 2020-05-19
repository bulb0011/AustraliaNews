package com.ruanyun.australianews.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * author: zhangsan on 2017/11/15 下午11:22.
 */

public class StringUtil {

    /**
     * 是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 是否是身份证号
     *
     * @param card
     * @return
     */
    public static boolean isIDCard(String card) {
        Pattern p = Pattern.compile("^\\d{17}([0-9]|X|x)$");
        Matcher m = p.matcher(card);
        return m.matches();
    }

    public static String getPwdIDCard(String idNumber) {
        if (idNumber == null) idNumber = "";
        if (idNumber.length() >= 10) {
            return idNumber.substring(0, 6) + "****" + idNumber.substring(idNumber.length() - 4, idNumber.length());
        } else {
            return idNumber;
        }
    }

    public static String getPwdPhone(String phone) {
        if (phone == null) phone = "";
        if (phone.length() >= 7) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        } else if (phone.length() > 3) {
            return phone.substring(0, 3) + "****";
        } else {
            return phone;
        }
    }


    public static String getSBKH(String sbkh) {
        if (sbkh == null) sbkh = "";
        if (sbkh.length() > 4) {
            return  sbkh.substring(0, 4) + "****";
        } else {
            return sbkh;
        }
    }

    public static String getTimeStrFromFormatStr(String format, String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat timeFormat = getTimeFormat();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = timeFormat.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
        return dateFormat.format(date);
    }

    public static String getyyyyMMddHHmmStr(String format, String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateStr);
        } catch (Exception e) {
            return dateStr;
        }
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(new Date());

        SimpleDateFormat sfd;
        if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
            sfd = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            return sfd.format(date);
        }else {
            sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return sfd.format(date);
        }
    }

    public static String getFormatStrFromFormatStr(String format, String oldFormat, String dateStr) {
        if (!CommonUtil.isNotEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(oldFormat, Locale.CHINA);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = timeFormat.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
        return dateFormat.format(date);
    }

    public static String getTimeStrFromLong(long time) {
        return getTimeStrFromLong("yyyy-MM-dd HH:mm:ss", time);
    }
    public static String getTimeStrFromLong(String format, long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    private static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    }

    /**
     * 获取前n天日期、后n天日期
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        return getOldDate("yyyy/MM/dd", distanceDay);
    }

    /**
     * 获取前n天日期、后n天日期
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(String format, int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat(format);
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }


    public static StringBuilder getCarLength(String length) {
        StringBuilder carLength = new StringBuilder();
        if (TextUtils.isEmpty(length)) return carLength;
        String[] strings = length.split(",");
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (TextUtils.isEmpty(string)) {
                continue;
            }
            if (!TextUtils.isEmpty(carLength)) {
                carLength.append("、");
            }
            carLength.append(string).append("米");
        }
        return carLength;
    }

    /**
     * 获取几分钟、几小时前
     * @return
     */
    public static String getBeApartTime(String originalTime){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(originalTime);
//            Calendar dateCalendar = Calendar.getInstance();
//            dateCalendar.setTime(date);
//            Calendar todayCalendar = Calendar.getInstance();
//            todayCalendar.setTime(new Date());
//            if (dateCalendar.get(Calendar.YEAR) != todayCalendar.get(Calendar.YEAR)) {
//                return getTimeStrFromFormatStr("yyyy-MM-dd", originalTime);
//            }

            long ctime = date.getTime();
            long time = System.currentTimeMillis() - ctime;
            long timeLeft;
            if (TimeUnit.MILLISECONDS.toDays(time) >= 1) {
                return getTimeStrFromFormatStr("MM-dd", originalTime);
            } else if ((timeLeft = TimeUnit.MILLISECONDS.toHours(time)) > 0) {
                return timeLeft + "小时前";
            } else if ((timeLeft = TimeUnit.MILLISECONDS.toMinutes(time)) > 0) {
                return timeLeft + "分钟前";
            } else {//时间不足一分钟，
                return "刚刚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return originalTime;
        }
    }

    /**
     * 根据日期获得星期
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 根据日期获得星期
     * @param calendar
     * @return
     */
    public static String getWeekOfCalendar(Calendar calendar) {
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    public static String getNewsTime(String timeStr){
        return getJustTime("MM-dd HH:mm", timeStr);
    }

    public static String getLifeTime(String timeStr){
        return getJustTime("MM-dd HH:mm", timeStr);
    }

    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getJustTime(String format, String createTime) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createTime);
        } catch (Exception e) {
            return createTime;
        }
        boolean sameYear = false;
        SimpleDateFormat sfd;
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
            sameYear = true;
        }
        if (!sameYear) {
            sfd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sfd.format(date);
        }else {
            if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
                return getBeApartTime(createTime);
            } else {
                todayCalendar.add(Calendar.DATE, -1);
                if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
                    sfd = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    return "昨天 " + sfd.format(date);
//                    return "昨天";
                }else {
//                    sfd = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    sfd = new SimpleDateFormat(format, Locale.getDefault());
                    return sfd.format(date);
                }
            }
        }
    }


    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getTime(String createTime) {
        if(TextUtils.isEmpty(createTime)){
            return "";
        }
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean sameYear = false;
        String todySDF = "HH:mm";
        String otherSDF = "MM-dd HH:mm";
        String otherYearSDF = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
            sameYear = true;
        } else {
            sameYear = false;
        }

        if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(date);
            return "今天 "+time;
        } else {
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
                time = new SimpleDateFormat(todySDF).format(date);
                return "昨天 "+time;
            }
        }

        if (sameYear) {
            sfd = new SimpleDateFormat(otherSDF);
            time = sfd.format(date);
        } else {
            sfd = new SimpleDateFormat(otherYearSDF);
            time = sfd.format(date);
        }

        return time;
    }


    public static void copyToClipboard(Context context, String text) {
        ClipboardManager systemService = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if(systemService !=null) {
            systemService.setPrimaryClip(ClipData.newPlainText("text", text));
            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * true:选择时间>当前时间,false:选择时间<=当前时间
     */
    public static boolean isNotLessThanToday(String time){
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentTime = sfd.format(new Date());
        return currentTime.compareTo(time) < 0;
    }

    /**
     * 获取汉字字符串的第一个字母
     */
    public static String getPinYinFirstLetter(String str) {
        if("重庆市".equals(str)){
            return "C";
        }
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyinArray != null) {
            sb.append(pinyinArray[0].charAt(0));
        } else {
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }

    public static String getWCount(int count) {
        if(count>=10000){
            return (count/10000)+"+";
        }else {
            return count+"";
        }
    }

}
