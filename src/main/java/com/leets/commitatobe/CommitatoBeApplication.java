package com.leets.commitatobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CommitatoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommitatoBeApplication.class, args);
    }

}
