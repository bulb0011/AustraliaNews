/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.ruanyun.imagepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.File;


/**
 * <b>desc your class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class Util {

    /**
     * Convert a dp float value to pixels
     * @param context
     * @param dp
     * @return the responsive pixels
     */
    public static int dp2px(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return Math.round(px);
    }
    public static int px2dp(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int)(var1 / var2 + 0.5F);
    }

    public static int sp2px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int)(var1 * var2 + 0.5F);
    }

    public static int px2sp(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int)(var1 / var2 + 0.5F);
    }
    public static boolean isStorageEnable(){
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
          //  L.d("TestFile", "SD card is not available/writable right now.");
            return false;
        }
        return true;
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    private Util() {
        throw new AssertionError("No Instances");
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    private static String uriAuthority = "";

    public static String getUriAuthority() {
        if (TextUtils.isEmpty(uriAuthority)) {
            throw new NullPointerException("imagePicker  必须设置 Util.setUriAuthority(app) 方法");
        }
        return uriAuthority;
    }

    /**
     *
     * @param context
     */
    public static void setUriAuthority(Context context) {
        uriAuthority = (context.getPackageName() + ".fileprovider");
    }

    /**
     * @param ctx
     * @param photoFile
     * @return
     */
    public static Uri getUriForFile(Context ctx, File photoFile) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = ImagePickerFileProvider.getUriForFile(ctx, getUriAuthority(), photoFile);
        } else {
            uri = Uri.fromFile(photoFile);
        }
        return uri;
    }

}
