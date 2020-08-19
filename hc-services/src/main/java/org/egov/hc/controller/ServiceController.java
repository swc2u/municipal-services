package org.egov.hc.controller;

import org.egov.hc.contract.ServiceRequest;
import org.egov.hc.contract.ServiceResponse;
import org.egov.hc.model.RequestData;
import org.egov.hc.service.ServiceRequestService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/serviceRequest")
public class ServiceController {

	@Autowired
	private ServiceRequestService service;

	/*
	 * enpoint to create & edit service requests
	 * 
	 * @param ServiceReqRequest
	 * 
	 * @author Dhanaji Patil
	 */

	@RequestMapping(value = "/_create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody ServiceRequest serviceRequest,
			@RequestHeader("User-Agent") String request) throws JSONException, InterruptedException {
		ServiceResponse response = null;

		if (serviceRequest.getServices().get(0).getIsEditState() == 1) {	
			response = service.updateServiceRequest(serviceRequest, request);
		} else {
			response = service.create(serviceRequest, request);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	/*
	 * enpoint to getDetail service requests
	 * 
	 * @param ServiceReqRequest
	 * 
	 * @author Dhanaji Patil
	 */

	@PostMapping("_getDetail")
	@ResponseBody
	public ResponseEntity<?> getDetail(@RequestBody RequestData requestData) {
		log.debug(String.format("STARTED Get Details SERVICE REQUEST : %1s", requestData.toString()));
		return service.getServiceRequestDetails(requestData);
	}
	
	
	/*
	 * enpoint to get list of service requests as per the filter criteria
	 * 
	 * @param  RequestInfoWrapper,RequestData
	 * 
	 * @author Dhanaji Patil
	 */
	
	@PostMapping("_get")
	@ResponseBody
	public ResponseEntity<?> get( @RequestBody RequestData requestData) {
		
		log.debug(String.format("STARTED Get Details SERVICE REQUEST : %1s", requestData.toString()));
		return service.searchRequest(requestData, requestData.getRequestInfo());
		
	}

	/*
	 * enpoint to update service requests
	 * 
	 * @param ServiceReqRequest
	 * 
	 * @author Savita Sutar
	 */

	@RequestMapping(value = "/_update", method = RequestMethod.POST)
	public ResponseEntity<?> update(@RequestBody ServiceRequest serviceRequest,
			@RequestHeader("User-Agent") String requestHeader) throws JSONException {

		ServiceResponse response = service.update(serviceRequest, requestHeader);
		return new ResponseEntity<>(response, HttpStatus.CREATED);		
	}
	
	@RequestMapping(value = "/_scheduler", method = RequestMethod.POST)
	public ResponseEntity<?> scheduler(@RequestParam("tenantId") String tenantId ,
			@RequestBody ServiceRequest requestInfo) 
	{
		ServiceResponse response = service.scheduler(requestInfo, tenantId);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
