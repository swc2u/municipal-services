package org.egov.tlcalculator.web.controllers;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.service.CTLBillingslabService;
import org.egov.tlcalculator.validator.CTLBillingslabValidator;
import org.egov.tlcalculator.web.models.CTLBillingSlabReq;
import org.egov.tlcalculator.web.models.CTLBillingSlabRes;
import org.egov.tlcalculator.web.models.CTLBillingSlabSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ctlbillingslab")
public class CTLBillingslabController {
	@Autowired
	private CTLBillingslabValidator ctlBillingslabValidator;

	@Autowired
	private CTLBillingslabService service;

	/**
	 * Creates Billing Slabs for Fine master
	 * 
	 * @param billingSlabReq
	 * @return
	 */
	@RequestMapping(value = "/_create", method = RequestMethod.POST)
	public ResponseEntity<CTLBillingSlabRes> billingslabCreatePost(
			@Valid @RequestBody CTLBillingSlabReq ctlBillingSlabReq) {
		ctlBillingslabValidator.validateCreate(ctlBillingSlabReq);
		CTLBillingSlabRes response = service.createSlabs(ctlBillingSlabReq);
		return new ResponseEntity<CTLBillingSlabRes>(response, HttpStatus.OK);
	}

	/**
	 * Searches Billing Slabs belonging TradeLicense based on criteria
	 * 
	 * @param billingSlabSearchCriteria
	 * @param requestInfo
	 * @return
	 */
	@RequestMapping(value = { "/{servicename}/_search", "/_search" }, method = RequestMethod.POST)
	public ResponseEntity<CTLBillingSlabRes> billingslabSearchPost(
			@ModelAttribute @Valid CTLBillingSlabSearchCriteria ctlBillingSlabSearchCriteria,
			@Valid @RequestBody RequestInfo requestInfo) {
		CTLBillingSlabRes response = service.searchSlabs(ctlBillingSlabSearchCriteria, requestInfo);
		return new ResponseEntity<CTLBillingSlabRes>(response, HttpStatus.OK);
	}

	/**
	 * Updates Billing Slabs of Fine Master
	 * 
	 * @param billingSlabReq
	 * @return
	 */
	@RequestMapping(value = "/_update", method = RequestMethod.POST)
	public ResponseEntity<CTLBillingSlabRes> billingslabUpdatePost(
			@Valid @RequestBody CTLBillingSlabReq billingSlabReq) {
		ctlBillingslabValidator.validateUpdate(billingSlabReq);
		CTLBillingSlabRes response = service.updateSlabs(billingSlabReq);
		return new ResponseEntity<CTLBillingSlabRes>(response, HttpStatus.OK);
	}

}
