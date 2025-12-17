package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import ru.volnenko.cloud.as.util.EnvUtil;

public interface Storage {

    @NonNull
    static Storage create() {
        @NonNull final String type = EnvUtil.storageType();
        if ("MINIO".equals(type)) return new MinioStorage();
        return new FileStorage();
    }

    String text(@NonNull String resource);

    byte[] bytes(@NonNull String resource);

}
