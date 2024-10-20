package com.newgen.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	Environment environment;
	
	private final String jwtSecretKey;
	
	public SecurityConfig(String jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth-> auth
				.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated())
		.addFilterBefore(new JwtAuthenticationFilter(jwtSecretKey), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
