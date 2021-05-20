package org.egov.bookings.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.CommercialGrndAvailabiltyLockRequest;
import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.service.CommercialGroundService;
import org.egov.bookings.service.impl.EnrichmentService;
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
	private BookingsFieldsValidator bookingsFieldsValidator;
	
	/** The commercial ground fee service. */
	@Autowired
	private CommercialGroundService  commercialGroundService;
	
	
	
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
	private ResponseEntity<?> saveCommercialAvailabilityLockDates(
			@RequestBody CommercialGrndAvailabiltyLockRequest commercialGrndAvailabiltyLockRequest) {
		
		bookingsFieldsValidator.validateCommercialGroundAvailabilityModel(commercialGrndAvailabiltyLockRequest);
		if (!BookingsFieldsValidator.isNullOrEmpty(commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock())) {
			commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock().forEach(lock -> {
				lock.setId(UUID.randomUUID().toString());
			});
		}
		List<CommercialGrndAvailabilityModel> res = commercialGroundService.saveCommercialAvailabilityLockDates(commercialGrndAvailabiltyLockRequest);
		
		ResponseModel rs = new ResponseModel();
		if (null != res) {
			rs.setStatus("200");
			rs.setMessage("Dates Locked");
			rs.setData(res);
		}
		else {
			rs.setStatus("200");
			rs.setMessage("Already Booked");
			rs.setData(res);
		}
		
		return ResponseEntity.ok(rs);
	}

	
	
	/**
	 * Fetch booked dates.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
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
	
	
	/**
	 * Update commercial availability lock dates.
	 *
	 * @param commercialGrndAvailabiltyLockRequest the commercial grnd availabilty lock request
	 * @return the response entity
	 */
	@PostMapping("/updateAvailability/_lock")
	private ResponseEntity<?> updateCommercialAvailabilityLockDates(
			@RequestBody CommercialGrndAvailabiltyLockRequest commercialGrndAvailabiltyLockRequest) {

		bookingsFieldsValidator.validateCommercialGroundAvailabilityUpdateModel(commercialGrndAvailabiltyLockRequest);

		List<CommercialGrndAvailabilityModel> res = commercialGroundService
				.updateCommercialAvailabilityLockDates(commercialGrndAvailabiltyLockRequest);

		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Dates Unlocked");
		rs.setData("");

		return ResponseEntity.ok(rs);
	}
	
	
	
	/**
	 * Fetch locked dates.
	 *
	 * @return the response entity
	 */
	@PostMapping("/lock/dates/_fetch")
	private ResponseEntity<?> fetchLockedDates() {

		List<CommercialGrndAvailabilityModel> res = commercialGroundService.fetchLockedDates();
		ResponseModel rs = null;
		if (!BookingsFieldsValidator.isNullOrEmpty(res)) {
		    rs = new ResponseModel();
			rs.setStatus("200");
			rs.setMessage("Locked Dates");
			rs.setData(res);
		}
		else {
			rs = new ResponseModel();
			rs.setStatus("200");
			rs.setMessage("There is not any Locked Dates");
			rs.setData(res);
		}

		return ResponseEntity.ok(rs);
	}
	
}
