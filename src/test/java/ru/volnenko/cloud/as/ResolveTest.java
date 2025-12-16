package ru.volnenko.cloud.as;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ResolveTest {

    public static void main(String[] args) {
        System.out.println(Paths.get("").toAbsolutePath());
        System.out.println(Paths.get("").resolve("index.adoc").toAbsolutePath());
        System.out.println(Paths.get("").resolve("/index.adoc").toAbsolutePath());
        System.out.println(Paths.get("").resolve("./index.adoc").toAbsolutePath());
        System.out.println(Paths.get("").resolve("./index.adoc").normalize().toAbsolutePath());
        System.out.println(Paths.get("").resolve("./../index.adoc").normalize().toAbsolutePath());
        System.out.println(Paths.get("").resolve("./foo/../index.adoc").normalize().toAbsolutePath());
    }

    @Test
    public void test() {
        System.out.println(ClassLoader.getSystemResourceAsStream("header.html"));
    }

}
