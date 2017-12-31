package com.visucius.secp.util;

import java.util.Collection;

public class Util {
    public static <E> void addAllIfNotNull(Collection<E> list, Collection<? extends E> c) {
        if (c != null) {
            list.addAll(c);
        }
    }
}
