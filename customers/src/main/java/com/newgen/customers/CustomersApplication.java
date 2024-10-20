package com.newgen.customers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CustomersApplication {
	@Autowired
	Environment environment;
	public static void main(String[] args) {
		SpringApplication.run(CustomersApplication.class, args);
	}
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	@Bean
	WebClient.Builder webClientBuilder(){
		return WebClient.builder();
	}
	@Bean
	String jwtSecretKey() {
		return environment.getProperty("security.jwt.secret-key");
	}
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
