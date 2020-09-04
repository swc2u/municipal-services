package org.egov.rti.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.egov.rti.model.RequestInfoWrapper;
import org.egov.rti.model.ResponseInfoWrapper;
import org.egov.rti.service.RtiService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class RtiController {

	private final RtiService service;
	
	@Autowired
	public RtiController(RtiService service) {
		this.service = service;
	}
	
	/**
	 * Get and confirms total requet for nodal,appealate,cpio
	 * @param requestInfoWrapper 
	 * @return Rti Response
	 */
	@PostMapping(value = "/_get")
	public ResponseEntity<ResponseInfoWrapper> get(@Valid @RequestBody  RequestInfoWrapper request) {
		 
		return service.get(request);
		
	}
	/**
	 * Get ministry mapping with user id
	 * @param requestInfoWrapper 
	 * @return Ministry Master Response
	 */
	@PostMapping(value = "/_getDepartment")
	public ResponseEntity<ResponseInfoWrapper> getDepartment(@Valid @RequestBody  RequestInfoWrapper request)  {
		 
		return service.getDepartment(request);
		
	}
	
	

}
