package org.egov.bookings.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.egov.bookings.model.OsujmFeeModel.OsujmFeeModelBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "OsujmNewLocationModel")
@Table(name = "BK_OSUJM_NEW_LOCATION")
public class OsujmNewLocationModel {

	@Id
	@Column(name = "APPLICATION_NUMBER")
	private String applicationNumber;

	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "APPLICANT_NAME")
	private String applicantName;

	@Column(name = "APPLICANT_ADDRESS")
	private String applicantAddress;

	@Valid
	@JsonProperty("wfDocuments")
	@Transient
	private List<Document> wfDocuments;

	@Column(name = "AREA_REQUIREMENT")
	private String areaRequirement;

	@Column(name = "SECTOR")
	private String sector;

	@Column(name = "LOCALITY_ADDRESS")
	private String localityAddress;

	@Column(name = "LANDMARK")
	private String landmark;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "CONTACT")
	private String contact;

	@Column(name = "ID_PROOF")
	private String idProof;

	@Column(name = "MAIL_ADDRESS")
	private String mailAddress;
	
	@Column(name = "TENANT_ID")
	private String tenantId;

	@Column(name = "BUSINESS_SERVICE")
	private String businessService;
	
	@Size(max = 64)
	@JsonProperty("assignee")
	@Transient
	private String assignee = null;
	
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "APPLICATION_STATUS")
	private String applicationStatus;
	
	@Column(name = "DATE_CREATED")
	private Date dateCreated;
	
	@Column(name = "UUID")
	private String uuid;
	
}
