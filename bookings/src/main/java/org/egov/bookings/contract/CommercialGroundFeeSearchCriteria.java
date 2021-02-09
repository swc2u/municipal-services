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
 * Instantiates a new commercial ground fee search criteria.
 *
 * @param locality the locality
 * @param category the category
 */
@AllArgsConstructor

/**
 * Instantiates a new commercial ground fee search criteria.
 */
@NoArgsConstructor

/**
 * Builder.
 *
 * @return the commercial ground fee search criteria builder
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class CommercialGroundFeeSearchCriteria implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The locality. */
	private String locality;

	/** The category. */
	private String category;

	
	private String bookingVenue;
}
