package ru.volnenko.cloud.as.component;

import freemarker.template.Template;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;
import ru.volnenko.cloud.as.dto.Root;
import ru.volnenko.cloud.as.util.EnvUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CustomErrorHandler extends ErrorHandler {

    @NonNull
    private final TemplateProcessor processorFreeMarker;

    @NonNull
    private final Template template;

    @NonNull
    private final String error = error();

    @NonNull
    private final Root root = Root.env();

    public CustomErrorHandler(@NonNull TemplateProcessor processorFreeMarker) {
        this.processorFreeMarker = processorFreeMarker;
        this.template = processorFreeMarker.getIndexTemplate();
    }

    @SneakyThrows
    private String error() {
        return new String(ClassLoader.getSystemClassLoader().getResourceAsStream("static/error.html").readAllBytes());
    }

    @Override
    @SneakyThrows
    public void doError(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        baseRequest.setHandled(true);

        @NonNull final Map<String, Object> data = new LinkedHashMap<>();
        data.put("body", error);
        data.put("title", "ERROR");
        data.put("caption", EnvUtil.caption());
        data.put("footer", EnvUtil.footer());
        data.put("menuLeft", root.left().getItems());
        data.put("menuLeftEnabled", root.left().enabled());
        data.put("menuLeftSize", EnvUtil.menuLeftSize());
        data.put("menuMain", root.main().getItems());
        data.put("menuMainEnabled", root.main().enabled());
        template.process(data, response.getWriter());

        baseRequest.getResponse().closeOutput();
    }

}
