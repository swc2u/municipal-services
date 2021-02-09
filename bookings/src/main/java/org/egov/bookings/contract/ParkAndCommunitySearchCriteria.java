package org.egov.bookings.contract;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new park and community search criteria.
 */
@NoArgsConstructor

/**
 * Instantiates a new park and community search criteria.
 *
 * @param bookingType the booking type
 * @param bookingVenue the booking venue
 * @param sector the sector
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class ParkAndCommunitySearchCriteria implements Serializable{

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The booking type. */
	private String bookingType;

	/** The booking venue. */
	private String bookingVenue;

	/** The sector. */
	private String sector;
	
	/** The application number. */
	private String applicationNumber;

}
