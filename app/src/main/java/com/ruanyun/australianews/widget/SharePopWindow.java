package com.ruanyun.australianews.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.tools.utils.UIHandler;
import com.ruanyun.australianews.R;
import com.ruanyun.australianews.model.IconInfo;
import com.ruanyun.australianews.util.CommonUtil;
import com.ruanyun.australianews.util.FileUtil;
import com.ruanyun.australianews.util.LogX;
import com.ruanyun.imagepicker.base.CommonAdapter;
import com.ruanyun.imagepicker.base.CommonViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 分享 弹出框
 */
public class SharePopWindow extends PopupWindow implements PopupWindow.OnDismissListener, PlatformActionListener,
        Handler.Callback {
    private Context mContext;
    private String strings[] = {"微信", "朋友圈", "QQ", "QQ空间"};
    private int icon[] = {R.drawable.news_icon_weixin, R.drawable.news_icon_pyq,
            R.drawable.news_icon_qq, R.drawable.news_icon_qqspace};

    private GridView grid;

    int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

        public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * 朋友圈分享对象
     */
    private Platform platform_circle;

    /**
     * 微信好友分享对象
     */
    private Platform platform_wxFriend;

    /**
     * QQ空间分享对象
     */
    private Platform platform_qzone;

    /**
     * QQ好友分享对象
     */
    private Platform platform_qqFriend;

    /**
     * 新浪分享对象
     */
    private Platform platform_sina;

    /**
     * 分享的标题部分
     */
    private String share_title = "分享";

    /**
     * 分享的文字内容部分
     */
    private String share_text = "澳洲财经";

    /**
     * 分享的图片部分
     */
    private String share_image = "";
    /*本地图片地址*/
    private String image = "";
    /*分享的图片的 bitmap */
    private Bitmap share_bitmap;

    /**
     * 分享的网址部分
     */
    private String share_url;

    private ShareIconAdapter shareAdapter;

    public SharePopWindow(Context context) {
        super(context);
        this.mContext = context;
        initShareSDK();
        initView();
    }

    public SharePopWindow(Context context, String share_title, String share_text, String share_image, String share_url) {
        super(context);
        this.mContext = context;
        this.share_title = share_title;
        this.share_text = share_text;
        this.share_image = share_image;
        this.share_url = share_url;
        initShareSDK();
        initView();
    }


    private void initView() {
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color
                .transparent)));
        setOutsideTouchable(false);
        setFocusable(true);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.popupwindow_anim_style);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_share, null);
        grid = view.findViewById(R.id.grid);
        List<IconInfo> infoList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            infoList.add(new IconInfo(strings[i], icon[i]));
        }
        shareAdapter = new ShareIconAdapter(mContext, R.layout.grid_item_share, infoList);
        grid.setAdapter(shareAdapter);
        TextView tvClose = view.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOnDismissListener(this);
        // : 2016/9/30 分享点击事件
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 分享到微信好友
                        share_WxFriend();
//                        CommonUtil.showToast("请提供腾讯授权秘钥");
                        break;
                    case 1:
                        // 分享到微信朋友圈
                        share_CircleFriend();
//                        CommonUtil.showToast("请提供腾讯授权秘钥");
                        break;
                    case 2:
                        // 分享到QQ好友
                        share_QQFriend();
//                        CommonUtil.showToast("请提供腾讯授权秘钥");
                        break;
                    case 3:
                        // 分享到QQ空间
                        share_Qzone();
