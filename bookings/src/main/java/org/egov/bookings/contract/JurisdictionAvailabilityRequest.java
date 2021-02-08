package org.egov.bookings.contract;

import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JurisdictionAvailabilityRequest implements Serializable {

	private static final long serialVersionUID = -3683257254333784296L;

	private String bkSector;

	private String bookingVenue;
	
	private String bookingType;
	
	
}
