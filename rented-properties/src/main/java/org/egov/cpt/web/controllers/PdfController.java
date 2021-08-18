package org.egov.cpt.web.controllers;


import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.egov.cpt.models.PdfSearchCriteria;
import org.egov.cpt.service.pdf.PdfService;
import org.egov.cpt.web.contracts.DuplicateCopyRequest;
import org.egov.cpt.web.contracts.MortgageRequest;
import org.egov.cpt.web.contracts.NoticeGenerationRequest;
import org.egov.cpt.web.contracts.OwnershipTransferRequest;
import org.egov.cpt.web.contracts.PDFAccountStatementRequest;
import org.egov.cpt.web.contracts.PDFPaymentReceiptRequest;
import org.egov.cpt.web.contracts.PropertyRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/pdf")
public class PdfController {

	@Autowired
	private PdfService pdfServcie;

	List<HashMap<String, String>> response;

	/**
	 * 
	 * @param propertyRequest
	 * @param searchCriteria
	 * @return
	 */

	@PostMapping("/_create_allotment")
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody PropertyRequest propertyRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {
		try {
			response = pdfServcie.createPdfReport(searchCriteria,propertyRequest);
		} catch (final Exception e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/_create_account_statement")
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody PDFAccountStatementRequest accountStatementRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {
		try {
			response = pdfServcie.createPdfReport(searchCriteria,accountStatementRequest);
		} catch (final Exception e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	/**
	 * 
	 * @param dcRequest
	 * @param searchCriteria
	 * @return
	 */
	@PostMapping(value={"/_create_dc"})
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody DuplicateCopyRequest dcRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {

		try {
			response = pdfServcie.createPdfReport(searchCriteria,dcRequest);
		} catch (final Exception e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	/**
	 * 
	 * @param mgRequest
	 * @param searchCriteria
	 * @return
	 */
	@PostMapping(value={"/_create_mg"})
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody MortgageRequest mgRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {
		try {
			response = pdfServcie.createPdfReport(searchCriteria,mgRequest);
		} catch (final Exception e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	/**
	 * 
	 * @param otRequest
	 * @param searchCriteria
	 * @return
	 */
	@PostMapping(value={"/_create_ot"})
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody OwnershipTransferRequest otRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {
		try {
			response= pdfServcie.createPdfReport(searchCriteria,otRequest);
		} catch (final Exception e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	/**
	 * 
	 * @param receiptRequest
	 * @param searchCriteria
	 * @return
	 */
	@PostMapping(value={"/_create_payment_receipt"})
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody PDFPaymentReceiptRequest receiptRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {
		try {
			response=pdfServcie.createPdfReport(searchCriteria,receiptRequest);
		} catch (final Exception e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value={"/_create_notice"})
	public ResponseEntity<List<HashMap<String, String>>> create(@Valid @RequestBody NoticeGenerationRequest noticeRequest,@Valid @ModelAttribute PdfSearchCriteria searchCriteria) {
		try {
			response = pdfServcie.createPdfReport(searchCriteria,noticeRequest);
		} catch (JRException e) {
			throw new CustomException("ERROR IN PDF GENERATION","Error while creating PDF"+e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
