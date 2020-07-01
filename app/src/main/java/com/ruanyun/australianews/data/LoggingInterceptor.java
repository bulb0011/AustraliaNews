package com.ruanyun.australianews.data;

import com.ruanyun.australianews.util.LogX;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "retrofit";
    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
      /*  Request newRequest=request.newBuilder()
                .url(request.url())
                .method(request.method(),request.body())
                .header("Accept","application/json").build();*/
        // request.headers().
        // LogX.d(TAG,"header"+request.headers().toString());
        // LogX.d(TAG,"method"+request.method());
        //  LogX.d(TAG,"method"+request.body().contentType());
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        MediaType contentType = null;
        String bodyString = "";
        if (response!=null&&response.body() != null) {
            contentType = response.body().contentType();
            //Response response1=response;
            bodyString = response.body().string();
            //bodyString = RSAUtils.deCodeKey(response.body().string());
            //bodyString=RSAUtils.deCodeKey(bodyString);
        }

        double time = (t2 - t1) / 1e6d;

        if ("GET".equals(request.method())) {
            LogX.d(TAG, String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
        } else if ("POST".equals(request.method())) {
//            LogX.d(TAG, String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), commonTime, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
            Log(String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
           // LogX.d(TAG, String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), commonTime, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
//            Log(String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), commonTime, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
        } else if ("PUT".equals(request.method())) {
            LogX.d(TAG, String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), request.body().toString(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
        } else if ("DELETE".equals(request.method())) {
            LogX.d(TAG, String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), response.code(), response.headers()));
        }

        if (response.body() != null) {
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }

   /* private void log(String msgContent){
        if(msgContent.length()>4000){

        }else {
            LogX.d(TAG,msgContent);
        }
    }*/

    public static void Log(String str) {
        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String sub;
        while (index < str.length()) {
            // java的字符不允许指定超过总的长度end
            if (str.length() <= index + maxLength) {
                sub = str.substring(index);
            } else {
                sub = str.substring(index, maxLength+index);
            }

            index += maxLength;
            LogX.d(TAG,sub.trim());
        }
    }



    private static String stringifyRequestBody(Request request) {
        try {
            if (request.body().contentType()!=null&&request.body().contentType().toString().contains("multipart")) {
                StringBuilder content=new StringBuilder("");
                MultipartBody multipartBody= (MultipartBody) request.body();
                if(multipartBody.parts().size()>0){
                    for(MultipartBody.Part part:multipartBody.parts()){
                        try {
                            Class<?> _c=part.getClass();
                            Field headerField= _c.getDeclaredField("headers");
                            Field bodyField=_c.getDeclaredField("body");
                            bodyField.setAccessible(true);
                            headerField.setAccessible(true);
                            Headers headers= (Headers) headerField.get(part);
                            RequestBody body= (RequestBody) bodyField.get(part);
                            // msgContent.append(headers.toString());
                            // msgContent.append("Content-Disposition: ");
                            content.append(headers.get("Content-Disposition"));
                            if(body.contentType()!=null&& "text".equals(body.contentType().type())){
                                final Buffer bufferString = new Buffer();
                                body.writeTo(bufferString);
                                content.append(" value:");
                                content.append(bufferString.readUtf8());
                                content.append("\n");
                            }

                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                }

                return content.toString();
            }
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

   public String stringifyResponseBody(String responseBody) throws UnsupportedEncodingException {
//        return URLDecoder.decode(RSAUtils.deCodeKey(responseBody),"utf-8");
       //return responseBody;
       return  responseBody;
       // return new String(Base64.decode(RSAUtils.deCodeKey(responseBody)),"utf-8");
    }
}
