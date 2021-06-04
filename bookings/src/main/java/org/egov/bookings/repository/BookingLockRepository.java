package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.BookingLockModel;
import org.egov.bookings.repository.querybuilder.BookingsQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingLockRepository extends CrudRepository<BookingLockModel, String> {

	long deleteByApplicationNumber(String bkApplicationNumber);

	@Query(value = BookingsQueryBuilder.FETCH_LOCK_DATES, nativeQuery = true)
	List<BookingLockModel> fetchLockedDates(@Param(BookingsConstants.BOOKING_VENUE) String bookingVenue,
			@Param(BookingsConstants.BOOKING_TYPE) String bookingType,
			@Param(BookingsConstants.SECTOR) String bkSector);

	@Query(value = BookingsQueryBuilder.FETCH_LOCK_DATES_FOR_COMMERCIAL, nativeQuery = true)
	List<BookingLockModel> fetchLockedDatesForCommercial(@Param(BookingsConstants.BOOKING_VENUE) String bookingVenue,
			@Param(BookingsConstants.BOOKING_TYPE) String bookingType);

}
