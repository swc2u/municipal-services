package org.egov.bookings.model;

import java.util.Date;

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
@Entity(name = "CommercialGroundFeeModel")
@Table(name = "BK_COMMERCIAL_GROUND_FEE")
public class CommercialGroundFeeModel {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private String id;
	
	/** The locality. */
	@Column(name = "LOCALITY")
	private String locality;
	
	/** The category. */
	@Column(name = "CATEGORY")
	private String category;
	
	/** The rate per day. */
	@Column(name = "RATE_PER_DAY")
	private Long ratePerDay;
	
	/** The booking venue. */
	@Column(name = "BOOKING_VENUE")
	private String bookingVenue;
	
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
	
	@Column(name = "RefundabelSecurity")
	private String refundabelSecurity;
}
