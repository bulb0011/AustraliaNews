package jiguang.chat.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static final String DEF_FILEPATH = "/XhsEmoticonsKeyboard/Emoticons/";

//    public static String API_URL = "http://192.168.1.65:8080/";//龙
    public static String API_URL = "http://120.55.57.203:8080/";//线上

    public static final String IMG_URL = "http://api.app.localau.com/";//图片地址


    public static String getFolderPath(String folder) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + DEF_FILEPATH + folder;
    }

    public static void unzip(InputStream is, String dir) throws IOException {
        File dest = new File(dir);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        if (!dest.isDirectory())
            throw new IOException("Invalid Unzip destination " + dest);
        if (null == is) {
            throw new IOException("InputStream is null");
        }

        ZipInputStream zip = new ZipInputStream(is);

        ZipEntry ze;
        while ((ze = zip.getNextEntry()) != null) {
            final String path = dest.getAbsolutePath()
                    + File.separator + ze.getName();

            String zeName = ze.getName();
            char cTail = zeName.charAt(zeName.length() - 1);
            if (cTail == File.separatorChar) {
                File file = new File(path);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new IOException("Unable to create folder " + file);
                    }
                }
                continue;
            }

            FileOutputStream fout = new FileOutputStream(path);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = zip.read(bytes)) != -1) {
                fout.write(bytes, 0, c);
            }
            zip.closeEntry();
            fout.close();
        }
    }

    public static String getFileSize(Number fileSize) {
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        //保留小数点后两位
        ddf1.setMaximumFractionDigits(2);
        double size = fileSize.doubleValue();
        String sizeDisplay;
        if (size> 1048576.0) {
            double result = size / 1048576.0;
            sizeDisplay = ddf1.format(result) + " MB";
        } else if (size > 1024) {
            double result = size/ 1024;
            sizeDisplay = ddf1.format(result) + " KB";

        } else {
            sizeDisplay = ddf1.format(size) + " B";
        }
        return sizeDisplay;
    }

    public static String getFileSize(long size) {
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        //保留小数点后两位
        ddf1.setMaximumFractionDigits(2);
        String sizeDisplay;
        if (size > 1048576.0) {
            double result = size / 1048576.0;
            sizeDisplay = ddf1.format(result);
        } else if (size > 1024) {
            double result = size / 1024f;
            sizeDisplay = ddf1.format(result);

        } else {
            sizeDisplay = ddf1.format(size);
        }
        return sizeDisplay;
    }

    public static void loadAvatar(Context context, String url, ImageView imageView, @DrawableRes int resourceId){
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = String.format("%s%s", IMG_URL, url);
        }
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .transform(new CircleTransform())
                        .placeholder(resourceId)
                        .error(resourceId))
                .into(imageView);
    }

}

