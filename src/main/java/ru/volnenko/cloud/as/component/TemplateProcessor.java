package ru.volnenko.cloud.as.component;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TemplateProcessor {

    @NonNull
    private final static String INDEX = "index.ftl";

    @NonNull
    private final DefaultObjectWrapper wrapper = new DefaultObjectWrapper();

    @NonNull
    private final ClassTemplateLoader classTemplateLoader = classTemplateLoader();

    @NonNull
    private final Configuration classTemplateConfiguration = configuration(wrapper, classTemplateLoader);

    @NonNull
    private ClassTemplateLoader classTemplateLoader() {
        return new ClassTemplateLoader(ClassLoader.getSystemClassLoader(), "static");
    }

    @NonNull
    private Configuration configuration(
            @NonNull final DefaultObjectWrapper wrapper,
            @NonNull final TemplateLoader fileTemplateLoader
    ) {
        @NonNull final Configuration configuration = new Configuration();
        configuration.setObjectWrapper(wrapper);
        configuration.setTemplateLoader(fileTemplateLoader);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }

    @SneakyThrows
    public static void main(String[] args) {
        @NonNull final TemplateProcessor processorFreeMarker = new TemplateProcessor();
        @NonNull final Template template = processorFreeMarker.getIndexTemplate();
        @NonNull final Map<String, Object> data = new LinkedHashMap<>();
        data.put("body", "123");
        @Cleanup final Writer out = new StringWriter();
        template.process(data, out);
        System.out.println(out);
    }

    @NonNull
    @SneakyThrows
    public Template getIndexTemplate() {
        return classTemplateConfiguration.getTemplate(INDEX);
    }

}
