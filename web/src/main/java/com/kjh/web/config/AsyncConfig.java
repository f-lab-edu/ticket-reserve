package com.kjh.web.config;

import com.kjh.core.config.BaseAsyncConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public BaseAsyncConfig baseAsyncConfig() {
        return new BaseAsyncConfig(10, 20, 500, "Async-");
    }
}
