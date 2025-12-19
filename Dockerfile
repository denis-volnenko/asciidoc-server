FROM eclipse-temurin:21-jdk as docker
MAINTAINER Denis Volnenko <denis@volnenko.ru>

COPY  ./target/asciidoc-server/ /opt/asciidoc-server/
WORKDIR /opt/asciidoc-server/data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/asciidoc-server/bin/asciidoc-server.jar"]