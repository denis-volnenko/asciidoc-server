package ru.volnenko.cloud.as.component;

import freemarker.template.Template;
import jnr.a64asm.OP;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.Document;
import org.eclipse.jetty.http.HttpContent;
import ru.volnenko.cloud.as.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ProcessorAsciiDoc implements Processor {

    @NonNull
    private static final Asciidoctor ASCIIDOCTOR = Asciidoctor.Factory.create();

    @NonNull
    private static final Options OPTIONS = OptionsBuilder.options().backend("html5").safe(SafeMode.UNSAFE).build();

    @NonNull
    private static Path BASE_PATH = getBasePath();

    @NonNull
    private final TemplateProcessor processorFreeMarker = new TemplateProcessor();

    @NonNull
    private final Template template = processorFreeMarker.getIndexTemplate();

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

//        org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
//        context.setVariable("request", request);
//        context.setVariable("response", response);
//        context.setVariable("context", new ContextProxy(request, response));

        @NonNull final String file = FileUtil.prepare(request.getRequestURI());
        Path resolvedPath = BASE_PATH.resolve(file);
        // 2. Crucial Security Check: Ensure the resolved path is still inside the base directory
        if (!resolvedPath.startsWith(BASE_PATH)) {
            throw new SecurityException("Invalid file path: " + file + ". Access denied.");
        }
        final byte[] bytes = Files.readAllBytes(resolvedPath);
        final String adoc = new String(bytes);

        Document document = ASCIIDOCTOR.load(adoc, OPTIONS);

        final String html = ASCIIDOCTOR.convert(adoc, OPTIONS);
        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("body", html);
        data.put("title", document.getTitle());
        template.process(data, response.getWriter());

        return true;
    }

    @NonNull
    private static Path getBasePath() {
        return Path.of("").toAbsolutePath();
    }

    public boolean valid(@NonNull final String name) {
        return name.toLowerCase().endsWith(".adoc");
    }

}

