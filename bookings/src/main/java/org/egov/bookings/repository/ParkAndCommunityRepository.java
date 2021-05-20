package org.egov.bookings.repository;

import java.sql.Date;
import java.util.Set;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.repository.querybuilder.BookingsQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkAndCommunityRepository extends CrudRepository<BookingsModel, String> {

	/**
	 * Fetch booked dates of park and community.
	 *
	 * @param bookingVenue the booking venue
	 * @param bookingType the booking type
	 * @param sector the sector
	 * @param date the date
	 * @param SUCCESS the success
	 * @param applicationNumber the application number
	 * @return the sets the
	 */
	@Query(value = BookingsQueryBuilder.CHECK_PARK_AND_COMMUNITY_AVAILABILITY, nativeQuery = true)
	Set<BookingsModel> fetchBookedDatesOfParkAndCommunity(@Param(BookingsConstants.BOOKING_VENUE) String bookingVenue,
			@Param(BookingsConstants.BOOKING_TYPE) String bookingType, @Param(BookingsConstants.SECTOR) String sector,
			@Param(BookingsConstants.DATE) Date date, @Param(BookingsConstants.PAYMENT_SUCCESS_STATUS) String SUCCESS,
			@Param(BookingsConstants.APPLICATION_NUMBER) String applicationNumber);


	
}
