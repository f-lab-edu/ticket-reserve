package com.kjh.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.kjh.core")
@EnableJpaRepositories(basePackages = "com.kjh.core")
@SpringBootApplication(scanBasePackages = { "com.kjh.core", "com.kjh.admin" })
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
