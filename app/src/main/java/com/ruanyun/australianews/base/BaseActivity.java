package com.ruanyun.australianews.base;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ruanyun.australianews.App;
import com.ruanyun.australianews.R;
import com.ruanyun.australianews.ui.login.LoginActivity;
import com.ruanyun.australianews.util.CommonUtil;
import com.ruanyun.australianews.util.LogX;
import com.ruanyun.australianews.util.PixelSizeUtil;
import com.ruanyun.australianews.util.SharedPreferencesUtils;
import com.ruanyun.australianews.widget.LoadingDialog;
import com.ruanyun.australianews.widget.TopBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import me.jessyan.autosize.AutoSize;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Description:
 * author: zhangsan on 16/11/21 上午9:28.
 */

public abstract class BaseActivity extends DaggerAppCompatActivity implements TipCommonMvpView, TopBar.onTopBarClickListener{

    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    protected LoadingDialog loadingDialog;
    protected CompositeSubscription mCompositeSubscription;
    @Inject
    protected App app;
//    private ActivityComponent activityComponent;

    protected <T extends View> T getView(int viewId) {
        return (T) findViewById(viewId);
    }

    protected <T extends View> T getViewFromLayout(@LayoutRes int layout) {
        return getViewFromLayout(layout, null, false);
    }

    protected <T extends View> T getViewFromLayout(@LayoutRes int layout, ViewGroup root, boolean attach) {
        return (T) LayoutInflater.from(mContext).inflate(layout, root, attach);
    }

    protected <T extends View> T getViewFromLayout(@LayoutRes int layout, @IdRes int viewId) {
        View v = LayoutInflater.from(mContext).inflate(layout, null, false);
        return (T) v.findViewById(viewId);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    Locale locale;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LogX.e("retrofit", "onCreate: "+TAG);
//        initInjector();
        mContext = this;
        app.pushActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        changeAppLanguage();
    }

    public void changeAppLanguage( ) {

        String yuyan= (String) SharedPreferencesUtils.getParam(App.context, SharedPreferencesUtils.KEY_SYSTEM_LANGUAGE,"");


        if (!TextUtils.isEmpty(yuyan)) {
            Resources resources = getContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();

            if ("zh".equals(yuyan)){
                config.locale = Locale.CHINESE;
                LogX.e("dengpao","中文"+yuyan);
            }
            else{
                // 应用用户选择语言
                config.locale = Locale.ENGLISH;
                LogX.e("dengpao","英文"+yuyan);
            }

            LogX.e("dengpao","语言"+yuyan);
            resources.updateConfiguration(config, dm);
        }
    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBarWhite();
    }

