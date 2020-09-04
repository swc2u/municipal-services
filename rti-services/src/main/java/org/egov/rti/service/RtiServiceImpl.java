
package org.egov.rti.service;
import java.util.concurrent.CompletableFuture;

import org.egov.rti.common.CommonConstants;
import org.egov.rti.config.RtiConfiguration;
import org.egov.rti.model.Response;
import org.egov.rti.model.RtiRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RtiServiceImpl {

	@Autowired
	private RequestFactory requestFactory;

	private RtiConfiguration config;

	@Autowired
	public RtiServiceImpl(RtiConfiguration config) {

		this.config = config;
	}

	

	@Async
	public CompletableFuture<ResponseEntity<Response>>  getCpioResponse(RtiRequest rti, String token) {
		try {
		
		RestTemplate restTemplate = requestFactory.getRestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("Authorization", "Bearer " + token);
		ResponseEntity<Response> totalRequest = restTemplate.exchange(config.getCpioHost() + config.getCpioPath(),
				HttpMethod.GET, new HttpEntity<>(rti, header), Response.class);
		
		if(totalRequest.getBody().getTransactionNo()!=null || !totalRequest.getBody().getTransactionNo().isEmpty())
		{
			Response res=Response.builder().transactionNo(totalRequest.getBody().getTransactionNo()).build();
			confirmTransaction(res,token);
		}
		return CompletableFuture.completedFuture(totalRequest);
		
		} catch (HttpClientErrorException e) {
			throw new CustomException(CommonConstants.RTI_CPIO_EXCEPTION_CODE, e.getResponseBodyAsString());
		}
	}

	@Async
	public CompletableFuture<ResponseEntity<Response>> getNodalResponse(RtiRequest rti, String token) {
		try {
		
		RestTemplate restTemplate = requestFactory.getRestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("Authorization", "Bearer " + token);
		ResponseEntity<Response> totalRequest = restTemplate.exchange(config.getNodalHost() + config.getNodalPath(),
				HttpMethod.GET, new HttpEntity<>(rti, header), Response.class);
		
		if(totalRequest.getBody().getTransactionNo()!=null || !totalRequest.getBody().getTransactionNo().isEmpty())
		{
			Response res=Response.builder().transactionNo(totalRequest.getBody().getTransactionNo()).build();
			confirmTransaction(res,token);
		}
		return CompletableFuture.completedFuture(totalRequest);
	} catch (HttpClientErrorException e) {
		throw new CustomException(CommonConstants.RTI_NODAL_EXCEPTION_CODE, e.getResponseBodyAsString());
	}
	}

	@Async
	public CompletableFuture<ResponseEntity<Response>> geAppellateResponse(RtiRequest rti, String token) {
		try {
	
		RestTemplate restTemplate = requestFactory.getRestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("Authorization", "Bearer " + token);
		
		ResponseEntity<Response> totalRequest = restTemplate.exchange(config.getAppellateHost() + config.getAppellatePath(),
				HttpMethod.GET, new HttpEntity<>(rti, header), Response.class);
		
		if(totalRequest.getBody().getTransactionNo()!=null || !totalRequest.getBody().getTransactionNo().isEmpty())
		{
			Response res=Response.builder().transactionNo(totalRequest.getBody().getTransactionNo()).build();
			confirmTransaction(res,token);
		}
		return CompletableFuture.completedFuture(totalRequest);
	} catch (HttpClientErrorException e) {
		throw new CustomException(CommonConstants.RTI_APPELLATE_EXCEPTION_CODE, e.getResponseBodyAsString());
	}
	}
	
	@Async
	public void  confirmTransaction(Response res, String token) {
		try {
		
		RestTemplate restTemplate = requestFactory.getRestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add("Authorization", "Bearer " + token);
		ResponseEntity<Response> totalRequest = restTemplate.exchange(config.getTransConfirmHost() + config.getTransConfirmPath(),
				HttpMethod.GET, new HttpEntity<>(res, header), Response.class);
		log.info("rti-services logs :: Executing confirmTransaction() = {}", totalRequest.toString());
		} catch (HttpClientErrorException e) {
			throw new CustomException(CommonConstants.RTI_CONFIM_TRANSACTION_EXCEPTION_CODE, e.getResponseBodyAsString());
		}
	}
	
}