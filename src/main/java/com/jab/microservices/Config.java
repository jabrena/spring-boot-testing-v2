package com.jab.microservices;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    @ConfigurationProperties(prefix = "provider")
    GlobalConfiguration globalConfig(){
        return new GlobalConfiguration();
    }

}
