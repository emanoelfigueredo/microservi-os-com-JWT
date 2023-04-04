package com.efigueredo.service_identidade.infra.conf;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfigurarion {

    @Bean
    public ModelMapper obterModelMapper() {
        return new ModelMapper();
    }

}
