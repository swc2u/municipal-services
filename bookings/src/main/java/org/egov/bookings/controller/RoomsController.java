package org.egov.bookings.controller;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.contract.RoomFeeFetchRequest;
import org.egov.bookings.contract.RoomFeeFetchResponse;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.RoomMasterModel;
import org.egov.bookings.service.RoomsService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community/room")
public class RoomsController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger( RoomsController.class.getName() );
	
	@Autowired
	private BookingsFieldsValidator bookingsFieldsValidator;

	@Autowired
	private RoomsService roomsService;
	
	@PostMapping("_create")
	private ResponseEntity<?> createRoomForCommunityBooking(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validateRoomBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = roomsService.createRoomForCommunityBooking(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Creating Room for Community Booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Community Room Booking Created Successfully");
			rs.setData(bookingsModel);
		}

		return ResponseEntity.ok(rs);
	}

	
	@PostMapping("_update")
	private ResponseEntity<?> updateRoomForCommunityBooking(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validateRoomBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = roomsService.updateRoomForCommunityBooking(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while updating Room fo Community Booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Community Room Booking updated Successfully");
			rs.setData(bookingsModel);
		}

		return ResponseEntity.ok(rs);
	}

	/**
	 * Community center room availbility fetch.
	 *
	 * @param roomAvailabilityFetchDto the room availability fetch dto
	 * @return the response entity
	 */
	@PostMapping(value = "/availability/_fetch")
	public ResponseEntity<?> communityCenterRoomAvailbilityFetch(@RequestBody SearchCriteriaFieldsDTO searchCreteriaFieldDto){
		if (BookingsFieldsValidator.isNullOrEmpty(searchCreteriaFieldDto)) 
		{
			throw new IllegalArgumentException("Invalid searchCreteriaFieldDto");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(searchCreteriaFieldDto.getRequestInfo())) 
		{
			throw new IllegalArgumentException("Invalid RequestInfo");
		}
		ResponseModel rs = new ResponseModel();
		try {
			Map<String, String> typesOfRoomMap = roomsService.communityCenterRoomAvailbilityFetch(searchCreteriaFieldDto);
			rs.setStatus("200");
			rs.setMessage("communityCenterRoomAvailbilityFetch data");
			rs.setData(typesOfRoomMap);
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the communityCenterRoomAvailbilityFetch " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}

	
	
	
	@PostMapping("/fee/_fetch")
	private ResponseEntity<?> fetchRoomFee(
			@RequestBody RoomFeeFetchRequest roomFeeFetchRequest) {
		
		bookingsFieldsValidator.validateRoomFeeFetchRequest(roomFeeFetchRequest);
		
		RoomFeeFetchResponse res = roomsService.fetchRoomFee(roomFeeFetchRequest);
		
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data Found");
		rs.setData(res);
		
		return ResponseEntity.ok(rs);
	}
	
}
