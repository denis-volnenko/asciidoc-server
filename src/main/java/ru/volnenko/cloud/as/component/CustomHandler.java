package ru.volnenko.cloud.as.component;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.volnenko.cloud.as.util.EnvUtil;
import ru.volnenko.cloud.as.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

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
            @NonNull final Request baseRequest,
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response
    ) throws IOException, ServletException {
        @NonNull final String file = FileUtil.prepare(request.getRequestURI());
        if (login(baseRequest, request, response, file)) return;
        if (logout(baseRequest, request, response, file)) return;
        if (!auth(request, response)) return;
        if (assets(baseRequest, request, response, file)) return;
        process(baseRequest, request, response);
    }

    private boolean auth(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return false;

        return true;
    }

    private boolean logout(
            @NonNull final Request baseRequest,
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final String file
    ) throws IOException {
        if (!file.equals("logout/")) return false;
        final Cookie cookie = new Cookie("session", "");

        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        response.sendRedirect("/");
        return true;
    }
    private boolean login(
            @NonNull final Request baseRequest,
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final String file
    ) throws IOException {
        if (!file.equals("login/")) return false;
        final String method = request.getMethod();
        if (!"POST".equals(method)) return false;
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final boolean checkUsername = EnvUtil.authUsername().equals(username);
        final boolean checkPassword = EnvUtil.authPassword().equals(password);

        if (checkUsername && checkPassword) {
            response.getOutputStream().write("OK".getBytes(StandardCharsets.UTF_8));
            final Cookie cookie = new Cookie("session", UUID.randomUUID().toString());

            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
            response.getOutputStream().flush();

        } else {
            response.sendRedirect("/");
        }
        return true;
    }

    private boolean assets(
            @NonNull final Request baseRequest,
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final String file
    ) throws IOException {
        if (!file.startsWith("assets/")) return false;
        byte[] bytes = resource(file);
        if (bytes == null) return false;
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
        return true;
    }

    private void process(
            @NonNull final Request baseRequest,
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response
    ) throws ServletException, IOException {
        baseRequest.setHandled(true);
        resourceService.doGet(request, response);
    }

    @SneakyThrows
    private byte[] resource(@NonNull final String resource) {
        @Cleanup final InputStream inputStream = ClassLoader.getSystemResourceAsStream(resource);
        if (inputStream == null) return null;
        return inputStream.readAllBytes();
    }

}
