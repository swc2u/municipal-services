package org.egov.bookings.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "BookingLockModel")
@Table(name = "BK_BOOKING_LOCK_DATES")
public class BookingLockModel {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "APPLICATION_NUMBER")
	private String applicationNumber;

	@Column(name = "SECTOR")
	private String sector;

	@Column(name = "BOOKING_VENUE")
	private String bookingVenue;

	@Column(name = "BOOKING_TYPE")
	private String bookingType;

	@Column(name = "CREATED_DATE")
	private String createdDate;

	@Column(name = "LAST_MODIFIED_DATE")
	private String lastModifiedDate;

}
