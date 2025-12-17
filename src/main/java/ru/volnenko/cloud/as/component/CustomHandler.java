package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.handler.ResourceHandler;

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
        resourceService.doGet(request, response);
        if (response.isCommitted()) {
            baseRequest.setHandled(true);
        } else {
            super.handle(target, baseRequest, request, response);
        }
    }

}
