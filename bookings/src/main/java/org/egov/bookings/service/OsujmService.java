package org.egov.bookings.service;

import java.sql.Date;
import java.util.Set;

import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.JurisdictionAvailabilityRequest;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.web.models.BookingsRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface OsujmService.
 */
public interface OsujmService {

	/**
	 * Find jurisdiction fee.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the osujm fee model
	 */
	OsujmFeeModel findJurisdictionFee(BookingsRequest bookingsRequest);

	/**
	 * Search jurisdiction availability.
	 *
	 * @param jurisdictionAvailabilityRequest the jurisdiction availability request
	 * @return the sets the
	 */
	Set<AvailabilityResponse> searchJurisdictionAvailability(
			JurisdictionAvailabilityRequest jurisdictionAvailabilityRequest);

	Set<Date> fetchBookedDates(BookingsRequest bookingsRequest);

}
