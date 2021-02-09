package org.egov.bookings.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CommercialGrndAvailabilityModel")
@Table(name = "BK_COMMERCIAL_GROUND_AVAILABILITY_LOCK")
public class CommercialGrndAvailabilityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private String id;
	
	@Column(name = "BOOKING_VENUE")
	private String bookingVenue;
	
	@Column(name = "FROM_DATE")
	private Date fromDate;
	
	@Column(name = "TO_DATE")
	private Date toDate;
	
	@Column(name = "ISLOCKED")
	private boolean isLocked;
	
}
