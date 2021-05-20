package org.egov.bookings.service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.ParkAndCommunitySearchCriteria;
import org.egov.bookings.contract.ParkCommunityFeeMasterRequest;
import org.egov.bookings.contract.ParkCommunityFeeMasterResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.web.models.BookingsRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface ParkAndCommunityService.
 */
public interface ParkAndCommunityService {

	/**
	 * Creates the park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	BookingsModel createParkAndCommunityBooking(BookingsRequest bookingsRequest);

	/**
	 * Update park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	BookingsModel updateParkAndCommunityBooking(BookingsRequest bookingsRequest);

	/**
	 * Fetch park community master.
	 *
	 * @param parkCommunityFeeMasterRequest the park community fee master request
	 * @return the list
	 */
	List<ParkCommunityHallV1MasterModel> fetchParkCommunityMaster(ParkCommunityFeeMasterRequest parkCommunityFeeMasterRequest);


	/**
	 * Availability search.
	 *
	 * @param parkAndCommunitySearchCriteria the park and community search criteria
	 * @return the sets the
	 */
	Set<AvailabilityResponse> availabilitySearch(ParkAndCommunitySearchCriteria parkAndCommunitySearchCriteria);

	/**
	 * Fetch booked dates.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the sets the
	 */
	Set<Date> fetchBookedDates(BookingsRequest bookingsRequest);

	/**
	 * Find park and community fee.
	 *
	 * @param string the string
	 * @return the park community hall V 1 master model
	 */
	ParkCommunityHallV1MasterModel findParkAndCommunityFee(String string);

	/**
	 * Fetch amount.
	 *
	 * @param parkCommunityFeeMasterRequest the park community fee master request
	 * @return the park community fee master response
	 */
	ParkCommunityFeeMasterResponse fetchAmount(ParkCommunityFeeMasterRequest parkCommunityFeeMasterRequest);
	
	/**
	 * Fetch sector.
	 *
	 * @param venueType the venue type
	 * @return the list
	 */
	List<ParkCommunityHallV1MasterModel> fetchSector(String venueType);

}
