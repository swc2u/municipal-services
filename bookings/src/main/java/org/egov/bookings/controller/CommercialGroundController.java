package org.egov.bookings.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.service.CommercialGroundService;
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
 * The Class CommercialGroundController.
 */
@RestController
@RequestMapping("/commercial/ground")
public class CommercialGroundController {
	
	/** The bookings fields validator. */
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	/** The commercial ground fee service. */
	@Autowired
	CommercialGroundService  commercialGroundService;
	
	/**
	 * Search commercial ground fee.
	 *
	 * @param commercialGroundFeeSearchCriteria the commercial ground fee search criteria
	 * @return the response entity
	 */
	@PostMapping("/fee/_search")
	private ResponseEntity<?> searchCommercialGroundFee(
			@RequestBody CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria) {
		
		bookingsFieldsValidator.validateCommercialGroundCriteria(commercialGroundFeeSearchCriteria);
		
		CommercialGroundFeeModel res = commercialGroundService.searchCommercialGroundFee(commercialGroundFeeSearchCriteria);
		
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data Found");
		rs.setData(res);
		
		return ResponseEntity.ok(rs);
	}
	
	
	/**
	 * Search commercial ground availabilty.
	 *
	 * @param commercialGroundAvailabiltySearchCriteria the commercial ground availabilty search criteria
	 * @return the response entity
	 */
	@PostMapping("/availability/_search")
	private ResponseEntity<?> searchCommercialGroundAvailabilty(
			@RequestBody CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria) {
		
		bookingsFieldsValidator.validateCommercialGroundAvailabilityCriteria(commercialGroundAvailabiltySearchCriteria);
		
		
		Set<AvailabilityResponse> res = commercialGroundService.searchCommercialGroundAvailabilty(commercialGroundAvailabiltySearchCriteria);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Already Booked Dates");
		rs.setData(res);
		
		return ResponseEntity.ok(rs);
	}
	
	
	
	/**
	 * Lock commercial availability.
	 *
	 * @param commercialGrndAvailabilityModel the commercial grnd availability model
	 * @return the response entity
	 */
	@PostMapping("/availability/_lock")
	private ResponseEntity<?> lockCommercialAvailability(
			@RequestBody CommercialGrndAvailabilityModel commercialGrndAvailabilityModel) {
		
		bookingsFieldsValidator.validateCommercialGroundAvailabilityModel(commercialGrndAvailabilityModel);
		
		CommercialGrndAvailabilityModel res = commercialGroundService.lockCommercialAvailability(commercialGrndAvailabilityModel);
		
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Already Booked Dates");
		rs.setData(res);
		
		return ResponseEntity.ok(rs);
	}

	
	
	@PostMapping("/booked/dates/_search")
	private ResponseEntity<?> fetchBookedDates(
			@RequestBody BookingsRequest bookingsRequest) {
		
		bookingsFieldsValidator.validateGrndAvailabilityRequest(bookingsRequest);
		
		
		Set<Date> res = commercialGroundService.fetchBookedDates(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Already Booked Dates");
		rs.setData(res);
		
		return ResponseEntity.ok(rs);
	}
	
	
}
