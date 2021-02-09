package org.egov.bookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new inventory model.
 */
@NoArgsConstructor

/**
 * Instantiates a new inventory model.
 *
 * @param id                   the id
 * @param Amount               the amount
 * @param DimensionSqrYards    the dimension sqr yards
 * @param Rent                 the rent
 * @param CleaningCharges      the cleaning charges
 * @param surcharge            the surcharge
 * @param LuxuryTax            the luxury tax
 * @param Name                 the name
 * @param Isactive             the isactive
 * @param Radius               the radius
 * @param LocationChangeAmount the location change amount
 * @param UTGSTRate            the UTGST rate
 * @param CGSTRate             the CGST rate
 * @param RefundableSecurity   the refundable security
 * @param NormalType           the normal type
 * @param ImagePath            the image path
 * @param SectorId             the sector id
 * @param SectorName           the sector name
 * @param VenueType            the venue type
 */
@AllArgsConstructor
@Entity(name = "TM_PARK_COMMUNITY_HALL")
@Table(name = "TM_PARK_COMMUNITY_HALL")
public class InventoryModel {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Long id;

	/** The Amount. */
	@Column(name = "Amount")
	private String Amount;

	/** The Dimension sqr yards. */
	@Column(name = "DimensionSqrYards")
	private String DimensionSqrYards;

	/** The Rent. */
	@Column(name = "Rent")
	private String Rent;

	/** The Cleaning charges. */
	@Column(name = "CleaningCharges")
	private String CleaningCharges;

	/** The surcharge. */
	@Column(name = "surcharge")
	private String surcharge;

	/** The Luxury tax. */
	@Column(name = "LuxuryTax")
	private String LuxuryTax;

	/** The Name. */
	@Column(name = "Name")
	private String Name;

	/** The Isactive. */
	@Column(name = "Isactive")
	private Boolean Isactive;

	/** The Radius. */
	@Column(name = "Radius")
	private String Radius;

	/** The Location change amount. */
	@Column(name = "LocationChangeAmount")
	private String LocationChangeAmount;

	/** The UTGST rate. */
	@Column(name = "UTGSTRate")
	private String UTGSTRate;

	/** The CGST rate. */
	@Column(name = "CGSTRate")
	private String CGSTRate;

	/** The Refundable security. */
	@Column(name = "RefundableSecurity")
	private String RefundableSecurity;

	/** The Normal type. */
	@Column(name = "NormalType")
	private String NormalType;

	/** The Image path. */
	@Column(name = "ImagePath")
	private String ImagePath;

	/** The Sector id. */
	@Column(name = "SectorId")
	private String SectorId;

	/** The Sector name. */
	@Column(name = "SectorName")
	private String SectorName;

	/** The Venue type. */
	@Column(name = "VenueType")
	private String VenueType;

}
