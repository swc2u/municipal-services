package org.egov.bookings.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ParkCommunityHallV1MasterModel")
@Table(name = "BK_PARK_COMMUNITY_HALL_v1")
public class ParkCommunityHallV1MasterModel {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "SCCID")
	private String sccid;

	@Column(name = "SCID")
	private String scid;

	@Column(name = "X")
	private String x;

	@Column(name = "Y")
	private String y;

	@Column(name = "Amount")
	private String amount;

	@Column(name = "DimensionSqrYards")
	private String dimensionSqrYards;

	@Column(name = "Rent")
	private String rent;

	@Column(name = "CleaningCharges")
	private String cleaningCharges;

	@Column(name = "surcharge")
	private String surcharge;

	@Column(name = "LuxuryTax")
	private String luxuryTax;

	@Column(name = "Name")
	private String name;

	@Column(name = "Radius")
	private String radius;

	@Column(name = "LocationChangeAmount")
	private String locationChangeAmount;

	@Column(name = "Isactive")
	private Boolean isActive;

	@Column(name = "UTGSTRate")
	private String utgstRate;

	@Column(name = "CGSTRate")
	private String cgstRate;

	@Column(name = "RefundabelSecurity")
	private String refundabelSecurity;

	@Column(name = "NormalType")
	private String normalType;

	@Column(name = "Reviserate1")
	private String reviserate1;

	@Column(name = "Oldrent1")
	private String oldrent1;

	@Column(name = "RentNextSession")
	private String rentNextSession;

	@Column(name = "ImagePath")
	private String imagePath;

	@Column(name = "VenueType")
	private String venueType;

	@Column(name = "BookingAllowedFor")
	private String bookingAllowedFor;
	
	@Column(name = "Sector")
	private String sector;
	
	/** The last modified date. */
	@Column(name = "LAST_MODIFIED_DATE")
	private Date lastModifiedDate;
	
	/** The created date. */
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	/** The from date. */
	@Column(name = "FROM_DATE")
	private Date fromDate;
	
	/** The to date. */
	@Column(name = "TO_DATE")
	private Date toDate;

}
