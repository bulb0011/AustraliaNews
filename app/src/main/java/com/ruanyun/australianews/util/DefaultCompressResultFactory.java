package com.ruanyun.australianews.util;

import com.ruanyun.imagepicker.compressimage.CompressImageInfoGetter;
import com.ruanyun.imagepicker.compressimage.CompressImageResultAdapter;
import okhttp3.RequestBody;

import java.io.File;
import java.util.HashMap;

/**
 * Description:
 * author: zhangsan on 16/11/29 上午10:57.
 */
public final class DefaultCompressResultFactory extends CompressImageResultAdapter.Factory {

    @Override
    public CompressImageResultAdapter<HashMap<String, RequestBody>> creat() {
        return new CompressImageResultAdapter<HashMap<String, RequestBody>>() {

            @Override
            public HashMap<String, RequestBody> convert(CompressImageInfoGetter... compressImageInfoGetters) {
                HashMap<String, RequestBody> resultMap = new HashMap<>();
                for (CompressImageInfoGetter getter : compressImageInfoGetters) {
                    FileRequestBody requestBody = new FileRequestBody(FileRequestBody.TYPE_IMAGE, new File(getter.imageFilePath()));
                    resultMap.put(getImageMdStr(getter.requestPramsName(), getter.imageFileName()), requestBody);
                }

                return resultMap;
            }
        };
    }

    private String getImageMdStr(String paramsName, String fileName) {
        //文件名是中文时，上传报错,此处调整为MD5
        int index = fileName.lastIndexOf(".");
        if(index!=-1){
            fileName = MD5.md5(fileName) + fileName.substring(index);
        }
        StringBuilder sb = new StringBuilder(paramsName)
                .append("\";filename=\"")
                .append(fileName);
        return sb.toString();
    }
}
