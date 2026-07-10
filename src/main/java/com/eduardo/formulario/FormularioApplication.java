package com.eduardo.formulario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;



@SpringBootApplication
public class FormularioApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormularioApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> exibirUrl(Environment environment) {
        return event -> {
            String porta = environment.getProperty("local.server.port", "8080");
            System.out.println("Acesse o formulário em: http://localhost:" + porta + "/vistoria");
        };
    }
}