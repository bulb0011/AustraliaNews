package com.ruanyun.australianews.util;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruanyun.australianews.R;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * author: zhangsan on 16/11/21 上午9:46.
 */

public class CommonUtil {
    private static Toast toast;
    private static Context mAppContext;

    public static void setAppContext(Context mAppContext) {
        CommonUtil.mAppContext = mAppContext;
    }

    public static void showToast(String tip) {
        showToast(tip, R.color.transparent);
    }

    public static void showToast(@StringRes int tip) {
        showToast(mAppContext.getResources().getString(tip), R.color.transparent);
    }

    /**
     * 显示带状态图片的自定义toast
     *
     * @author zhangsan
     * @date 16/11/21 下午5:52
     */
    public static void showToast(String tip, int statusDrawable) {
        View contentView;
        if (toast == null) {
            toast = new Toast(mAppContext);
            contentView =
                    LayoutInflater.from(mAppContext).inflate(R.layout.view_custom_toast, null);
            toast.setView(contentView);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            contentView = toast.getView();
        }
        setToastViewAttr(contentView, tip, statusDrawable);
        toast.show();
    }

    private static void setToastViewAttr(View contentView, String tip, int statusDrawable) {
        if (null == contentView) {
            return;
        }
        contentView.getBackground().setAlpha(150);
        TextView tvTips = (TextView) contentView.findViewById(R.id.tv_message);
//        ImageView imageView = (ImageView) contentView.findViewById(R.id.img_status);
//        if (statusDrawable == R.color.transparent) {
//            imageView.setVisibility(View.GONE);
//        } else {
//            imageView.setImageResource(statusDrawable);
//            imageView.setVisibility(View.VISIBLE);
//        }
        tvTips.setText(tip);
    }

    /**
     * 显示隐藏输入框文字
     *
     * @param editText 输入框
     * @param isShow   true：显示，false：隐藏
     */
    public static void showHidePassword(EditText editText, boolean isShow) {
        if (isShow) {
            //如果选中，显示密码
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().length());
        } else {
            //否则隐藏密码
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().length());
        }
    }

    /**
     * 类型null检查
     *
     * @author zhangsan
     * @date 16/11/21 下午5:52
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException("reference is null");
            //return defaultValue;
        }
        return reference;
    }

    /**
     * 判断String是否为空
     *
     * @param str
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str) && !"null".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getString(@StringRes int resId) {
        return mAppContext.getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatObjects) {
        return mAppContext.getString(resId, formatObjects);
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /***
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mAppContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {// 如果仅仅是用来判断网络连接
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();

            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(mAppContext, resId);
    }

    /**
     * 根据比例转化实际数值为相对值
     *
     * @param gear 档位
     * @param max  最大值
     * @param curr 当前值
     * @return 相对值
     */
    public static int filtNumber(int gear, int max, int curr) {
        return curr / (max / gear);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pkName = packageInfos.get(i).packageName;
                if (pkName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 修复inputmethod 内存泄漏 在activity ondestroy调用
     *
     * @author zhangsan
     * @date 16/7/9 上午10:00
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm =
                (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext()
                            == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
//                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//                        if (QLog.isColorLevel()) {
//                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR,
// "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()
// +" dest_context=" + destContext);
//                        }
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static int dp2px(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return Math.round(px);
    }

    public static int dp2px(float dp) {
        return dp2px(mAppContext, dp);
    }

    public static float dp2pxF(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public static float sp2pxF(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            return -1;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    // 获取屏幕的大小
    public static Screen getScreenPix() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager =
                (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return new Screen(dm.widthPixels, dm.heightPixels);
    }

    public static class Screen {

        public int widthPixels;
        public int heightPixels;

        public Screen() {

        }

        public Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }

        @Override
        public String toString() {
            return "(" + widthPixels + "," + heightPixels + ")";
        }

    }

    /**
     * @description: 显示、隐藏view
     * @author: Daniel
     */
    public static void showAndHideViews(View showView, View hideView) {
        showView(showView);
        hideView(hideView);
    }

    /**
     * @description: 显示view
     * @author: Daniel
     */
    public static void showView(View v) {
        if (v != null) {
            v.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @description: 隐藏view
     * @author: Daniel
     */
    public static void hideView(View v) {
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    public static void copy(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // cmb.setText(content.trim());
        cmb.setPrimaryClip(ClipData.newPlainText("", content.trim()));
        showToast("已复制到剪贴板");
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return "v" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "无";
        }
    }

    public static int sp2px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int) (var1 * var2 + 0.5F);
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 设置消息数量及Visibility
     * @return
     */
    public static void setCountOrVisibility(TextView tv, int count){
        if(count<=0){
            tv.setVisibility(View.GONE);
        }else if(count <= 99){
            tv.setVisibility(View.VISIBLE);
            tv.setText(String.format("%s", count));
        }else {
            tv.setVisibility(View.VISIBLE);
            tv.setText("99+");
        }
    }



    /**
     * 我的起点区域到终点区域通过百度地图
     */
    public static void showBaiduMap(Context context, String startArea, String endArea){
        //mode:导航模式，可选transit（公交）、driving（驾车）、walking（步行）和riding（骑行）默认:driving
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?origin=" + startArea + "&destination=" + endArea + "&mode=driving&src=andr.物流"));
        if (isInstalled(context, "com.baidu.BaiduMap")) {
            context.startActivity(intent);
        } else {
            showToast("未安装百度地图客户端");
        }
    }

    /**
     * 高德导航
     */
    public static void setUpGaoDeAppByMine(Context context, String sname, String slat, String slon, String dname, String dlat, String dlon){
        Intent intent = new Intent();
        intent.setData(Uri.parse("amapuri://route/plan/?sourceApplication=物流&sname="+sname+"&slat="+slat+"&slon="+slon+"&dname="+dname+"&dlat="+dlat+"&dlon="+dlon+"&dev=0&m=0&t=0"));//驾车导航
        if(isInstalled(context, "com.autonavi.minimap")){
            context.startActivity(intent);
        }else {
            showToast("未安装高德地图客户端");
        }
    }

}
