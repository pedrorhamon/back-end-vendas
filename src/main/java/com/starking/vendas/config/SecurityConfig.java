package com.starking.vendas.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.services.JwtService;
import com.starking.vendas.services.SecurityUserDetailsService;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
	
	private final SecurityUserDetailsService userDetailsService;
	
	private final JwtService jwtService;
	
    private static final String[] AUTH = {  "/api/pessoas/**",
    		"/api/categorias/**", "/api/lancamentos/**", 
    		"/api/usuarios/**", "/api/permissoes/**"};
    
    @Bean
	public static PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
    
    
    @Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtService, userDetailsService);
	}
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable())
//				.sessionManagement(session -> 
//				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authorizeHttpRequests(authorize -> 
//				authorize.requestMatchers(AUTH).permitAll()
//				.requestMatchers(HttpMethod.POST, "/**").authenticated()
//				.requestMatchers(HttpMethod.GET, "/**").permitAll().anyRequest().authenticated())
//	            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//		return http.build();
//	}
    
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//            .sessionManagement(session -> 
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(authorize -> 
//                authorize
//                    .requestMatchers(AUTH).permitAll()
//                    .anyRequest().authenticated()
//                    .anyRequest().hasRole("ADMIN_PRIVILEGE"))
//            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH).permitAll()
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
//    @Bean
//    public FilterRegistrationBean<OpenSessionInViewFilter> openSessionInViewFilter() {
//        FilterRegistrationBean<OpenSessionInViewFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new OpenSessionInViewFilter());
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }
    
//    @Bean
//	public FilterRegistrationBean<CorsFilter> corsFilter(){
//		
//		List<String> all = Arrays.asList("http://localhost:3000");
//		
//		CorsConfiguration config = new CorsConfiguration();
//		config.setAllowedMethods(all);
//		config.setAllowedOrigins(all);
//		config.setAllowedHeaders(all);
//		config.setAllowCredentials(true);
//		
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", config);
//		
//		CorsFilter corFilter = new CorsFilter(source);
//		
//		FilterRegistrationBean<CorsFilter> filter = 
//				new FilterRegistrationBean<CorsFilter>(corFilter);
//		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		
//		return filter;
//	}


}
