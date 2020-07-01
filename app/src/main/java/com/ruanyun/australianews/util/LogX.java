package com.ruanyun.australianews.util;

import android.util.Log;
import com.ruanyun.australianews.BuildConfig;

public class LogX {
    private static final String THIS_FILE_NAME = "LogX.java";
    private static boolean isShowLogs = BuildConfig.DEBUG;

    public static void isShowLogs(boolean flag) {
        isShowLogs = flag;
    }


    public static final void d(String tag, String msg) {
        if (isShowLogs) {
            Log.d(tag, msg);
        }
    }

    public static final void d(String tag, String msg, Throwable tr) {
        if (isShowLogs) {
            Log.d(tag, msg, tr);
        }
    }

    public static final void i(String tag, String msg) {
        if (isShowLogs) {
            Log.i(tag, msg);
        }
    }

    public static final void i(String tag, String msg, Throwable tr) {
        if (isShowLogs) {
            Log.d(tag, msg, tr);
        }
    }

    public static final void e(String tag, String msg) {
        if (isShowLogs) {
            Log.e(tag, msg);
        }
    }

    public static final void e(String tag, String msg, Throwable e) {
        if (isShowLogs) {
            Log.e(tag, msg, e);
        }
    }

    public static final void v(String tag, String msg) {
        if (isShowLogs) {
            Log.v(tag, msg);
        }
    }

    public static final void v(String tag, String msg, Throwable e) {
        if (isShowLogs) {
            Log.v(tag, msg, e);
        }
    }


    public static void trace(String tag, String msg) {
        if (isShowLogs) {
            Log.i(tag, msg);
        }
    }

    private static void getMessage(){
        final StackTraceElement[] elements = new Throwable().getStackTrace();

        String fileName;
        String methodName;

        int lineNumber;

        for (StackTraceElement element : elements) {

            fileName = element.getFileName();

//            if (THIS_FILE_NAME.equals(fileName)) {
//                continue;
//            }

            lineNumber = element.getLineNumber();
            methodName = element.getMethodName();
            i(THIS_FILE_NAME, "methodName:" + methodName + "lineNumber:" + lineNumber);

        }
    }

}
