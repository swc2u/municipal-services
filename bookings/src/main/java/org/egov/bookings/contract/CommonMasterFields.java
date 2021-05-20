package org.egov.bookings.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMasterFields {
	
	/** The id. */
	private String id;
	
	/** The sector. */
	private String sector;
	
	/** The uuid. */
	private String uuid;
	
	/** The last modified date. */
	private String lastModifiedDate;
	
	/** The created date. */
	private String createdDate;
	
	/** The role code. */
	private String roleCode;
	
	/** The village city. */
	private String villageCity;

	/** The residential commercial. */
	private String residentialCommercial;

	/** The storage. */
	private String storage;

	/** The duration in months. */
	private String durationInMonths;

	/** The construction type. */
	private String constructionType;

	/** The amount. */
	private Long amount;
	
	/** The slab. */
	private String slab;
	
	/** The area from. */
	private Long areaFrom;
	
	/** The area to. */
	private Long areaTo;
	
	/** The rate per sqr feet per day. */
	private Long ratePerSqrFeetPerDay;
	
	/** The locality. */
	private String locality;
	
	/** The category. */
	private String category;
	
	/** The rate per day. */
	private Long ratePerDay;
	
	/** The booking venue. */
	private String bookingVenue;
	
	/** The sccid. */
	private String sccid;

	/** The scid. */
	private String scid;

	/** The x. */
	private String x;

	/** The y. */
	private String y;

	/** The pacc amount. */
	private String paccAmount;

	/** The dimension sqr yards. */
	private String dimensionSqrYards;

	/** The rent. */
	private String rent;

	/** The cleaning charges. */
	private String cleaningCharges;

	/** The surcharge. */
	private String surcharge;

	/** The luxury tax. */
	private String luxuryTax;

	/** The name. */
	private String name;

	/** The radius. */
	private String radius;

	/** The location change amount. */
	private String locationChangeAmount;

	/** The is active. */
	private Boolean isActive;

	/** The utgst rate. */
	private String utgstRate;

	/** The cgst rate. */
	private String cgstRate;

	/** The refundabel security. */
	private String refundabelSecurity;

	/** The normal type. */
	private String normalType;

	/** The reviserate 1. */
	private String reviserate1;

	/** The oldrent 1. */
	private String oldrent1;

	/** The rent next session. */
	private String rentNextSession;

	/** The image path. */
	private String imagePath;

	/** The venue type. */
	private String venueType;

	/** The booking allowed for. */
	private String bookingAllowedFor;
	
	/** The user id. */
	private Long userId;
	
	/** The from date. */
	private String fromDate;
	
	/** The to date. */
	private String toDate;
	
	/** The total number of rooms. */
	private String totalNumberOfRooms;
	
	/** The type of room. */
	private String typeOfRoom;
	
	/** The rent for one day. */
	private String rentForOneDay;
	
	/** The rent for 3 hrs. */
	private String rentFor3Hrs;
	
	/** The rent for 6 hrs. */
	private String rentFor6Hrs;
	
	/** The rent for 9 hrs. */
	private String rentFor9Hrs;
	
	/** The community center name. */
	private String communityCenterName;
	
}
