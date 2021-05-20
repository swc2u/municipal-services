package org.egov.bookings.service;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.bookings.contract.RoomFeeFetchRequest;
import org.egov.bookings.contract.RoomFeeFetchResponse;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.RoomMasterModel;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.stereotype.Service;

@Service
public interface RoomsService {

	BookingsModel createRoomForCommunityBooking(BookingsRequest bookingsRequest);

	BigDecimal getRoomAmount(BookingsRequest bookingsRequest);

	boolean isRoomBookingExists(String bkApplicationNumber);

	BookingsModel updateRoomForCommunityBooking(BookingsRequest bookingsRequest);
	
	/**
	 * Community center room availbility fetch.
	 *
	 * @param searchCreteriaFieldDto the search creteria field dto
	 * @return the map
	 */
	public Map<String, String> communityCenterRoomAvailbilityFetch(SearchCriteriaFieldsDTO searchCreteriaFieldDto);

	RoomFeeFetchResponse fetchRoomFee(RoomFeeFetchRequest roomFeeFetchRequest);

}
