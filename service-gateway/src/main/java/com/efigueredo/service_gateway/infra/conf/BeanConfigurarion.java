package com.efigueredo.service_gateway.infra.conf;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurarion {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

}
