package com.pharmastock.pharmastock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PharmastockApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmastockApplication.class, args);
	}

	// Ce composant permettra à notre application de CONSOMMER des API externes
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}