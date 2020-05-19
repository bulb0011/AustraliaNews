package com.ruanyun.australianews.data.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.ruanyun.australianews.base.ResultBase;
import com.ruanyun.australianews.util.GsonUtil;
import com.ruanyun.australianews.util.LogX;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

import java.io.IOException;


final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.adapter = adapter;
        this.gson = gson;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = "";
        try {
            tempStr = bufferedSource.readUtf8();
            //tempStr = RSAUtils.deCodeKey(bufferedSource.readUtf8());
            //tempStr= new String(Base64.decode(tempStr),"UTF-8");

            //return  adapter.fromJson(bufferedSource.readUtf8());
            return adapter.fromJson(tempStr);
//            return adapter.fromJson(value.charStream());
        } catch (Exception e) {
            LogX.d("retrofit", e.getMessage());
            return (T) GsonUtil.parseJson(tempStr, ResultBase.class);
        } finally {
            if (value != null)
                value.close();
            if (bufferedSource != null)
                bufferedSource.close();
        }


//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
//        try {
//            T code = adapter.read(jsonReader);
//            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
//                throw new JsonIOException("JSON document was not fully consumed.");
//            }
//            return code;
//        } finally {
//            value.close();
//        }

    }
}