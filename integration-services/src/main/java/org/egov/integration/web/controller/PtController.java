package org.egov.integration.web.controller;

import javax.validation.Valid;

import org.egov.integration.model.PtRequestInfoWrapper;
import org.egov.integration.model.ResponseInfoWrapper;
import org.egov.integration.service.PtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pt/v1")
public class PtController {
	private final PtService service;

	@Autowired
	public PtController(PtService service) {
		this.service = service;
	}

	/**
	 * Get and confirms requet for Property Tax
	 * 
	 * @param requestInfoWrapper
	 * @return PT Response
	 */
	@PostMapping(value = "/_get")
	public ResponseEntity<ResponseInfoWrapper> get(@Valid @RequestBody PtRequestInfoWrapper request) {
		return service.getPropertyTaxDetails(request.getUid());
	}

	@PostMapping(value = "/_getOTP")
	public ResponseEntity<ResponseInfoWrapper> getOTP(@Valid @RequestBody PtRequestInfoWrapper request) {
		return service.getOTP(request);
	}

	@PostMapping(value = "/_verifyOTP")
	public ResponseEntity<ResponseInfoWrapper> verifyOTP(@Valid @RequestBody PtRequestInfoWrapper request) {
		return service.verifyOTP(request);
	}

	@PostMapping(value = "/_getSectorList")
	public ResponseEntity<ResponseInfoWrapper> getSectorList(@Valid @RequestBody PtRequestInfoWrapper request) {
		return service.getSectorList(request);
	}

	@PostMapping(value = "/_getPropertyByPID")
	public ResponseEntity<ResponseInfoWrapper> getPropertyByPID(@Valid @RequestBody PtRequestInfoWrapper request) {
		return service.getPropertyByHouseNo(request);
	}
}