package com.avanza.license.util;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = "com.avanza.*")
@EnableJpaRepositories(basePackages = "com.avanza.*")
@EntityScan(basePackages = "com.avanza.*")
public class LicenseManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(LicenseManagementApplication.class, args);
    }

    private Connector connector (){
        Connector connector=new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(9093);
        connector.setSecure(false);
        connector.setScheme("http");
        return connector;
    }
    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
        TomcatServletWebServerFactory factory=new TomcatServletWebServerFactory();
        factory.addAdditionalTomcatConnectors(connector());
        return factory;
    }
}
