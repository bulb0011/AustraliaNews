package com.ruanyun.australianews.data.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.ruanyun.australianews.util.BeanUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;


final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    //private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    //private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8");
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        String requestString = BeanUtils.toString(value).replace("+", "%2B");
        writer.write(requestString);
        writer.flush();
        writer.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());

    }
}