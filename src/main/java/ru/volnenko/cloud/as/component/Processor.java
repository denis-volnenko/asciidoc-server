package ru.volnenko.cloud.as.component;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public interface Processor {

    boolean valid(@NonNull String name);

    boolean process(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            boolean include,
            @NonNull HttpContent content,
            Enumeration<String> reqRanges
    );

}
