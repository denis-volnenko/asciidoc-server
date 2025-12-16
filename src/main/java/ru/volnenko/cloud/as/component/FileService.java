package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jetty.http.HttpContent;
import org.eclipse.jetty.server.ResourceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public final class FileService extends ResourceService {

    @NonNull
    private final Processor[] processors;

    public FileService(@NonNull final Processor[] processors) {
        this.processors = processors;
    }

    @Override
    @SneakyThrows
    protected boolean sendData(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            final boolean include,
            @NonNull final HttpContent content,
            final Enumeration<String> reqRanges
    ) {
        @NonNull final String file = request.getRequestURI();
        if (processors != null)
            for (Processor processor: processors)
                if (processor.valid(file))
                    return processor.process(request, response, include, content, reqRanges);
        return super.sendData(request, response, include, content, reqRanges);
    }

}
