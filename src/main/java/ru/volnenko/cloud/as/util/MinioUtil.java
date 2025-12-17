package ru.volnenko.cloud.as.util;

import io.minio.MinioClient;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class MinioUtil {

    @NonNull
    public static MinioClient client() {
        @NonNull final String minioEndpoint = EnvUtil.minioEndpoint();
        @NonNull final String minioAccessKey = EnvUtil.minioAccessKey();
        @NonNull final String minioSecretKey = EnvUtil.minioSecretKey();
        return MinioClient.builder().endpoint(minioEndpoint).credentials(minioAccessKey, minioSecretKey).build();
    }

}
