package com.learntrack.course_engine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI courseEngineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Engine API")
                        .description("High-concurrency course enrollment engine with optimistic locking, rate limiting, and async waitlist processing.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Varun")
                                .url("https://github.com/VarunPS-eee/course-engine"))
                        .license(new License().name("MIT License")));
    }
}