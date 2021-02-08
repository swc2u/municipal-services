package org.egov.bookings.contract;

import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * The Class CommercialGroundAvailabiltySearchCriteria.
 */
/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new commercial ground availabilty search criteria.
 *
 * @param date         the date
 * @param bookingVenue the booking venue
 */

/**
 * Instantiates a new commercial ground availabilty search criteria.
 *
 * @param date         the date
 * @param bookingType  the booking type
 * @param bookingVenue the booking venue
 */
@AllArgsConstructor

/**
 * Instantiates a new commercial ground availabilty search criteria.
 */

/**
 * Instantiates a new commercial ground availabilty search criteria.
 */
@NoArgsConstructor

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */
@Builder
public class CommercialGroundAvailabiltySearchCriteria implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The booking type. */
	private String bookingType;

	/** The booking venue. */
	private String bookingVenue;

	
}
