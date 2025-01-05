package com.starking.vendas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springdoc.core.models.GroupedOpenApi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * @author pedroRhamon
 */

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("API Vendas")
						.version("1.0")
				.description("API documentation for the application in Vendas in Geolocation")
				.termsOfService("http://swagger.io/terms/")
				.license(new License().name("Directly").url("https://github.com/pedrorhamon")))
				.addSecurityItem(new SecurityRequirement().addList("JWT"))
	            .components(new Components().addSecuritySchemes("JWT", createJWT()));
	}

	private SecurityScheme createJWT() {
		return new SecurityScheme()
	            .name("JWT")
	            .type(SecurityScheme.Type.HTTP)
	            .scheme("bearer")
	            .bearerFormat("JWT")
	            .in(SecurityScheme.In.HEADER)
	            .name("Authorization");
	}

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("public-api")
				.pathsToMatch("/**")
				.build();
	}
}
