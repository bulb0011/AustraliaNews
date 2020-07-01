package com.ruanyun.imagepicker.imagelist;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hdl on 2017/7/29.
 */

public class ImageListUtil {

    public static final String SHOW_IMAGES_DATAS_TYPES = "show_images_datas_types";
    public static final String SHOW_IMAGES_SELECT_POSTION = "show_images_select_postion";
    public static final String IMAGE_URLS = "image_urls";

    /**
     * 显示多张大图
     * @param context
     * @param datas
     * @param currentPic
     * @param <T>
     */
    public static <T extends ImageUrlGetter> void showBigImages(Context context, ArrayList<T> datas, int currentPic) {
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra(SHOW_IMAGES_DATAS_TYPES, ShowImagesActivity.TYPE_GETTERS);
        intent.putExtra(SHOW_IMAGES_SELECT_POSTION, currentPic);
        intent.putParcelableArrayListExtra(IMAGE_URLS, datas);
        context.startActivity(intent);
    }

    /**
     * 显示单张大图
     * @param context
     * @param path
     * @param <T>
     */
    public static <T extends ImageUrlGetter> void showBigImage(Context context, final String path) {
        ArrayList<String> datas = new ArrayList<>();
        datas.add(path);
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra(SHOW_IMAGES_DATAS_TYPES, ShowImagesActivity.TYPE_STRINGS);
        intent.putStringArrayListExtra(IMAGE_URLS, datas);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param datas
     * @param currentPic
     */
    public static void showBigImagesWithStringList(Context context, ArrayList<String> datas, int currentPic) {
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra(SHOW_IMAGES_DATAS_TYPES, ShowImagesActivity.TYPE_STRINGS);
        intent.putStringArrayListExtra(IMAGE_URLS, datas);
        intent.putExtra(SHOW_IMAGES_SELECT_POSTION, currentPic);
        context.startActivity(intent);
    }


}
