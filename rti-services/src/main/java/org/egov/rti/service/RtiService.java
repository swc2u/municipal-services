
package org.egov.rti.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.rti.common.CommonConstants;
import org.egov.rti.config.RtiConfiguration;
import org.egov.rti.model.AccountResponse;
import org.egov.rti.model.MinistryMaster;
import org.egov.rti.model.RequestInfoWrapper;
import org.egov.rti.model.Response;
import org.egov.rti.model.ResponseInfoWrapper;
import org.egov.rti.model.RtiRequest;
import org.egov.rti.model.RtiResponse;
import org.egov.rti.repository.RtiRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RtiService {

	@Autowired
	private RequestFactory requestFactory;
	private RtiServiceImpl rtiServiceImpl;
	private RtiRepository repository;
	private RtiConfiguration config;
	private final ObjectMapper objectMapper;

	@Autowired
	public RtiService(RtiConfiguration config, ObjectMapper objectMapper,RtiServiceImpl rtiServiceImpl,RtiRepository repository) {

		this.config = config;
		this.objectMapper = objectMapper;
		this.rtiServiceImpl=rtiServiceImpl;
		this.repository=repository;
	}

	public ResponseEntity<ResponseInfoWrapper> get(RequestInfoWrapper request){
		RestTemplate restTemplate = requestFactory.getRestTemplate();

		try {
			RtiRequest rti = objectMapper.convertValue(request.getRtiRequest(), RtiRequest.class);

			RtiResponse reponse = new RtiResponse();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(
					config.getAccountHost() + config.getAccountPath(), HttpMethod.GET,
					new HttpEntity<>("{\"password\": \"" + config.getPassword() + "\",\"username\": \""
							+ config.getUsername() + "\"}", headers),
					AccountResponse.class);

			if (responseEntity.getBody().getToken() != null || !responseEntity.getBody().getToken().isEmpty()) {
				CompletableFuture<ResponseEntity<Response>> res = rtiServiceImpl.getCpioResponse(rti, responseEntity.getBody().getToken());
				CompletableFuture<ResponseEntity<Response>> nodalres = rtiServiceImpl.getNodalResponse(rti, responseEntity.getBody().getToken());
				CompletableFuture<ResponseEntity<Response>> appellateres = rtiServiceImpl.geAppellateResponse(rti, responseEntity.getBody().getToken());

				try {
					reponse.setCpio(res.get().getBody());
					reponse.setNodal(nodalres.get().getBody());
					reponse.setAppellate(appellateres.get().getBody());
				} catch (InterruptedException | ExecutionException e) {
					
					throw new CustomException(CommonConstants.RTI_EXCEPTION_CODE, e.getMessage());
				}
				
			}

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(reponse)
					.build(), HttpStatus.OK);
		} catch (HttpClientErrorException e) {
			throw new CustomException(CommonConstants.RTI_EXCEPTION_CODE, e.getResponseBodyAsString());
		}

	}
	
	
	public ResponseEntity<ResponseInfoWrapper> getDepartment(RequestInfoWrapper request) {
		try {
    		Long userId=request.getRequestInfo().getUserInfo().getId();
			List<MinistryMaster> SEPApplicationresult = repository.getDepartment(request);
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(SEPApplicationresult).build(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(CommonConstants.RTI_EXCEPTION_CODE, e.getMessage());
		}
	}

}