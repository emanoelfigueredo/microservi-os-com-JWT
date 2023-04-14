package com.efigueredo.serviceanotacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceAnotacoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAnotacoesApplication.class, args);
	}

}
