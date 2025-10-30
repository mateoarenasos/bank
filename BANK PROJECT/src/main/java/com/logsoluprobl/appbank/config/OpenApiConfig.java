package com.logsoluprobl.appbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank App API")
                        .description("API REST para la gestión de clientes, cuentas y transacciones bancarias")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Daniel Felipe Agudelo Molina")
                                .email("daniel.agudelomo@amigo.edu.co")
                                .url("https://github.com/danieldev87"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
