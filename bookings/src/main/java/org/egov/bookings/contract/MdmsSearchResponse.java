package org.egov.bookings.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmsSearchResponse {
	
	/** The booking roles. */
	@JsonProperty("BookingRoles")
	List<MdmsJsonFields> bookingRoles;

	/**
	 * Gets the booking roles.
	 *
	 * @return the booking roles
	 */
	public List<MdmsJsonFields> getBookingRoles() {
		return bookingRoles;
	}

	public void setBookingRoles(List<MdmsJsonFields> bookingRoles) {
		this.bookingRoles = bookingRoles;
	}
}
