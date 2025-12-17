package ru.volnenko.cloud.as.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtil {

    @NonNull
    public static String minioEndpoint() {
        return value("MINIO_ENDPOINT", "http://localhost:9000");
    }

    @NonNull
    public static String minioAccessKey() {
        return value("MINIO_ACCESS_KEY", "minio");
    }

    @NonNull
    public static String minioSecretKey() {
        return value("MINIO_SECRET_KEY", "minio");
    }

    @NonNull
    public static String minioBucket() {
        return value("MINIO_BUCKET", "minio");
    }

    @NonNull
    public static String menuLeftSize() {
        return value("MENU_LEFT_SIZE", "250");
    }

    @NonNull
    public static String storageType() {
        return value("STORAGE_TYPE", "FILE");
    }

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
