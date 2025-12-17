package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public final class FileStorage implements Storage {

    @NonNull
    private static Path BASE_PATH = getBasePath();

    @Override
    @SneakyThrows
    public String text(@NonNull final String resource) {
        @NonNull final Path resolvedPath = BASE_PATH.resolve(resource);
        if (!resolvedPath.startsWith(BASE_PATH)) throw new SecurityException("Invalid file path: " + resource + ". Access denied.");
        @NonNull final byte[] bytes = Files.readAllBytes(resolvedPath);
        @NonNull final String adoc = new String(bytes);
        return adoc;
    }

    @Override
    @SneakyThrows
    public byte[] bytes(@NonNull final String resource) {
        @NonNull final Path resolvedPath = BASE_PATH.resolve(resource);
        if (!resolvedPath.startsWith(BASE_PATH)) throw new SecurityException("Invalid file path: " + resource + ". Access denied.");
        return Files.readAllBytes(resolvedPath);
    }

    @NonNull
    private static Path getBasePath() {
        return Path.of("").toAbsolutePath();
    }

}
