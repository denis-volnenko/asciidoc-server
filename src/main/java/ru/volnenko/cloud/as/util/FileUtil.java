package ru.volnenko.cloud.as.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class FileUtil {

    @NonNull
    public static String prepare(@NonNull final String value) {
        return value
                .replaceAll("//", "")
                .replaceAll("^/", "")
                .replaceAll("\\../", "")
                .replaceAll("\\./", "");
    }

}
