package ru.volnenko.cloud.as.component;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import ru.volnenko.cloud.as.util.EnvUtil;
import ru.volnenko.cloud.as.util.MinioUtil;

public final class MinioStorage implements Storage {

    @NonNull
    private final MinioClient minioClient = MinioUtil.client();

    @Override
    @SneakyThrows
    public String text(@NonNull final String resource) {
        return new String(bytes(resource));
    }

    public GetObjectArgs getObjectArgs(@NonNull final String resource) {
        return GetObjectArgs.builder().bucket(EnvUtil.minioBucket()).object(resource).build();
    }

    @Override
    @SneakyThrows
    public byte[] bytes(@NonNull final String resource) {
        return IOUtils.toByteArray(minioClient.getObject(getObjectArgs(resource)));
    }

}
