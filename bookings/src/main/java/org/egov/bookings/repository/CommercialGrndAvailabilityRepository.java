package org.egov.bookings.repository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.repository.querybuilder.BookingsQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialGrndAvailabilityRepository extends JpaRepository<CommercialGrndAvailabilityModel, String> {

	//CommercialGrndAvailabilityModel findByBookingVenueAndIsLocked(String bookingVenue, boolean b);

	@Query(value = BookingsQueryBuilder.FIND_COMMERCIAL_GRND_LOCK_DATES, nativeQuery = true)
	public Set<CommercialGrndAvailabilityModel> findByBookingVenueAndIsLocked(@Param(BookingsConstants.BOOKING_VENUE) String bookingVenue,
			@Param(BookingsConstants.DATE) Date date);

	@Query(value = BookingsQueryBuilder.FIND_LOCK_DATES_FROM_NOW_TO_SIX_MONTHS, nativeQuery = true)
	public List<CommercialGrndAvailabilityModel> findLockedDatesFromNowTo6Months(@Param(BookingsConstants.DATE) Date date,
			@Param(BookingsConstants.SIX_MONTHS_FROM_NOW_SQL) Date sixMonthsFromNowSql);

	

}
