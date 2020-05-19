package com.ruanyun.imagepicker.compressimage;

import java.io.File;

public  class CompressTaskResult implements CompressImageInfoGetter {
        public String tag;
        public File imageFile;
        public String paramsName;

        @Override
        public void setParamsName(String paramsName) {
            this.paramsName = paramsName;
        }

        @Override
        public String imageFilePath() {
            return imageFile.getPath();
        }

        @Override
        public String imageFileName() {
            return imageFile.getName();
        }

        @Override
        public String requestPramsName() {
            return paramsName;
        }
    }