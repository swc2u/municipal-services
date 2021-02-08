package org.egov.bookings.controller;

import java.sql.Date;
import java.util.Set;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.JurisdictionAvailabilityRequest;
import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.service.OsujmService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class OsujmController.
 */
@RestController
@RequestMapping("/osujm")
public class OsujmController {

	/** The osujm service. */
	@Autowired
	private OsujmService osujmService;

	/** The bookings fields validator. */
	@Autowired
	private BookingsFieldsValidator bookingsFieldsValidator;

	/**
	 * Gets the jurisdiction fee.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the jurisdiction fee
	 */
	@PostMapping("/fee/_search")
	public ResponseEntity<?> getJurisdictionFee(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validateFeeRequest(bookingsRequest);
		OsujmFeeModel osbmFeeModel = osujmService.findJurisdictionFee(bookingsRequest);

		ResponseModel rs = new ResponseModel();
		if (osbmFeeModel == null) {
			rs.setStatus("400");
			rs.setMessage("Failure while Fetching Fee");
			rs.setData(osbmFeeModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Fee Fetched successfully");
			rs.setData(osbmFeeModel);
		}

		return ResponseEntity.ok(rs);

	}

	/**
	 * Search jurisdiction availability.
	 *
	 * @param jurisdictionAvailabilityRequest the jurisdiction availability request
	 * @return the response entity
	 */
	@PostMapping("/availability/_search")
	public ResponseEntity<?> searchJurisdictionAvailability(
			@RequestBody JurisdictionAvailabilityRequest jurisdictionAvailabilityRequest) {

		bookingsFieldsValidator.validateJurisdictionAvailablityRequest(jurisdictionAvailabilityRequest);
		Set<AvailabilityResponse> res = osujmService.searchJurisdictionAvailability(jurisdictionAvailabilityRequest);

		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Already Booked Dates");
		rs.setData(res);
		return ResponseEntity.ok(rs);

	}
	
	
	@PostMapping("/booked/dates/_fetch")
	public ResponseEntity<?> fetchBookedDates(
			@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validateJurisdictionAvailablityRqst(bookingsRequest);
		Set<Date> res = osujmService.fetchBookedDates(bookingsRequest);

		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Already Booked Dates");
		rs.setData(res);
		return ResponseEntity.ok(rs);

	}
}
