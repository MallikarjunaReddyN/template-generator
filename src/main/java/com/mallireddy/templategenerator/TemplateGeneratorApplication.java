package com.mallireddy.templategenerator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Template Generator", description = "This is a Spring Boot Template Microservice"))
/*@OpenAPIDefinition(info = @Info(title = "Template Generator", description = "This is a Spring Boot Template Microservice"),
        security = @SecurityRequirement(name = "AUTHORIZATION"))
@SecurityScheme(name = "AUTHORIZATION", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)*/
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class TemplateGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateGeneratorApplication.class, args);
    }

}