    //    public ActivityComponent getComponent() {
//        return activityComponent;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.popActivity(this);
        mContext = null;
        unSubscribe();
    }

    public void autoSize() {
        AutoSize.autoConvertDensity(this, PixelSizeUtil.getAndroidScreenWidth(mContext), true);
    }


    /**
     *  需要在 setContentView()之后调用
     *  当状态栏颜色为白色的，调用此方法
     */
    protected void setStatusBarWhite(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarUpperAPI21(R.color.white);
            statusBarTextColorBlack();
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setStatusBarUpperAPI21(R.color.transparent_black);
        }else {
            setStatusBarUpperAPI19To20(R.color.transparent_black);
        }
    }

    /**
     * 全屏模式适合在没有topbar 或 有topbar但背景不是白色 时使用
     * 当背景是白色时，21以下没法更改状态栏文字颜色为黑色 需调用 setStatusBarWhite()
     */
    protected void immerse() {
        //沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }
//        SoftHideKeyBoardUtil.assistActivity(this);
    }

    /**
     * 设置状态栏颜色为深色
     */
    public void statusBarTextColorBlack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 思路:直接设置状态栏的颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarUpperAPI21(@ColorRes int id) {
        Window window = getWindow();
        //取消设置悬浮透明状态栏,ContentView便不会进入状态栏的下方了
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置状态栏颜色
        window.setStatusBarColor(ContextCompat.getColor(mContext, id));
    }

    /**
     * 思路:设置状态栏悬浮透明，然后制造一个和状态栏等尺寸的View设置好颜色填进去，就好像是状态栏着色了一样
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setStatusBarUpperAPI19To20(@ColorRes int id) {
        immerse();
        ViewGroup mContentView = (ViewGroup) findViewById(android.R.id.content);
        int statusBarHeight = PixelSizeUtil.getStatusHeight(mContext);
        int statusColor = ContextCompat.getColor(mContext, id);

        View mTopView = mContentView.getChildAt(0);
        if (mTopView != null) {
            //制造一个和状态栏等尺寸的 View
            View mStatusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            mStatusBarView.setBackgroundColor(statusColor);
            //将view添加到第一个位置
            mContentView.addView(mStatusBarView, 0, lp);

            FrameLayout.LayoutParams topViewLp = (FrameLayout.LayoutParams) mTopView.getLayoutParams();
            topViewLp.setMargins(0, statusBarHeight, 0, 0);
        }
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

//    protected void initInjector() {
//        activityComponent = DaggerActivityComponent.builder()
//                .applicationComponent(App.getInstance().getAppCoponent())
//                .activityModule(new ActivityModule(this)).build();
//        activityComponent.inject(this);
//    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 关闭软键盘
     */
    public void closeSoftInputFromWindow() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public int getCompatColor(int colorId){
        return ContextCompat.getColor(mContext, colorId);
    }

    /**
     * show to @param(cls)
     */
    public void showActivity(Intent it) {
        startActivity(it);
    }

    /**
     * show to @param(cls)
     */
    public void showActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(extras);
        startActivity(intent);
    }


    /**
     * show to @param(cls)
     */
    public void showActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    /**
     * skip to @param(cls), and this finish() method
     */
    public void skipActivity(Class<?> cls) {
        showActivity(cls);
        finish();
    }

    /**
     * skip to @param(cls), and this finish() method
     */
    public void skipActivity(Intent it) {
        showActivity(it);
        finish();
    }

    /**
     * 启动包名的    activity
     *
     * @param clasName
     */
    public void showActivity(String packageName, String clasName) {
        Intent i = new Intent();
//        i.setComponent(new ComponentName(getPackageName(), clasName));
        i.setClassName(packageName, clasName);
        startActivity(i);
    }

    public void showActivity(String clasName) {
        Intent i = new Intent();
//        i.setComponent(new ComponentName(getPackageName(), clasName));
        i.setClassName(mContext, clasName);
        startActivity(i);
    }

    public void copyText(String text) {
        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, text));
        showToast("复制成功");
    }


    protected void showLoading() {
        showLoading("加载中...");
    }

    protected void showLoading(String msg) {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTvMessage(msg);
        loadingDialog.show();
    }


    protected float getListFloat(List<? extends Object> list){
        if(list.size()>7){
            return 7.5f;
        }else {
            return list.size();
        }
    }

    protected void disMissLoading() {
        loadingDialog.dismiss();
    }

    protected void registerBus() {
        EventBus.getDefault().register(this);
    }

    protected void unRegisterBus() {
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void showToast(String msg) {
        CommonUtil.showToast(msg);
    }

    public void showToast(@StringRes int tip) {
        showToast(mContext.getResources().getString(tip));
    }
    @Override
    public void showLoadingView(String msg) {
        showLoading(msg);
    }

    @Override
    public void showLoadingView(int msg) {
        showLoading(getString(msg));
    }

    @Override
    public void disMissLoadingView() {
        disMissLoading();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onTopBarViewClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_left_img:
                onTopBarLeftImgClick();
                break;
            case R.id.topbar_title:
                onTopBarTitleClick();
                break;
            case R.id.topbar_right_img:
                onTopBarRightImgClick();
                break;
            case R.id.topbar_right_text:
                onTopBarRightTextClick();
                break;
            default:
                break;
        }
    }

    /**
     * topbar 左边imagebtn 点击响应
     */
    public void onTopBarLeftImgClick() {
        finish();
    }

    /**
     * topbar 右边imagebtn 点击响应
     */
    public void onTopBarRightImgClick() {

    }

    /**
     * desc: 右边TextView点击响应
     **/
    public void onTopBarRightTextClick(){

    }

    /**
     * topbar 中间 文本 点击响应
     */
    public void onTopBarTitleClick() {

    }

    public boolean isLoginToActivity(){
        if(app.getUser()!=null){
            return true;
        }else {
            LoginActivity.Companion.start(mContext);
            return false;
        }
    }

    /**
     * 未登录跳转登录，登录以后判断是否可以发布生活服务
     * @return
     */
    public boolean isLoginToActivityByIsRelease(){
        if(app.getUser()!=null){
            if("1".equals(app.getUser().isLiveService)){
                return true;
            }else {
                showToast("暂时无法发布生活服务");
                return false;
            }
        }else {
            LoginActivity.Companion.start(mContext);
            return false;
        }
    }
}
