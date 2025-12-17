package ru.volnenko.cloud.as.dto;

import lombok.NonNull;

import java.util.Map;

public final class Root {

    @NonNull
    public static Root env() {
        return new Root().load();
    }

    private final Menu left = new Menu("MENU_LEFT");

    private final Menu main = new Menu("MENU_MAIN");

    public Menu left() {
        return left;
    }

    public Menu main() {
        return main;
    }

    public Root load() {
        main.load();
        left.load();
        return this;
    }

    public void load(@NonNull final Map<String, String> map) {
        main.load(map);
        left.load(map);
    }

    public String toEnv() {
        final StringBuilder sb = new StringBuilder();
        sb.append(main.toEnv()).append(left.toEnv());
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(main).append(left);
        return sb.toString();
    }
}
