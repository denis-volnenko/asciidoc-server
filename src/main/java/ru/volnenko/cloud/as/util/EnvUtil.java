package ru.volnenko.cloud.as.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtil {

    public static String authPassword() {
        return value("AUTH_PASSWORD", "admin");
    }

    public static String authUsername() {
        return value("AUTH_USERNAME", "admin");
    }

    public static boolean authEnabled() {
        return value("AUTH_ENABLED", "true").equals("true");
    }

    public static boolean directoriesListed() {
        return value("DIRECTORIES_LISTED", "true").equals("true");
    }

    public static boolean redirectWelcome() {
        return value("REDIRECT_WELCOME", "false").equals("true");
    }

    @NonNull
    public static String menuLeftSize() {
        return value("MENU_LEFT_SIZE", "250");
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
