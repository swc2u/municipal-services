package org.egov.bookings.config;

import java.math.BigDecimal;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.bookings.config.BookingsConfiguration.BookingsConfigurationBuilder;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Import({ TracerConfiguration.class })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class FinanceConfiguration {

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void initialize() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

	@Bean
	@Autowired
	public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);
		return converter;
	}
	
	
	 @Value("${finace.vocher.auth.authorization}")
	 private String keyAuthorization;
	 
	 @Value("${finace.vocher.auth.grant_type}")
	 private String grantType;
	 
	 @Value("${finace.vocher.auth.scope}")
	 private String scope;
	 
	 
	 @Value("${finace.vocher.auth.username}")
	 private String userName;


	 @Value("${finace.vocher.auth.identification}")
	 private String identification;
	 
	 
	 @Value("${finace.vocher.auth.tenantId}")
	 private String tenantId;
	 

	 @Value("${finace.vocher.auth.userType}")
	 private String userType;
	 
	 
	 @Value("${finace.vocher.auth.url}")
	 private String authUrl;
}
