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

package com.ruanyun.imagepicker.bean;

import com.ruanyun.imagepicker.compressimage.CompressImageInfoGetter;

import java.io.Serializable;

/**
 * Created by Eason.Lai on 2015/11/1 10:42 contact：easonline7@gmail.com
 */
public class ImageItem implements Serializable, CompressImageInfoGetter {
    public static final int TYPE_LOCAL = 123;
    public static final int TYPE_REMOTE = 321;
    public String path;
    public String name;
    public int resId;
    public long time;
    public String tag;
    public int type = TYPE_LOCAL;
    public boolean isAdd = false;
    public String paramsName;

    public ImageItem() {
    }

    public ImageItem(String path) {
        this.path = path;
    }

    public ImageItem(String name, String path, long time) {
        this.name = name;
        this.path = path;
        this.time = time;
    }
    public ImageItem(String name, String path, int type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.time == other.time;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    @Override
    public void setParamsName(String paramsName) {
        this.paramsName = paramsName;
    }

    @Override
    public String imageFilePath() {
        return path;
    }

    @Override
    public String imageFileName() {
        return name;
    }

    @Override
    public String requestPramsName() {
        return paramsName;
    }
}
