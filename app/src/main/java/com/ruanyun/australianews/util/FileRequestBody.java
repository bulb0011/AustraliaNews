package com.ruanyun.australianews.util;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 * author: zhangsan on 16/11/29 上午11:04.
 */
public class FileRequestBody extends RequestBody {
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");
    public static final MediaType MEDIA_TYPE_FILE = MediaType.parse("application/octet-stream");

    public static final int TYPE_IMAGE = 11;
    public static final int TYPE_FILE = 12;
    private int mediaType = TYPE_FILE;
    private File contentFile;

    public FileRequestBody(int mediaType, File file) {
        this.mediaType = mediaType;
        this.contentFile=file;
    }

    public FileRequestBody(int mediaType, String filePath) {
       new FileRequestBody(mediaType,new File(filePath));
    }

    @Override
    public long contentLength() throws IOException {
        return contentFile!=null&&contentFile.exists()?contentFile.length():0;
    }

    @Override
    public MediaType contentType() {
        if (mediaType == TYPE_FILE) {
            return MEDIA_TYPE_FILE;
        } else if (mediaType == TYPE_IMAGE) {
            return MEDIA_TYPE_IMAGE;
        }
        return MEDIA_TYPE_FILE;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(contentFile);
            sink.writeAll(source);
        } finally {
            Util.closeQuietly(source);
        }
    }

}
