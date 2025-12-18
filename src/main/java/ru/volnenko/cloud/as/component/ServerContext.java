package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import ru.volnenko.cloud.as.util.EnvUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ServerContext {

    @NonNull
    public static final String WEBAPP = "./";

    @NonNull
    private final Integer serverPort = 8080;

    @NonNull
    private final String[] welcomeFiles = welcomeFiles();

    @NonNull
    private final TemplateProcessor processorFreeMarker = new TemplateProcessor();

    @NonNull
    private final ProcessorAsciiDoc processorAsciiDoc = new ProcessorAsciiDoc(processorFreeMarker);

    @NonNull
    private final Processor[] processors = new Processor[] { processorAsciiDoc };

    @NonNull
    private final DefaultHandler defaultHandler = new DefaultHandler();

    @NonNull
    private final FileService fileService = new FileService(processors);

    @NonNull
    private final CustomHandler customHandler = customHandler(fileService, welcomeFiles);

    @NonNull
    private final CustomErrorHandler customErrorHandler = new CustomErrorHandler(processorFreeMarker);

    @NonNull
    private final ContextHandler contextHandler = contextHandler(customHandler);

    @NonNull
    private final HandlerList handlerList = handlerList(contextHandler, defaultHandler);

    @NonNull
    private final Server server = server(handlerList);

    @NonNull
    private String[] welcomeFiles() {
        return new String[]{ "index.html", "index.htm", "index.adoc" };
    }

    @NonNull
    private CustomHandler customHandler(
            @NonNull final FileService fileService,
            @NonNull final String[] welcomeFiles
    ) {
        @NonNull final CustomHandler resourceHandler = new CustomHandler(fileService);
        resourceHandler.setDirectoriesListed(EnvUtil.directoriesListed());
        resourceHandler.setRedirectWelcome(EnvUtil.redirectWelcome());
        resourceHandler.setWelcomeFiles(welcomeFiles);
        resourceHandler.setResourceBase(WEBAPP);
        return resourceHandler;
    }

    @NonNull
    private ContextHandler contextHandler(@NonNull final CustomHandler customHandler) {
        @NonNull final ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setHandler(customHandler);
        contextHandler.setErrorHandler(customErrorHandler);
        return contextHandler;
    }

    @NonNull
    private HandlerList handlerList(
            @NonNull final ContextHandler contextHandler,
            @NonNull final DefaultHandler defaultHandler
    ) {
        @NonNull final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { contextHandler, defaultHandler});
        return handlers;
    }

    @NonNull
    private Server server(@NonNull final HandlerList handlerList) {
        @NonNull final Server server = new Server();
        @NonNull final ServerConnector serverConnector = new ServerConnector(server);
        @NonNull final Connector[] connectors = new Connector[] { serverConnector };
        serverConnector.setPort(serverPort);
        server.setConnectors(connectors);
        server.setHandler(handlerList);
        return server;
    }

    @SneakyThrows
    public void publish() {
        BasicConfigurator.configure();
        server.start();
    }

}
