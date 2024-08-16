//package com.starking.vendas.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@EnableWebMvc
//@Configuration
//public class WebConfiguration implements WebMvcConfigurer {
//
//	@Override
//	public void addCorsMappings( CorsRegistry registry ) {
//		registry.addMapping("/**")
//		 .allowedOrigins("http://localhost")
//		 .allowedOrigins("http://localhost:4200")
//		 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//		 .allowedHeaders("*")
//         .allowCredentials(true);
//	}
//}