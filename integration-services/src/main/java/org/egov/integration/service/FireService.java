
package org.egov.integration.service;

import java.util.UUID;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.integration.common.CommonConstants;
import org.egov.integration.config.RtiConfiguration;
import org.egov.integration.model.AccountResponse;
import org.egov.integration.model.FireNoc;
import org.egov.integration.model.FireRequestInfoWrapper;
import org.egov.integration.model.PtMapping;
import org.egov.integration.model.ResponseInfoWrapper;
import org.egov.integration.model.RtiRequest;
import org.egov.integration.model.RtiResponse;
import org.egov.integration.repository.RtiRepository;
import org.egov.integration.repository.fireRepository;
import org.egov.integration.util.AuditDetailsUtil;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FireService {

	@Autowired
	private AuditDetailsUtil auditDetailsUtil;
	
	@Autowired
	private fireRepository repository;
	
	@Autowired
	private  ObjectMapper objectMapper;



	public ResponseEntity<ResponseInfoWrapper> postData(FireRequestInfoWrapper request){
		try {
			FireNoc data = objectMapper.convertValue(request.getFireRequest(), FireNoc.class);

			String uuid = UUID.randomUUID().toString();
			data.setUuid(uuid);
			data.setIsActive(true);
			data.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
			
			repository.saveFireData(data);
			
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(data)
					.build(), HttpStatus.CREATED);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.FIR_NOC_EXCEPTION_CODE, e.getMessage());
		}

		
	}	
	
	
}