//                        CommonUtil.showToast("请提供腾讯授权秘钥");
                        break;
                }
                dismiss();
            }
        });
    }

    private void initShareSDK() {
        // 初始化sdk分享资源
        MobSDK.submitPolicyGrantResult(true, null);

        share_bitmap = ((BitmapDrawable)MobSDK.getContext().getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
        try {
            image = FileUtil.saveBitmapFile(share_bitmap, "icloun.jpg").getPath();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    /**
     * 分享到朋友圈
     */
    private void share_CircleFriend() {
        if (!CommonUtil.isInstalled(mContext, "com.tencent.mm")) {
            CommonUtil.showToast("请先安装微信");
            return;
        }

        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(share_title);
        sp.setText(share_text);
        if (TextUtils.isEmpty(share_image)) {
            sp.setImageData(share_bitmap);
        } else {
            sp.setImageUrl(share_image);
        }
        sp.setUrl(share_url);

        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        platform_circle.share(sp);
    }

    /**
     * 分享到微信好友
     */
    private void share_WxFriend() {
        if (!CommonUtil.isInstalled(mContext, "com.tencent.mm")) {
            CommonUtil.showToast("请先安装微信");
            return;
        }

        platform_wxFriend = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(share_title);
        sp.setText(share_text);
        sp.setUrl(share_url);
//        sp.setImageUrl(share_image);
        if (TextUtils.isEmpty(share_image)) {
            sp.setImagePath(image);
        } else {
            sp.setImageUrl(share_image);
        }

        platform_wxFriend.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        platform_wxFriend.share(sp);
    }

    /**
     * 分享到QQ空间
     */
    private void share_Qzone() {
        platform_qzone = ShareSDK.getPlatform(QZone.NAME);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(share_title);
        sp.setTitleUrl(share_url);
        sp.setSite(share_title);
        sp.setSiteUrl(share_url);
        sp.setText(share_text);
//        sp.setImageUrl(share_image);// imageUrl存在的时候，原来的imagePath将被忽略
        if (TextUtils.isEmpty(share_image)) {
            sp.setImagePath(image);
        } else {
            sp.setImageUrl(share_image);
        }
        platform_qzone.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        platform_qzone.share(sp);
    }

    /**
     * 分享到QQ好友
     */
    private void share_QQFriend() {
//        platform_qqFriend = ShareSDK.getPlatform(QQ.NAME);
//        QQ.ShareParams sp = new QQ.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(share_title);
//        sp.setTitleUrl(share_url);
//        sp.setText(share_text);
//        if (TextUtils.isEmpty(share_image)) {
//            sp.setImageData(share_bitmap);
//        } else {
//            sp.setImageUrl(share_image);
//        }
//
//        platform_qqFriend.setPlatformActionListener(this); // 设置分享事件回调
//        // 执行图文分享
//        platform_qqFriend.share(sp);

        OnekeyShare oks = new OnekeyShare();
        oks.setText(share_text);
        oks.setUrl(share_url);

        oks.setTitle(share_title);
        oks.setTitleUrl(share_url);

        if (TextUtils.isEmpty(share_image)) {
            oks.setImagePath(image);
        } else {
            oks.setImageUrl(share_image);
        }

        oks.setPlatform(QQ.NAME);
        oks.setCallback(this);
        oks.show(mContext);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        //   5.1  有效
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
        // 释放资源空间
//        ShareSDK.stopSDK(mContext);
    }

    @Override
    public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform plat, int action) {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform plat, int action, Throwable t) {

        LogX.e("dengpao","错误"+t.getMessage());

        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            // 1代表分享成功，2代表分享失败，3代表分享取消
            case 1:
//                dismiss();
                break;
            case 2:
                // 失败
                CommonUtil.showToast("分享失败");
                break;
            case 3:
                // 取消
                CommonUtil.showToast("分享取消");
                break;

        }
        return false;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_text() {
        return share_text;
    }

    public void setShare_text(String share_text) {
        this.share_text = share_text;
    }

    public String getShare_image() {
        return share_image;
    }

    public void setShare_image(String share_image) {
        this.share_image = share_image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }


    public class ShareIconAdapter extends CommonAdapter<IconInfo> {
        public ShareIconAdapter(Context context, int layoutId, List<IconInfo> datas) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(CommonViewHolder viewHolder, IconInfo iconInfo) {
            ImageView ivIcon = viewHolder.getView(R.id.iv_icon);
            ivIcon.setImageResource(iconInfo.iconRes);
            viewHolder.setText(R.id.tv_name, iconInfo.title);
        }
    }
}



