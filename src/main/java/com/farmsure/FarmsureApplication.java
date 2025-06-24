package com.farmsure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class FarmsureApplication {
    public static void main(String[] args) {
        SpringApplication.run(FarmsureApplication.class, args);
    }
}

@Component
class PortLogger implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() instanceof ServletWebServerApplicationContext) {
            ServletWebServerApplicationContext webServerContext = (ServletWebServerApplicationContext) event
                    .getApplicationContext();
            int port = webServerContext.getWebServer().getPort();
            System.out.println("Application is running at http://localhost:" + port);
        }
    }
}