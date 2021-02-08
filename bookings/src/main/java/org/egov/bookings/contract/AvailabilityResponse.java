package org.egov.bookings.contract;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import org.egov.bookings.model.TimeslotsModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * Instantiates a new availability response.
 */

/**
 * Instantiates a new availability response.
 *
 * @param fromDate the from date
 * @param toDate the to date
 * @param timeslots the timeslots
 */
@AllArgsConstructor

/**
 * Instantiates a new availability response.
 */

/**
 * Instantiates a new availability response.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class AvailabilityResponse implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3683257254333784296L;
	
	/** The from date. */
	private Date fromDate;
	
	/** The to date. */
	private Date toDate;
	
	/** The timeslots. */
	private List<TimeslotsModel> timeslots;
}
