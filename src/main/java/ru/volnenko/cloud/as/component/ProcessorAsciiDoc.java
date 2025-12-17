package ru.volnenko.cloud.as.component;

import freemarker.template.Template;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.Document;
import org.eclipse.jetty.http.HttpContent;
import ru.volnenko.cloud.as.dto.Root;
import ru.volnenko.cloud.as.util.EnvUtil;
import ru.volnenko.cloud.as.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ProcessorAsciiDoc implements Processor {

    @NonNull
    private static Path BASE_PATH = getBasePath();

    @NonNull
    private static final Asciidoctor ASCIIDOCTOR = Asciidoctor.Factory.create();

    @NonNull
    private static final Options OPTIONS = OptionsBuilder.options().backend("html5").safe(SafeMode.UNSAFE).build();

    @NonNull
    private final TemplateProcessor processorFreeMarker = new TemplateProcessor();

    @NonNull
    private final Template template = processorFreeMarker.getIndexTemplate();

    @NonNull
    private final Root root = Root.env();

    @Override
    @SneakyThrows
    public boolean process(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            final boolean include,
            @NonNull final HttpContent content,
            final Enumeration<String> reqRanges
    ) {
        response.setCharacterEncoding("UTF-8");
        @NonNull final String file = FileUtil.prepare(request.getRequestURI());

        @NonNull final Path resolvedPath = BASE_PATH.resolve(file);
        if (!resolvedPath.startsWith(BASE_PATH)) throw new SecurityException("Invalid file path: " + file + ". Access denied.");
        @NonNull final byte[] bytes = Files.readAllBytes(resolvedPath);
        @NonNull final String adoc = new String(bytes);

        @NonNull final Document document = ASCIIDOCTOR.load(adoc, OPTIONS);
        @NonNull final String html = ASCIIDOCTOR.convert(adoc, OPTIONS);

        @NonNull final Map<String, Object> data = new LinkedHashMap<>();
        data.put("body", html);
        data.put("title", document.getTitle());
        data.put("caption", EnvUtil.caption());
        data.put("footer", EnvUtil.footer());
        data.put("menuLeft", root.left().getItems());
        data.put("menuLeftEnabled", root.left().enabled());
        data.put("menuLeftSize", EnvUtil.menuLeftSize());
        data.put("menuMain", root.main().getItems());
        data.put("menuMainEnabled", root.main().enabled());
        template.process(data, response.getWriter());

        return true;
    }

    public boolean valid(@NonNull final String name) {
        return name.toLowerCase().endsWith(".adoc");
    }

    @NonNull
    private static Path getBasePath() {
        return Path.of("").toAbsolutePath();
    }

}

