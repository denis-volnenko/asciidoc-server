package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.volnenko.cloud.as.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class CustomHandler extends ResourceHandler {

    @NonNull
    private final ResourceService resourceService;

    public CustomHandler(@NonNull final ResourceService resourceService) {
        super(resourceService);
        this.resourceService = resourceService;
    }

    @Override
    public void handle(
            final String target,
            final Request baseRequest,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException, ServletException {
        @NonNull final String file = FileUtil.prepare(request.getRequestURI());
        if (file.startsWith("assets/")) {
            response.getOutputStream().write(resource(file));
            response.getOutputStream().flush();
            return;
        }
        baseRequest.setHandled(true);
        resourceService.doGet(request, response);
    }

    @SneakyThrows
    private byte[] resource(@NonNull final String resource) {
        return ClassLoader.getSystemResourceAsStream(resource).readAllBytes();
    }

}
