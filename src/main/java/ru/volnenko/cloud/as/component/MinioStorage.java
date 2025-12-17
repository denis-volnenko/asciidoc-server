package ru.volnenko.cloud.as.component;

import io.minio.MinioClient;
import lombok.NonNull;
import ru.volnenko.cloud.as.util.MinioUtil;

public final class MinioStorage implements Storage {

    @NonNull
    private final MinioClient minioClient = MinioUtil.client();

    @Override
    public String text(@NonNull final String resource) {
        return null;
    }

    @Override
    public byte[] bytes(@NonNull final String resource) {
        return new byte[0];
    }

}
