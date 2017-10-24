package io.github.miroslavpokorny.blog.model.helper;

import java.util.List;

public class ListHelper {
    public static <T> List<T> listToGeneric(List list) {
        @SuppressWarnings("unchecked")
        List<T> localList = list;
        return localList;
    }
}
