package ru.volnenko.cloud.as.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;
import java.util.regex.Pattern;

public final class Menu {

    private String name = "menu";

    public Menu(String name) {
        this.name = name;
    }

    @Getter
    @Setter
    private List<MenuItem> items = new ArrayList<>();

    public void load() {
        load(System.getenv());
    }

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private boolean isNumeric(final String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public void load(@NonNull final Map<String, String> map) {
        items.clear();
        items.addAll(items(map));
    }

    @NonNull
    private List<MenuItem> items(@NonNull final Map<String,String> map) {
        @NonNull final List<MenuItem> result = new ArrayList<>();
        @NonNull final Set<String> keys = keys(map);
        for (@NonNull final String key: keys) {
            final String menuKeyName = name + "[" + key + "]_NAME";
            if (!map.containsKey(menuKeyName)) continue;
            final String menuKeyLink = name + "[" + key + "]_LINK";
            if (!map.containsKey(menuKeyLink)) continue;

            final String name = map.get(menuKeyName);
            if (name == null || name.isEmpty()) continue;
            final String link = map.get(menuKeyLink);
            if (name == link || name.isEmpty()) continue;

            final MenuItem menuItem = new MenuItem();
            menuItem.setName(name);
            menuItem.setLink(link);

            result.add(menuItem);
        }
        return result;
    }

    @NonNull
    private Set<String> keys(@NonNull final Map<String,String> map) {
        @NonNull final Set<String> keys = new LinkedHashSet<>();
        for (final String key: map.keySet()) {
            if (key == null) continue;
            if (!key.startsWith(name)) continue;
            @NonNull final String pk = key
                    .replaceAll(name + "\\[","")
                    .replaceAll("\\]_NAME", "")
                    .replaceAll("\\]_LINK", "");
            if (!isNumeric(pk)) continue;
            keys.add(pk);
        }
        return keys;
    }

    public void add(@NonNull final String name, @NonNull final String link) {
        @NonNull final MenuItem item = new MenuItem();
        item.setName(name);
        item.setLink(link);
        items.add(item);
    }

    @NonNull
    public String toEnv() {
        @NonNull final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (@NonNull final MenuItem item: items) {
            sb.append(name).append("[").append(i).append("]").append("_").append("NAME").append("=").append(item.getName()).append(";");
            sb.append(name).append("[").append(i).append("]").append("_").append("LINK").append("=").append(item.getLink()).append(";");
            i++;
        }
        return sb.toString();
    }

    @NonNull
    @Override
    public String toString() {
        @NonNull final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (@NonNull final MenuItem item: items) {
            sb.append(name).append("[").append(i).append("]").append("_").append("NAME").append("=").append(item.getName()).append("\n\r");
            sb.append(name).append("[").append(i).append("]").append("_").append("LINK").append("=").append(item.getLink()).append("\n\r");
            i++;
        }
        return sb.toString();
    }

}
