package ru.volnenko.cloud.as;

import lombok.NonNull;
import ru.volnenko.cloud.as.component.ServerContext;

public final class Server {

    public static void main(final String[] args) {
        @NonNull final ServerContext serverContext = new ServerContext();
        serverContext.publish();
    }

}
