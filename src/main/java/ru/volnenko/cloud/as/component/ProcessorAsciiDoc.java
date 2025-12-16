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
    private final String headerADOC = headerADOC();

    @NonNull
    private final String headerGUI = headerGUI();

    @NonNull
    private final String footerADOC = footerADOC();

    @NonNull
    private final String footerGUI = footerGUI();

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
        response.getWriter().println(headerADOC);
        response.getWriter().println(headerGUI);
        response.getWriter().println(asciidoc);
        response.getWriter().println(footerGUI);
        response.getWriter().println(footerADOC);
        response.getWriter().flush();
        return true;
    }

    @NonNull
    private static Path getBasePath() {
        return Path.of("").toAbsolutePath();
    }

    public boolean valid(@NonNull final String name) {
        return name.toLowerCase().endsWith(".adoc");
    }

    @NonNull
    @SneakyThrows
    private static String headerADOC() {
        return resource("header_adoc.html");
    }

    @NonNull
    @SneakyThrows
    private static String footerADOC() {
        return resource("footer_adoc.html");
    }

    @NonNull
    @SneakyThrows
    private static String headerGUI() {
        return resource("header_gui.html");
    }

    @NonNull
    @SneakyThrows
    private static String footerGUI() {
        return resource("footer_gui.html");
    }

    @NonNull
    @SneakyThrows
    private static String resource(@NonNull final String name) {
        return new String(ClassLoader.getSystemResourceAsStream(name).readAllBytes());
    }

}

