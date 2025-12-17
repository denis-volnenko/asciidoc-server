package ru.volnenko.cloud.as;

import org.junit.Test;
import ru.volnenko.cloud.as.dto.Root;

import java.util.LinkedHashMap;
import java.util.Map;


public class RootTest {

    @Test
    public void test() {
        final Root root = new Root();
        root.main().add("MAIN", "/");
        root.main().add("CONTACTS", "/contact");
        root.left().add("ROOT", "/index.adoc");
        root.left().add("AUTH", "/auth");
    }

    @Test
    public void testLoadMap() {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("MENU_MAIN[0]_NAME", "MAIN");
        map.put("MENU_MAIN[0]_LINK", "/");
        map.put("MENU_MAIN[1]_NAME", "CONTACTS");
        map.put("MENU_MAIN[1]_LINK", "/contacts");

        map.put("MENU_LEFT[0]_NAME", "ROOT");
        map.put("MENU_LEFT[0]_LINK", "/index.adoc");
        map.put("MENU_LEFT[1]_NAME", "AUTH");
        map.put("MENU_LEFT[1]_LINK", "/auth");

        final Root root = new Root();
        root.load(map);
    }

    @Test
    public void testENV() {
        final Root root = new Root();
        root.load();
    }

}
