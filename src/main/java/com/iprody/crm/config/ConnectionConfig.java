package com.iprody.crm.config;


import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfig {
    private static final int HTTP_PORT = 8080;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            // Настройка дополнительного порта для HTTP (8080)
            factory.addAdditionalTomcatConnectors(httpConnector());
        };
    }

    // HTTP порт (обычный)
    private Connector httpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(HTTP_PORT); // Указываем порт 8080
        return connector;
    }
}
