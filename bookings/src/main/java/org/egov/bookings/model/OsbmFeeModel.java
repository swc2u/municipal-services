package org.egov.bookings.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new osbm fee model.
 */
@NoArgsConstructor

/**
 * Instantiates a new osbm fee model.
 *
 * @param id the id
 * @param villageCity the village city
 * @param residentialCommercial the residential commercial
 * @param storage the storage
 * @param durationInMonths the duration in months
 * @param constructionType the construction type
 * @param amount the amount
 */
@AllArgsConstructor
@Entity(name = "OsbmFeeModel")
@Table(name = "BK_OSBM_FEE")
public class OsbmFeeModel {

	/** The id. */
	@Id
	@Column(name = "ID")
	private String id;

	/** The village city. */
	@Column(name = "VILLAGE_CITY")
	private String villageCity;

	/** The residential commercial. */
	@Column(name = "RESIDENTIAL_COMMERCIAL")
	private String residentialCommercial;

	/** The storage. */
	@Column(name = "STORAGE")
	private String storage;

	/** The duration in months. */
	@Column(name = "DURATION_IN_MONTHS")
	private String durationInMonths;

	/** The construction type. */
	@Column(name = "CONSTRUCTION_TYPE")
	private String constructionType;

	/** The amount. */
	@Column(name = "AMOUNT")
	private Long amount;
	
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
