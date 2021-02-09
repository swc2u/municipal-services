package org.egov.bookings.controller;

import java.util.List;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.OsbmApproverService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class OsbmController.
 */
@RestController
public class OsbmController {

	/** The osbm approver service. */
	@Autowired
	private OsbmApproverService osbmApproverService;
	
	/** The bookings service. */
	@Autowired
	private BookingsService bookingsService;
	
	/** The bookings fields validator. */
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;

	/**
	 * Creates the osbm approver.
	 *
	 * @param osbmApproverRequest the osbm approver request
	 * @return the response entity
	 */
	@PostMapping("/approver/_create")
	public ResponseEntity<?> createOsbmApprover(@RequestBody OsbmApproverRequest osbmApproverRequest) {

		OsbmApproverModel osbmModel = osbmApproverService.createOsbmApprover(osbmApproverRequest);

		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted in osbm table");
		rs.setData(osbmModel);
		return ResponseEntity.ok(rs);

	}

	
	
	/**
	 * Fetch all approver.
	 *
	 * @return the response entity
	 */
	@GetMapping("all/approver/_fetch")
	public ResponseEntity<?> fetchAllApprover() {

		List<BookingApprover> bookingApproverList = bookingsService.fetchAllApprover();

		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(bookingApproverList);
		return ResponseEntity.ok(rs);

	}
	
	
	
}
