package com.starking.vendas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.starking.vendas.services.JwtService;
import com.starking.vendas.services.SecurityUserDetailsService;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
	
	private final SecurityUserDetailsService userDetailsService;
	
	private final JwtService jwtService;
	
    private static final String[] AUTH = {  "/api/pessoas/**","/api/categorias/**", "/api/lancamentos/**", "/api/usuarios/**"};
    
    @Bean
	public static PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
    
    
    @Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtService, userDetailsService);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> 
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> 
				authorize.requestMatchers(AUTH).permitAll()
				.requestMatchers(HttpMethod.GET, "/**").permitAll().anyRequest().authenticated())
	            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
