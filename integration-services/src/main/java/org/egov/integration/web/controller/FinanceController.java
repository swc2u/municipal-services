package org.egov.integration.web.controller;

import javax.validation.Valid;

import org.egov.integration.model.PaymentsRequest;
import org.egov.integration.model.ResponseInfoWrapper;
import org.egov.integration.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/finance/v1")
public class FinanceController {
	private final FinanceService service;

	@Autowired
	public FinanceController(FinanceService service) {
		this.service = service;
	}


	@PostMapping(value = "/_generate")
	public ResponseEntity<ResponseInfoWrapper> generate(@Valid @RequestBody PaymentsRequest request) {
		log.info("request"+request);
		log.info("authToke"+request.getRequestInfo().getAuthToken());
		log.info("requestInfo"+request.getRequestInfo());
		return service.generate(request);
	}

	

}
