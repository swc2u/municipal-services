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
@Entity(name = "OsbmApproverModel")
@Table(name = "BK_APPROVER")
public class OsbmApproverModel {

	/** The id. */
	@Id
	@Column(name = "ID")
	private String id;
	
	/** The sector. */
	@Column(name = "SECTOR")
	private String sector;
	
	/** The uuid. */
	@Column(name = "UUID")
	private String uuid;
	
	/** The last modified date. */
	@Column(name = "LAST_MODIFIED_DATE")
	private Date lastModifiedDate;
	
	/** The created date. */
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	/** The created date. */
	@Column(name = "ROLE_CODE")
	private String roleCode;
	
	/** The user id. */
	@Column(name = "USER_ID")
	private Long userId;
}
