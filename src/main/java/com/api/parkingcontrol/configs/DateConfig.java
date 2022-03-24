package com.api.parkingcontrol.configs;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class DateConfig {
	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //padrão UTC informado pelo Z
	public static LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = 
			new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
		
			
	@Bean // metodo produtor
	@Primary //questoes de prioridades
	public ObjectMapper objectMapper() { // passa pro JavaTimeModule assumir esse padrão que adotamos
		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(LOCAL_DATETIME_SERIALIZER);
		return new ObjectMapper()
				.registerModule(module);
	}
	
}
