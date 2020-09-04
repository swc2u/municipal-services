package org.egov.rti;

import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.egov.rti.model.Response;
import org.egov.rti.model.RtiRequest;
import org.egov.rti.service.RtiService;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan(basePackages = {"org.egov.rti","org.egov.rti.web.controller","org.egov.rti.config","org.egov.rti.util"})
@Import({TracerConfiguration.class})
@EnableAsync
public class RtiServices {
	@Value("${app.timezone}")
    private String timeZone;
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
	}

    public static void main(String[] args) throws Exception {
        new SpringApplication(RtiServices.class).run(args);
    }
  
    
}
