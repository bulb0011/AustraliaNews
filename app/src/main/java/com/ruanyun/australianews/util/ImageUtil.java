package com.ruanyun.australianews.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ruanyun.australianews.App;
import com.ruanyun.australianews.R;
import com.ruanyun.australianews.widget.CircleTransform;

import java.io.File;
import java.math.BigDecimal;

import static com.bumptech.glide.request.RequestOptions.errorOf;
import static com.bumptech.glide.request.RequestOptions.overrideOf;


/**
 * Description:图片加载工具
 * author: zhangsan on 16/11/21 下午5:35.
 */

public class ImageUtil {

    public static void loadUriImage(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    public static void loadFileImage(Context context, ImageView imageView, File file) {
        Glide.with(context)
                .load(file)
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, Object obj) {
        Glide.with(context)
                .load(obj)
                .apply(errorOf(R.drawable.default_img)
                )
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .apply(errorOf(R.drawable.default_img)
                )
                .into(imageView);
    }
    public static void loadImagePE(Context context, ImageView imageView, String url) {
        loadImage(context, imageView, url, R.drawable.default_img, R.drawable.default_img);
    }

    public static void loadImage(Context context, ImageView imageView, String url, @DrawableRes int errorResId) {
        Glide.with(context)
                .load(url)
                .apply(errorOf(errorResId)
                )
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, String url, @DrawableRes int placeHolder, @DrawableRes int errorResId) {
        Glide.with(context)
                .load(url)
                .apply(errorOf(errorResId).placeholder(placeHolder)
                )
                .into(imageView);
    }
    public static void loadCircleImage(Context context, ImageView imageView, String url, @DrawableRes int placeHolder){
        Glide.with(context)
                .load(url)
                .apply(errorOf(placeHolder)
                        .placeholder(placeHolder)
                        .transform(new CircleTransform())
                )
                .into(imageView);
    }

    public static void loadRoundedCornersImage(Context context, ImageView imageView, Object url, int roundingRadius, int width, int height){
//        MultiTransformation<Bitmap> multi = new MultiTransformation<>(
//                new CropTransformation(width, height, CropTransformation.CropType.CENTER),
//                new RoundedCornersTransformation(roundingRadius, 0, RoundedCornersTransformation.CornerType.ALL));
//.apply(bitmapTransform(multi))
        if (url instanceof String || url instanceof File || url instanceof Drawable) {
            Glide.with(context).load(url).apply(overrideOf(width, height).transforms(new CenterCrop(), new RoundedCorners(roundingRadius))).into(imageView);
        } else {
            LogX.d("Glide", "loadRoundedCornersImage() called with: context = [" + context + "], imageView = [" + imageView + "], url = [" + url + "], roundingRadius = [" + roundingRadius + "], width = [" + width + "], height = [" + height + "]");
        }
    }

    /**
     * 清除图片所有缓存
     */
    public static void clearImageAllCache() {
        clearImageDiskCache();
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clearCache();
                    }
                }).start();
            } else {
                clearCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clearCache() {
        Glide.get(App.getInstance()).clearDiskCache();
        EventNotifier.getInstance().clearImageCache();
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(Glide.getPhotoCacheDir(App.getInstance())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }


}
