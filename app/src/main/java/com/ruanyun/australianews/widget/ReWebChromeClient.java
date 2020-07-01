package com.ruanyun.australianews.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.ruanyun.australianews.util.CommonUtil;
import com.ruanyun.australianews.util.PixelSizeUtil;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import me.jessyan.autosize.AutoSize;

/**
 * Created by hdl on 2017/7/13.
 */

public class ReWebChromeClient extends WebChromeClient {


    private OpenFileChooserCallBack mOpenFileChooserCallBack;


    public ReWebChromeClient(OpenFileChooserCallBack openFileChooserCallBack) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
    }


    //For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
    }

//    // For Android  > 4.1.1
//    @Override
//    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//        openFileChooser(uploadMsg, acceptType);
//    }


    // For Android 5.0+
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        mOpenFileChooserCallBack.showFileChooserCallBack(filePathCallback);
        return true;
    }

    @Override
    public boolean onJsAlert(WebView webView, String url, String message, final JsResult jsResult) {
        CommonUtil.showToast(message);
        jsResult.confirm();
//        final AlertDialog dlg = new AlertDialog.Builder(webView.getContext()).setTitle(null)
//                .setMessage(message).setPositiveButton("确定", new DialogInterface
//                        .OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        jsResult.confirm();
//                        dialog.dismiss();
//                    }
//                }).create();
//        AutoSize.autoConvertDensity((Activity) webView.getContext(), PixelSizeUtil.getAndroidScreenWidth(webView.getContext()), true);
//        dlg.show();
//        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                jsResult.cancel();
//            }
//        });
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView webView, String url, String message, final JsResult result) {
        final AlertDialog dlg = new AlertDialog.Builder(webView.getContext()).setTitle("提示")
                .setMessage(message).setPositiveButton("确定", new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        result.confirm();
                    }
                })
                .setNegativeButton("取消", null).create();
        AutoSize.autoConvertDensity((Activity) webView.getContext(), PixelSizeUtil.getAndroidScreenWidth(webView.getContext()), true);
        dlg.show();
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                result.cancel();
            }
        });
        return true;
    }

    public interface OpenFileChooserCallBack {
        void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);


        void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback);
    }

}
