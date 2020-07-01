package com.ruanyun.australianews.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Description:
 * author: zhangsan on 17/1/10 上午10:36.
 */

public class GsonUtil {
    static Gson gson;

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return getGson().fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  String toJson(Object t) {
        return getGson().toJson(t);
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        return getGson().fromJson(json, new ParameterizedTypeImpl(List.class, new Type[]{clazz}));
    }

    private static Gson getGson() {
        if (null == gson) {
            gson =new Gson();
        }
        return gson;
    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private Class raw;
        private Type[] types;

        public ParameterizedTypeImpl(Class raw, Type[] types) {
            this.raw = raw;
            this.types = types;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return types;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public Type getRawType() {
            return raw;
        }
    }

}
