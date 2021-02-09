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
 * Instantiates a new booking approver.
 *
 * @param userName the user name
 * @param name the name
 * @param mobileNumber the mobile number
 * @param uuid the uuid
 * @param id the id
 */
@AllArgsConstructor

/**
 * Instantiates a new booking approver.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class BookingApprover implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3683257254333784296L;
	
	/** The user name. */
	private String userName;
	
	/** The name. */
	private String name;
	
	/** The mobile number. */
	private String mobileNumber;
	
	/** The uuid. */
	private String uuid;
	
	/** The id. */
	private Long id;
	
}
