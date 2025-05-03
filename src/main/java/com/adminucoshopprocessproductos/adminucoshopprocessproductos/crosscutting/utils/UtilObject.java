package com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils;

public class UtilObject {
    private static final UtilObject instance = new UtilObject();

    private UtilObject() {
        super();
    }

    public static final UtilObject getUtilObject() {
        return instance;
    }

    public final <T> boolean isNUll(T object) {
        return object == null;
    }

    public final <T> T getDefault(final T object, final T defaultObject) {
        return isNUll(object) ? defaultObject : object;
    }

}
