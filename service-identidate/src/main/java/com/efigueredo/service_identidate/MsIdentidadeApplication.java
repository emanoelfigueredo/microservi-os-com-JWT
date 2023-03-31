package com.efigueredo.service_identidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsIdentidadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsIdentidadeApplication.class, args);
	}

}
