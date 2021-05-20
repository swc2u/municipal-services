package org.egov.bookings.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.user.CreateUserRequest;
import org.egov.bookings.model.user.OwnerInfo;
import org.egov.bookings.model.user.User;
import org.egov.bookings.model.user.UserDetailResponse;
import org.egov.bookings.model.user.UserSearchRequest;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	private ObjectMapper mapper;

	private org.egov.bookings.repository.impl.ServiceRequestRepository serviceRequestRepository;

	private BookingsConfiguration config;

	@Autowired
	public UserService(ObjectMapper mapper,
			org.egov.bookings.repository.impl.ServiceRequestRepository serviceRequestRepository,
			BookingsConfiguration config) {
		this.mapper = mapper;
		this.serviceRequestRepository = serviceRequestRepository;
		this.config = config;
	}

	/**
	 * Creates the user.
	 *
	 * @param bookingsRequest      the bookings request
	 * @param isBKRoleAddRequired the is BPA role add required
	 */
	public void createUser(BookingsRequest bookingsRequest, boolean isBKRoleAddRequired) {
		BookingsModel bookingsModel = bookingsRequest.getBookingsModel();
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		List<BookingsModel> bookingsModelList = new ArrayList<>();
		bookingsModelList.add(bookingsModel);
		Role role = getCitizenRole(bookingsModel.getTenantId());
		OwnerInfo owner = new OwnerInfo();
		/*
		 * Set<String> listOfMobileNumbers =
		 * getMobileNumbers(tradeLicense.getTradeLicenseDetail().getOwners()
		 * ,requestInfo,tradeLicense.getTenantId());
		 */

		String businessService = bookingsModel.getBusinessService();
		UserDetailResponse userDetailResponse = searchByMobileNumber(bookingsModel.getBkMobileNumber(),
				getStateLevelTenant(bookingsModel.getTenantId()));
		if (!userDetailResponse.getUser().isEmpty()) {
			User user = userDetailResponse.getUser().get(0);
			if (user.getMobileNumber() != null) {
				bookingsRequest.getRequestInfo().getUserInfo().setUuid(user.getUuid());
			}
			List<OwnerInfo> owner1 = userDetailResponse.getUser();
			owner = addNotNullFieldsFromOwner(user, owner1.get(0));
		} else {
			addUserDefaultFields(bookingsModel.getTenantId(), role, owner, businessService);
			// UserDetailResponse userDetailResponse = userExists(owner,requestInfo);
			StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserContextPath())
					.append(config.getUserCreateEndpoint());
			setUserDetails(bookingsModel, owner, role);
			UserDetailResponse userDetailResponse1 = userCall(new CreateUserRequest(requestInfo, owner), uri);
			if (userDetailResponse1.getUser().get(0).getUuid() == null) {
				throw new CustomException("INVALID USER RESPONSE", "The user created has uuid as null");
			}
			bookingsRequest.getRequestInfo().getUserInfo().setUuid(userDetailResponse1.getUser().get(0).getUuid());
			log.info("owner created --> " + userDetailResponse1.getUser().get(0).getUuid());
			setOwnerFields(owner, userDetailResponse1, requestInfo);
		}

	}

	private void setUserDetails(BookingsModel bookingsModel, OwnerInfo owner, Role role) {
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		String username = UUID.randomUUID().toString();
		owner.setUserName(username);
		owner.setName(bookingsModel.getBkApplicantName());
		owner.setActive(true);
		owner.setMobileNumber(bookingsModel.getBkMobileNumber());
		owner.setType("CITIZEN");
		owner.setPermanentAddress(bookingsModel.getBkAddress());
		owner.setCorrespondenceAddress(bookingsModel.getBkAddress());
		owner.setRoles(roles);
		owner.setOtpReference("12345");
		owner.setTenantId("ch");
	}

	private OwnerInfo addNotNullFieldsFromOwner(User user, OwnerInfo owner) {
		OwnerInfo newowner = new OwnerInfo();
		newowner.setUuid(getFromOwnerIfNotNull(user.getUuid(), owner.getUuid()));
		newowner.setId((owner.getId() == null) ? user.getId() : owner.getId());
		newowner.setUserName(getFromOwnerIfNotNull(user.getUserName(), owner.getUserName()));
		newowner.setPassword(getFromOwnerIfNotNull(user.getPassword(), owner.getPassword()));
		newowner.setSalutation(getFromOwnerIfNotNull(user.getSalutation(), owner.getSalutation()));
		newowner.setName(getFromOwnerIfNotNull(user.getName(), owner.getName()));
		newowner.setGender(getFromOwnerIfNotNull(user.getGender(), owner.getGender()));
		newowner.setMobileNumber(getFromOwnerIfNotNull(user.getMobileNumber(), owner.getMobileNumber()));
		newowner.setEmailId(getFromOwnerIfNotNull(user.getEmailId(), owner.getEmailId()));
		newowner.setAltContactNumber(getFromOwnerIfNotNull(user.getAltContactNumber(), owner.getAltContactNumber()));
		newowner.setPan(getFromOwnerIfNotNull(user.getPan(), owner.getPan()));
		newowner.setAadhaarNumber(getFromOwnerIfNotNull(user.getAadhaarNumber(), owner.getAadhaarNumber()));
		newowner.setPermanentAddress(getFromOwnerIfNotNull(user.getPermanentAddress(), owner.getPermanentAddress()));
		newowner.setPermanentCity(getFromOwnerIfNotNull(user.getPermanentCity(), owner.getPermanentCity()));
		newowner.setPermanentPincode(getFromOwnerIfNotNull(user.getPermanentPincode(), owner.getPermanentPincode()));
		newowner.setCorrespondenceAddress(
				getFromOwnerIfNotNull(user.getCorrespondenceAddress(), owner.getCorrespondenceAddress()));
		newowner.setCorrespondenceCity(
				getFromOwnerIfNotNull(user.getCorrespondenceCity(), owner.getCorrespondenceCity()));
		newowner.setCorrespondencePincode(
				getFromOwnerIfNotNull(user.getCorrespondencePincode(), owner.getCorrespondencePincode()));
		newowner.setActive((owner.getActive() == null) ? user.getActive() : owner.getActive());
		newowner.setDob((owner.getDob() != null) ? owner.getDob() : user.getDob());
		newowner.setPwdExpiryDate(
				(owner.getPwdExpiryDate() == null) ? user.getPwdExpiryDate() : owner.getPwdExpiryDate());
		newowner.setLocale(getFromOwnerIfNotNull(user.getLocale(), owner.getLocale()));
		newowner.setType(getFromOwnerIfNotNull(user.getType(), owner.getType()));
		newowner.setRoles(user.getRoles());
		newowner.setAccountLocked(
				(owner.getAccountLocked() == null) ? user.getAccountLocked() : owner.getAccountLocked());
		newowner.setFatherOrHusbandName(
				getFromOwnerIfNotNull(user.getFatherOrHusbandName(), owner.getFatherOrHusbandName()));
		newowner.setBloodGroup(getFromOwnerIfNotNull(user.getBloodGroup(), owner.getBloodGroup()));
		newowner.setIdentificationMark(
				getFromOwnerIfNotNull(user.getIdentificationMark(), owner.getIdentificationMark()));
		newowner.setPhoto(getFromOwnerIfNotNull(user.getPhoto(), owner.getPhoto()));
		newowner.setTenantId(getFromOwnerIfNotNull(user.getTenantId(), owner.getTenantId()));
		return newowner;
	}

	private String getFromOwnerIfNotNull(String fromuser, String fromowner) {
		if (fromowner != null) {
			return fromowner;
		}
		return fromuser;
	}

	/**
	 * Sets ownerfields from the userResponse
	 * 
	 * @param owner              The owner from tradeLicense
	 * @param userDetailResponse The response from user search
	 * @param requestInfo        The requestInfo of the request
	 */
	private void setOwnerFields(OwnerInfo owner, UserDetailResponse userDetailResponse, RequestInfo requestInfo) {
		owner.setUuid(userDetailResponse.getUser().get(0).getUuid());
		owner.setId(userDetailResponse.getUser().get(0).getId());
		owner.setUserName((userDetailResponse.getUser().get(0).getUserName()));
		owner.setCreatedBy(requestInfo.getUserInfo().getUuid());
		owner.setLastModifiedBy(requestInfo.getUserInfo().getUuid());
		owner.setCreatedDate(System.currentTimeMillis());
		owner.setLastModifiedDate(System.currentTimeMillis());
		owner.setActive(userDetailResponse.getUser().get(0).getActive());
	}

	/**
	 * Sets the role,type,active and tenantId for a Citizen
	 * 
	 * @param tenantId TenantId of the property
	 * @param role     The role of the user set in this case to CITIZEN
	 * @param owner    The user whose fields are to be set
	 */
	private void addUserDefaultFields(String tenantId, Role role, OwnerInfo owner, String businessService) {
		owner.setActive(true);
		owner.setTenantId(tenantId.split("\\.")[0]);
		owner.setRoles(Collections.singletonList(role));
		owner.setType("CITIZEN");
	}

	/**
	 * Creates citizen role
	 * 
	 * @return Role object for citizen
	 */
	private Role getCitizenRole(String tenantId) {
		Role role = new Role();
		role.setCode("CITIZEN");
		role.setName("Citizen");
		role.setTenantId(getStateLevelTenant(tenantId));
		return role;
	}

	private String getStateLevelTenant(String tenantId) {
		return tenantId.split("\\.")[0];
	}

	/**
	 * Returns UserDetailResponse by calling user service with given uri and object
	 * 
	 * @param userRequest Request object for user service
	 * @param uri         The address of the endpoint
	 * @return Response from user service as parsed as userDetailResponse
	 */
	private UserDetailResponse userCall(Object userRequest, StringBuilder uri) {
		String dobFormat = null;
		if (uri.toString().contains(config.getUserSearchEndpoint())
				|| uri.toString().contains(config.getUserUpdateEndpoint()))
			dobFormat = "yyyy-MM-dd";
		else if (uri.toString().contains(config.getUserCreateEndpoint()))
			dobFormat = "dd/MM/yyyy";
		try {
			LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(uri, userRequest);
			parseResponse(responseMap, dobFormat);
			UserDetailResponse userDetailResponse = mapper.convertValue(responseMap, UserDetailResponse.class);
			return userDetailResponse;
		} catch (IllegalArgumentException e) {
			throw new CustomException("IllegalArgumentException", "ObjectMapper not able to convertValue in userCall");
		}
	}

	/**
	 * Parses date formats to long for all users in responseMap
	 * 
	 * @param responeMap LinkedHashMap got from user api response
	 */
	private void parseResponse(LinkedHashMap responeMap, String dobFormat) {
		List<LinkedHashMap> users = (List<LinkedHashMap>) responeMap.get("user");
		String format1 = "dd-MM-yyyy HH:mm:ss";
		if (users != null) {
			users.forEach(map -> {
				map.put("createdDate", dateTolong((String) map.get("createdDate"), format1));
				if ((String) map.get("lastModifiedDate") != null)
					map.put("lastModifiedDate", dateTolong((String) map.get("lastModifiedDate"), format1));
				if ((String) map.get("dob") != null)
					map.put("dob", dateTolong((String) map.get("dob"), dobFormat));
				if ((String) map.get("pwdExpiryDate") != null)
					map.put("pwdExpiryDate", dateTolong((String) map.get("pwdExpiryDate"), format1));
			});
		}
	}

	/**
	 * Converts date to long
	 * 
	 * @param date   date to be parsed
	 * @param format Format of the date
	 * @return Long value of date
	 */
	private Long dateTolong(String date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = f.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d.getTime();
	}

	private UserDetailResponse searchByMobileNumber(String mobileNumber, String tenantId) {
		org.egov.bookings.model.user.UserSearchRequest userSearchRequest = new org.egov.bookings.model.user.UserSearchRequest();
		userSearchRequest.setUserType("CITIZEN");
		userSearchRequest.setMobileNumber(mobileNumber);
		userSearchRequest.setTenantId(tenantId);
		StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
		return userCall(userSearchRequest, uri);

	}
	
	/**
	 * Gets the user search details.
	 *
	 * @param roleCodes the role codes
	 * @param url the url
	 * @param requestInfo the request info
	 * @return the user search details
	 */
	public UserDetailResponse getUserSearchDetails(List<String> roleCodes, StringBuilder url, RequestInfo requestInfo) {
		org.egov.bookings.model.user.UserSearchRequest userSearchRequest = new org.egov.bookings.model.user.UserSearchRequest();
		userSearchRequest.setRoleCodes(roleCodes);
		userSearchRequest.setRequestInfo(requestInfo);
		userSearchRequest.setTenantId(BookingsConstants.EMPLOYEE_TENANT_ID);
		return userCall(userSearchRequest, url);
	}
	
	/**
	 * Gets the user search details.
	 *
	 * @param url the url
	 * @param requestInfo the request info
	 * @param userSearchRequest the user search request
	 * @return the user search details
	 */
	public UserDetailResponse getUserSearchDetails(StringBuilder url, RequestInfo requestInfo, UserSearchRequest userSearchRequest) {
		List<String> roleCodes = new ArrayList<>();
		roleCodes.add(BookingsConstants.EMPLOYEE);
		userSearchRequest.setRoleCodes(roleCodes);
		userSearchRequest.setTenantId(BookingsConstants.EMPLOYEE_TENANT_ID);
		return userCall(userSearchRequest, url);
	}

}
