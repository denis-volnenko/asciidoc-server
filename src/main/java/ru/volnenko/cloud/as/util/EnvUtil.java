package ru.volnenko.cloud.as.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtil {

    @NonNull
    public static String caption() {
        return value("CAPTION", "ASCIIDOC");
    }

    @NonNull
    public static String footer() {
        return value("FOOTER", "");
    }

    @NonNull
    private static String value(@NonNull final String key, @NonNull final String value) {
        if (key.isEmpty()) return value;
        final String v = System.getenv(key);
        if (v == null || v.isEmpty()) return value;
        return v;
    }

}
