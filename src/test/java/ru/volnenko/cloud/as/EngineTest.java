package ru.volnenko.cloud.as;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EngineTest {

    public final static String asciidocSource =
        """
        =Document Name
        :toc:
        
        == Первая глава
        
        [cols="20,80"]
        |===
        
        |213
        |213
        
        |===
        
        == Second Part
        """;
    public static void main(String[] args) {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        Options options = OptionsBuilder.options()
                .backend("html5")
                .safe(SafeMode.UNSAFE).build(); // Or SafeMode.SAFE, SafeMode.SERVER, etc.

        final String htmlOutput =
                """
                        <!DOCTYPE html>
                        <html lang="ru">
                        <head>
                            <meta charset="UTF-8">
                            <meta http-equiv="X-UA-Compatible" content="IE=edge">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <meta name="generator" content="Asciidoctor 2.0.20">
                            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,300italic,400,400italic,600,600italic%7CNoto+Serif:400,400italic,700,700italic%7CDroid+Sans+Mono:400,700">
                            <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/asciidoctor/asciidoctor@2.0/data/stylesheets/asciidoctor-default.css">
                        </head>
                                """ +
                asciidoctor.convert(asciidocSource, options) +
                "</html>";
        System.out.println(htmlOutput);

        final String filePath = "./target/test.html";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(htmlOutput.getBytes(StandardCharsets.UTF_8));
            System.out.println("Bytes successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing bytes to file: " + e.getMessage());
        }
    }

}
