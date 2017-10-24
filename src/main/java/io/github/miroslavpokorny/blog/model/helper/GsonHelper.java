package io.github.miroslavpokorny.blog.model.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
    private static final Gson gson;

    static {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        gson = b.create();
    }

    public static Gson getGson() {
        return gson;
    }
}
