package com.ecommerce.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ProductServiceDomainApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceDomainApplication.class, args);
	}
	@Bean
	public ObjectMapper obj() {
		
	return new ObjectMapper();	
	}
}
