package com.starking.vendas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * @author pedroRhamon
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
    private static final String[] AUTH = {  "/api/pessoas/**","/api/categorias/**", "/api/lancamentos/**"};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> 
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> 
				authorize.requestMatchers(AUTH).permitAll()
				.requestMatchers(HttpMethod.GET, "/**").permitAll().anyRequest().authenticated());
//	            .addFilterBefore(customBasicAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
