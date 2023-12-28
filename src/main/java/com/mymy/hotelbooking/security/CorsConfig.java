package com.mymy.hotelbooking.security;

import java.util.Arrays;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class CorsConfig {	
	
	private static final Long MAX_AGE = 3600L;	
	
	@Bean
	public CorsFilter corsFilter()
	{
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowCredentials(true);
		corsConfig.addAllowedOrigin("http://localhost:5173");
		corsConfig.addAllowedOrigin("https://hotelbooking-themusclecat.vercel.app/");
		corsConfig.setAllowedHeaders(Arrays.asList(
				HttpHeaders.AUTHORIZATION,
				HttpHeaders.CONTENT_TYPE,
				HttpHeaders.ACCEPT));
		corsConfig.setAllowedMethods(Arrays.asList(
				HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.DELETE.name()));
		corsConfig.setMaxAge(MAX_AGE);
		source.registerCorsConfiguration("/**", corsConfig);
		CorsFilter bean = new CorsFilter(source);
		return bean;		
	}

}
