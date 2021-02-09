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
 * Instantiates a new osbm search criteria.
 *
 * @param villageCity the village city
 * @param residentialCommercial the residential commercial
 * @param storage the storage
 * @param durationInMonths the duration in months
 * @param constructionType the construction type
 * @param amount the amount
 */
@AllArgsConstructor

/**
 * Instantiates a new osbm search criteria.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class OsbmSearchCriteria implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;
	
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

}
