package org.egov.bookings.controller;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.ParkAndCommunitySearchCriteria;
import org.egov.bookings.contract.ParkCommunityFeeMasterRequest;
import org.egov.bookings.contract.ParkCommunityFeeMasterResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.service.DemandService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.service.impl.EnrichmentService;
import org.egov.bookings.service.impl.UserService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class ParkAndCommunityController.
 */
@RestController
@RequestMapping("/park/community")
public class ParkAndCommunityController {

	/** The park and community service. */
	@Autowired
	private ParkAndCommunityService parkAndCommunityService;

	/** The env. */
	@Autowired
	private Environment env;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	/** The bookings fields validator. */
	@Autowired
	private BookingsFieldsValidator bookingsFieldsValidator;

	@Autowired
	private DemandService demandService;

	/**
	 * Creates the park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping("_create")
	private ResponseEntity<?> createParkAndCommunityBooking(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validatePAndCBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = parkAndCommunityService.createParkAndCommunityBooking(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Creating Park And Community Booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage(" Park And Community Booking Created Successfully");
			rs.setData(bookingsModel);
		}

		return ResponseEntity.ok(rs);
	}

	/**
	 * Update park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping("_update")
	private ResponseEntity<?> updateParkAndCommunityBooking(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validatePAndCBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = parkAndCommunityService.updateParkAndCommunityBooking(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Updating Park And Community Booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Park And Community Booking Updated Successfully ");
			rs.setData(bookingsModel);
		}

		return ResponseEntity.ok(rs);
	}

	/**
	 * Fetch park community master.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @return the response entity
	 */
	@PostMapping("/master/_fetch")
	private ResponseEntity<?> fetchParkCommunityMaster(
			@RequestBody ParkCommunityFeeMasterRequest parkCommunityFeeMasterRequest) {

		bookingsFieldsValidator.validateParkAndCommunityMasterRequest(parkCommunityFeeMasterRequest);

		List<ParkCommunityHallV1MasterModel> parkCommunityHallV1MasterList = parkAndCommunityService
				.fetchParkCommunityMaster(parkCommunityFeeMasterRequest);

		ResponseModel rs = new ResponseModel();
		if (parkCommunityHallV1MasterList == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Fetching Park And Community Master Data");
			rs.setData(parkCommunityHallV1MasterList);
		} else {
			rs.setStatus("200");
			rs.setMessage("Park And Community Master Data Fetched Successfully ");
			rs.setData(parkCommunityHallV1MasterList);
		}

		return ResponseEntity.ok(rs);
	}

	@PostMapping("/availability/_search")
	private ResponseEntity<?> availabilitySearch(
			@RequestBody ParkAndCommunitySearchCriteria parkAndCommunitySearchCriteria) {

		bookingsFieldsValidator.validatePACCSearchCriteria(parkAndCommunitySearchCriteria);
		Set<AvailabilityResponse> parkCommunityHallV1MasterList = parkAndCommunityService
				.availabilitySearch(parkAndCommunitySearchCriteria);
		ResponseModel rs = new ResponseModel();
		if (parkCommunityHallV1MasterList == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Fetching Park And Community Availability Date");
			rs.setData(parkCommunityHallV1MasterList);
		} else {
			rs.setStatus("200");
			rs.setMessage("Park And Community Availability Date Fetched Successfully ");
			rs.setData(parkCommunityHallV1MasterList);
		}

		return ResponseEntity.ok(rs);
	}

	@PostMapping("/booked/dates/_search")
	private ResponseEntity<?> fetchBookedDates(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validateGrndAvailabilityRequest(bookingsRequest);

		Set<Date> res = parkAndCommunityService.fetchBookedDates(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Already Booked Dates");
		rs.setData(res);

		return ResponseEntity.ok(rs);
	}

	@PostMapping("/amount/_fetch")
	private ResponseEntity<?> fetchAmount(@RequestBody ParkCommunityFeeMasterRequest parkCommunityFeeMasterRequest) {

		// bookingsFieldsValidator.validateGrndAvailabilityRequest(bookingsRequest);

		ParkCommunityFeeMasterResponse res = parkAndCommunityService.fetchAmount(parkCommunityFeeMasterRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Amount Fetched");
		rs.setData(res);

		return ResponseEntity.ok(rs);
	}
}
