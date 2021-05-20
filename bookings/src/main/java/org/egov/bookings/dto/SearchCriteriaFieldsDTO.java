package org.egov.bookings.dto;

import java.io.Serializable;
import java.sql.Date;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchCriteriaFieldsDTO.
 */
public class SearchCriteriaFieldsDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The tenant id. */
	private String tenantId;
	
	/** The application number. */
	private String applicationNumber;
	
	/** The application status. */
	private String applicationStatus;
	
	/** The from date. */
	private Date fromDate;
	
	/** The to date. */
	private Date toDate;
	
	/** The mobile number. */
	private String mobileNumber;
	
	/** The role code. */
	private String roleCode;
	
	/** The user id. */
	private int userId;
	
	/** The uu id. */
	private String uuid;
	
	/** The sector. */
	private String sector;
	
	/** The booking type. */
	private String bookingType;
	
	/** The request info. */
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	/** The action. */
	private String action;
	
	/** The business service. */
	private String businessService;
	
	/** The type of room. */
	private String typeOfRoom;
	
	/** The str from date. */
	private String strFromDate;
	
	/** The str to date. */
	private String strToDate;
	
	/**
	 * Gets the tenant id.
	 *
	 * @return the tenant id
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * Sets the tenant id.
	 *
	 * @param tenantId the new tenant id
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the application number
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber the new application number
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the application status.
	 *
	 * @return the application status
	 */
	public String getApplicationStatus() {
		return applicationStatus;
	}

	/**
	 * Sets the application status.
	 *
	 * @param applicationStatus the new application status
	 */
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	/**
	 * Gets the from date.
	 *
	 * @return the from date
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Sets the from date.
	 *
	 * @param fromDate the new from date
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets the to date.
	 *
	 * @return the to date
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Sets the to date.
	 *
	 * @param toDate the new to date
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile number
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the role code.
	 *
	 * @return the role code
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * Sets the role code.
	 *
	 * @param roleCode the new role code
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}


	/**
	 * Gets the sector.
	 *
	 * @return the sector
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * Sets the sector.
	 *
	 * @param sector the new sector
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the booking type.
	 *
	 * @return the booking type
	 */
	public String getBookingType() {
		return bookingType;
	}

	/**
	 * Sets the booking type.
	 *
	 * @param bookingType the new booking type
	 */
	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	/**
	 * Gets the request info.
	 *
	 * @return the request info
	 */
	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	/**
	 * Sets the request info.
	 *
	 * @param requestInfo the new request info
	 */
	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 *
	 * @param action the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Gets the business service.
	 *
	 * @return the business service
	 */
	public String getBusinessService() {
		return businessService;
	}

	/**
	 * Sets the business service.
	 *
	 * @param businessService the new business service
	 */
	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}

	/**
	 * Gets the type of room.
	 *
	 * @return the type of room
	 */
	public String getTypeOfRoom() {
		return typeOfRoom;
	}

	/**
	 * Sets the type of room.
	 *
	 * @param typeOfRoom the new type of room
	 */
	public void setTypeOfRoom(String typeOfRoom) {
		this.typeOfRoom = typeOfRoom;
	}

	/**
	 * Gets the str from date.
	 *
	 * @return the str from date
	 */
	public String getStrFromDate() {
		return strFromDate;
	}

	/**
	 * Sets the str from date.
	 *
	 * @param strFromDate the new str from date
	 */
	public void setStrFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
	}

	/**
	 * Gets the str to date.
	 *
	 * @return the str to date
	 */
	public String getStrToDate() {
		return strToDate;
	}

	/**
	 * Sets the str to date.
	 *
	 * @param strToDate the new str to date
	 */
	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
	}

	
}
