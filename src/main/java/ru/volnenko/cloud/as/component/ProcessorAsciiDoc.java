package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.eclipse.jetty.http.HttpContent;
import ru.volnenko.cloud.as.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

public final class ProcessorAsciiDoc implements Processor {

    @NonNull
    private static final Asciidoctor ASCIIDOCTOR = Asciidoctor.Factory.create();

    @NonNull
    private static final Options OPTIONS = OptionsBuilder.options().backend("html5").safe(SafeMode.UNSAFE).build();

    @NonNull
    private static Path BASE_PATH = getBasePath();

    @NonNull
    private final String header = header();

    @NonNull
    private final String footer = footer();

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
        final String data = new String(bytes);

        final String asciidoc = ASCIIDOCTOR.convert(data, OPTIONS);
        response.getWriter().println(header);
        response.getWriter().println(asciidoc);
        response.getWriter().println(footer);
        response.getWriter().flush();
        return true;
    }

    private static Path getBasePath() {
        return Path.of("").toAbsolutePath();
    }

    public boolean valid(@NonNull String name) {
        return name.toLowerCase().endsWith(".adoc");
    }

    @SneakyThrows
    private static String header() {
        return new String(ClassLoader.getSystemResourceAsStream("header_adoc.html").readAllBytes());
    }

    @SneakyThrows
    private static String footer() {
        return new String(ClassLoader.getSystemResourceAsStream("footer_adoc.html").readAllBytes());
    }

}

