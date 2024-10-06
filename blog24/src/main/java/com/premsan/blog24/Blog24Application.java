package com.premsan.blog24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan("com.premsan")
@EnableJdbcRepositories("com.premsan")
public class Blog24Application {

    public static void main(String[] args) {
        SpringApplication.run(Blog24Application.class, args);
    }
}
