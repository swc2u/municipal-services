package org.egov.assets.web.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.assets.model.IndentRequest;
import org.egov.assets.model.IndentResponse;
import org.egov.assets.model.IndentSearch;
import org.egov.assets.model.PDFResponse;
import org.egov.assets.model.PDFRequest;
import org.egov.assets.service.IndentService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/indents")
public class IndentsApiController {
	@Autowired
	private IndentService indentService;

	@PostMapping(value = "/_create", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<IndentResponse> indentsCreatePost(
			@NotNull @RequestParam(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody IndentRequest indentRequest) {
		IndentResponse response = indentService.create(indentRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PostMapping(value = "/_search", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<IndentResponse> indentsSearchPost(
			@NotNull @RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestBody RequestInfo requestInfo,
			@Size(max = 50) @RequestParam(value = "ids", required = false) List<String> ids,
			@RequestParam(value = "issueStore", required = false) String issueStore,
			@RequestParam(value = "indentStore", required = false) String indentStore,
			@RequestParam(value = "indentDate", required = false) Long indentDate,
			@RequestParam(value = "indentNumber", required = false) String indentNumber,
			@RequestParam(value = "indentPurpose", required = false) String indentPurpose,
			@RequestParam(value = "indentStatus", required = false) String indentStatus,
			@RequestParam(value = "totalIndentValue", required = false) BigDecimal totalIndentValue,
			@RequestParam(value = "indentType", required = false) String indentType,
			@RequestParam(value = "searchPurpose", required = false) String searchPurpose,
			@RequestParam(value = "inventoryType", required = false) String inventoryType,
			@Min(0) @Max(100) @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {

		IndentSearch is = new IndentSearch().builder().tenantId(tenantId).ids(ids).indentDate(indentDate)
				.indentStore(indentStore).indentNumber(indentNumber).indentPurpose(indentPurpose)
				.inventoryType(inventoryType).issueStore(issueStore).indentType(indentType).searchPurpose(searchPurpose)
				.build();
		IndentResponse response = indentService.search(is, requestInfo);
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PostMapping(value = "/_print", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<PDFResponse> indentsPrintPost(@Valid @RequestBody PDFRequest pdfRequest,
			@NotNull @RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "indentNumber", required = true) String indentNumber,
			@RequestParam(value = "indentType", required = true) String indentType) {
		IndentSearch is = new IndentSearch().builder().tenantId(tenantId).indentNumber(indentNumber)
				.indentType(indentType).build();
		PDFResponse response = indentService.printPdf(is, pdfRequest.getRequestInfo());
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PostMapping(value = "/_update", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<IndentResponse> indentsUpdatePost(
			@NotNull @RequestParam(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody IndentRequest indentRequest) {
		IndentResponse response = indentService.update(indentRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PostMapping(value = "/_updateStatus", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<IndentResponse> indentsUpdateStatusPost(
			@NotNull @RequestParam(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody IndentRequest indentRequest) {
		IndentResponse response = indentService.updateStatus(indentRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}

}