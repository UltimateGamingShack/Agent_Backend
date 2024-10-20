package com.newgen.agent;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class AgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentApplication.class, args);
	}
	@Bean
	@LoadBalanced
	WebClient.Builder webClientBuilder(){
		return WebClient.builder();
	}
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
