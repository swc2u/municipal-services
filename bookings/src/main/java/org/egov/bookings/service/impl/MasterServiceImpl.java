package org.egov.bookings.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.ApproverBean;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.CommonMasterFields;
import org.egov.bookings.contract.MasterRequest;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.MdmsSearchRequest;
import org.egov.bookings.contract.MdmsSearchResponse;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.model.user.OwnerInfo;
import org.egov.bookings.model.user.UserDetailResponse;
import org.egov.bookings.model.user.UserSearchRequest;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.repository.ParkCommunityHallV1MasterRepository;
import org.egov.bookings.repository.ParkCommunityInventoryRepsitory;
import org.egov.bookings.service.MasterService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.utils.MasterCreatedDateComparator;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

/**
 * The Class MasterServiceImpl.
 */
@Service
@Transactional
public class MasterServiceImpl implements MasterService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger( MasterServiceImpl.class.getName() );
	
	/** The park community inventory repsitory. */
	@Autowired
	private ParkCommunityInventoryRepsitory parkCommunityInventoryRepsitory;
	
	/** The osbm approver repository. */
	@Autowired
	private OsbmApproverRepository osbmApproverRepository; 

	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;
	
	/** The osujm fee repository. */
	@Autowired
	private OsujmFeeRepository osujmFeeRepository;
	
	/** The bookings producer. */
	@Autowired
	private BookingsProducer bookingsProducer;
	
	/** The config. */
	@Autowired
	private BookingsConfiguration config;
	
	/** The bookings fields validator. */
	@Autowired
	private BookingsFieldsValidator bookingsFieldsValidator;
	
	/** The commercial ground fee repository. */
	@Autowired
	private CommercialGroundRepository commercialGroundFeeRepository;
	
	/** The park community hall V 1 master repository. */
	@Autowired
	private ParkCommunityHallV1MasterRepository parkCommunityHallV1MasterRepository;
	
	/** The user service. */
	@Autowired
	private UserService userService;
	
	/** The bookings utils. */
	@Autowired
	private BookingsUtils bookingsUtils;
	
	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * Gets the park community inventory details.
	 *
	 * @param venue the venue
	 * @param sector the sector
	 * @return the park community inventory details
	 */
	@Override
	public List<InventoryModel> getParkCommunityInventoryDetails(String venue, String sector) {
		List< InventoryModel > inventoryModelList = new ArrayList<>();
		try
		{
			inventoryModelList = parkCommunityInventoryRepsitory.getParkCommunityInventoryDetails(venue, sector);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getParkCommunityInventoryDetails " + e);
			e.printStackTrace();
		}
		return inventoryModelList;
	}
	
	/**
	 * Creates the approver.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createApprover(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid osbmApproverRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		try {
			OsbmApproverModel OsbmApproverModel = osbmApproverRepository.findBySectorAndUuidAndRoleCodeAndUserId(masterRequest.getApproverList().get(0).getSector(), masterRequest.getApproverList().get(0).getUuid(), masterRequest.getApproverList().get(0).getRoleCode(), masterRequest.getApproverList().get(0).getUserId());
			OsbmApproverModel OsbmApproverModel2 = osbmApproverRepository.findBySectorAndRoleCode(masterRequest.getApproverList().get(0).getSector(), masterRequest.getApproverList().get(0).getRoleCode());
			if (BookingsFieldsValidator.isNullOrEmpty(OsbmApproverModel) && BookingsFieldsValidator.isNullOrEmpty(OsbmApproverModel2)) 
			{
				masterRequest.getApproverList().get(0).setId(UUID.randomUUID().toString());
				bookingsFieldsValidator.validateApproverBody(masterRequest);
				DateFormat formatter = getSimpleDateFormat();
				masterRequest.getApproverList().get(0).setCreatedDate(formatter.format(new Date()));
				masterRequest.getApproverList().get(0).setLastModifiedDate(formatter.format(new Date()));
				bookingsProducer.push(config.getSaveApproverTopic(), masterRequest);
			}
			else {
				throw new IllegalArgumentException("Duplicate Data");
			}
		}catch (Exception e) {
			throw new CustomException("APPROVER_SAVE_ERROR", "ERROR WHILE SAVING APPROVER DETAILS");
		}
		return masterRequest.getApproverList();
	}

	
	/**
	 * Update approver.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateApprover(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid Approver id");
		}
		try {
			bookingsFieldsValidator.validateApproverBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getApproverList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getUpdateApproverTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("APPROVER_UPDATE_ERROR", "ERROR WHILE UPDATE APPROVER DETAILS");
		}
		return masterRequest.getApproverList();
	}
	
	
	/**
	 * Creates the OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createOSBMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		try {
			masterRequest.getOsbmFeeList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateOSBMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsbmFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getOsbmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveOsbmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSBM_FEE_SAVE_ERROR", "ERROR WHILE SAVING OSBM FEE DETAILS");
		}
		return masterRequest.getOsbmFeeList();
	}

	
	/**
	 * Update OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateOSBMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee id");
		}
		OsbmFeeModel osbmFeeModel = new OsbmFeeModel();
		MasterRequest masterRequestOsbmFeeCreate = new MasterRequest();
		List<CommonMasterFields> osbmFeeList = new ArrayList<>();
		try {
			bookingsFieldsValidator.validateOSBMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsbmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			osbmFeeModel = osbmFeeRepository.findById(masterRequest.getOsbmFeeList().get(0).getId()); 
			String toDate = prepareToDate(masterRequest.getOsbmFeeList().get(0).getFromDate());
			Object object = prepareFeeObject(masterRequest.getOsbmFeeList().get(0));
			String jsonString = objectMapper.writeValueAsString(object);
			CommonMasterFields commonMasterFields = objectMapper.readValue(jsonString, CommonMasterFields.class);
			osbmFeeList.add(commonMasterFields);
			masterRequestOsbmFeeCreate.setOsbmFeeList(osbmFeeList);
			bookingsProducer.push(config.getSaveOsbmFeeTopic(), masterRequestOsbmFeeCreate);
			masterRequest.getOsbmFeeList().get(0).setAmount(osbmFeeModel.getAmount());
			masterRequest.getOsbmFeeList().get(0).setToDate(toDate);
			bookingsProducer.push(config.getUpdateOsbmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSBM_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE OSBM FEE DETAILS");
		}
		return masterRequest.getOsbmFeeList();
	}
	
	/**
	 * Creates the OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createOSUJMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		try {
			masterRequest.getOsujmFeeList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateOSUJMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsujmFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getOsujmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveOsujmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSUJM_FEE_SAVE_ERROR", "ERROR WHILE SAVING OSUJM FEE DETAILS");
		}
		return masterRequest.getOsujmFeeList();
	}
	
	/**
	 * Update OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateOSUJMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee id");
		}
		OsujmFeeModel osujmFeeModel = new OsujmFeeModel();
		MasterRequest masterRequestOsbmFeeCreate = new MasterRequest();
		List<CommonMasterFields> osujmFeeList = new ArrayList<>();
		try {
			bookingsFieldsValidator.validateOSUJMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsujmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			osujmFeeModel = osujmFeeRepository.findById(masterRequest.getOsujmFeeList().get(0).getId()); 
			String toDate = prepareToDate(masterRequest.getOsujmFeeList().get(0).getFromDate());
			Object object = prepareFeeObject(masterRequest.getOsujmFeeList().get(0));
			String jsonString = objectMapper.writeValueAsString(object);
			CommonMasterFields commonMasterFields = objectMapper.readValue(jsonString, CommonMasterFields.class);
			osujmFeeList.add(commonMasterFields);
			masterRequestOsbmFeeCreate.setOsujmFeeList(osujmFeeList);
			bookingsProducer.push(config.getSaveOsujmFeeTopic(), masterRequestOsbmFeeCreate);
			masterRequest.getOsujmFeeList().get(0).setRatePerSqrFeetPerDay(osujmFeeModel.getRatePerSqrFeetPerDay());
			masterRequest.getOsujmFeeList().get(0).setToDate(toDate);
			bookingsProducer.push(config.getUpdateOsujmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSUJM_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE OSUJM FEE DETAILS");
		}
		return masterRequest.getOsujmFeeList();
	}
	
	/**
	 * Creates the GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createGFCPFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee List");
		}
		try {
			masterRequest.getGfcpFeeList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateGFCPFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getGfcpFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getGfcpFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveGfcpFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("GFCP_FEE_SAVE_ERROR", "ERROR WHILE SAVING GFCP FEE DETAILS");
		}
		return masterRequest.getGfcpFeeList();
	}

	/**
	 * Update GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateGFCPFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee id");
		}
		CommercialGroundFeeModel commercialGroundFeeModel = new CommercialGroundFeeModel();
		MasterRequest masterRequestOsbmFeeCreate = new MasterRequest();
		List<CommonMasterFields> gfcpFeeList = new ArrayList<>();
		try {
			bookingsFieldsValidator.validateGFCPFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getGfcpFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			commercialGroundFeeModel = commercialGroundFeeRepository.findById(masterRequest.getGfcpFeeList().get(0).getId()); 
			String toDate = prepareToDate(masterRequest.getGfcpFeeList().get(0).getFromDate());
			Object object = prepareFeeObject(masterRequest.getGfcpFeeList().get(0));
			String jsonString = objectMapper.writeValueAsString(object);
			CommonMasterFields commonMasterFields = objectMapper.readValue(jsonString, CommonMasterFields.class);
			gfcpFeeList.add(commonMasterFields);
			masterRequestOsbmFeeCreate.setGfcpFeeList(gfcpFeeList);
			bookingsProducer.push(config.getSaveGfcpFeeTopic(), masterRequestOsbmFeeCreate);
			masterRequest.getGfcpFeeList().get(0).setRatePerDay(commercialGroundFeeModel.getRatePerDay());
			masterRequest.getGfcpFeeList().get(0).setToDate(toDate);
			bookingsProducer.push(config.getUpdateGfcpFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("GFCP_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE GFCP FEE DETAILS");
		}
		return masterRequest.getGfcpFeeList();
	}
	
	/**
	 * Creates the PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createPACCFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee List");
		}
		try {
			masterRequest.getPaccFeeList().get(0).setId(UUID.randomUUID().toString());
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getPaccFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getPaccFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSavePaccFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("PACC_FEE_SAVE_ERROR", "ERROR WHILE SAVING PACC FEE DETAILS");
		}
		return masterRequest.getPaccFeeList();
	}

	/**
	 * Update PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updatePACCFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee id");
		}
		ParkCommunityHallV1MasterModel parkCommunityHallV1MasterModel = new ParkCommunityHallV1MasterModel();
		MasterRequest masterRequestOsbmFeeCreate = new MasterRequest();
		List<CommonMasterFields> paccFeeList = new ArrayList<>();
		try {
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getPaccFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			parkCommunityHallV1MasterModel = parkCommunityHallV1MasterRepository.findById(masterRequest.getPaccFeeList().get(0).getId()); 
			String toDate = prepareToDate(masterRequest.getPaccFeeList().get(0).getFromDate());
			Object object = prepareFeeObject(masterRequest.getPaccFeeList().get(0));
			String jsonString = objectMapper.writeValueAsString(object);
			CommonMasterFields commonMasterFields = objectMapper.readValue(jsonString, CommonMasterFields.class);
			paccFeeList.add(commonMasterFields);
			masterRequestOsbmFeeCreate.setPaccFeeList(paccFeeList);
			bookingsProducer.push(config.getSavePaccFeeTopic(), masterRequestOsbmFeeCreate);
			masterRequest.getPaccFeeList().get(0).setPaccAmount(parkCommunityHallV1MasterModel.getAmount());
			masterRequest.getPaccFeeList().get(0).setToDate(toDate);
			bookingsProducer.push(config.getUpdatePaccFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("PACC_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE PACC FEE DETAILS");
		}
		return masterRequest.getPaccFeeList();
	}
	
	/**
	 * Gets the simple date format.
	 *
	 * @return the simple date format
	 */
	private DateFormat getSimpleDateFormat() {
		DateFormat formatter = new SimpleDateFormat(BookingsConstants.DATE_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone(BookingsConstants.TIME_ZONE));
		return formatter;
	}
	
	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.BookingsService#fetchAllApprover()
	 */
	@Override
	public List<BookingApprover> fetchAllApprover(UserSearchRequest userSearchRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(userSearchRequest)) {
			throw new IllegalArgumentException("Invalid userSearchRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(userSearchRequest.getRequestInfo())) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		List<String> roleCodes = new ArrayList<>();
		List<OwnerInfo> userList = new ArrayList<>();
		List<BookingApprover> bookingApproverList = new ArrayList<>();
		roleCodes.add(BookingsConstants.EMPLOYEE);
		try {
			StringBuilder url = prepareUrlForUserList();
			UserDetailResponse userDetailResponse = userService.getUserSearchDetails(roleCodes, url, userSearchRequest.getRequestInfo());
			if (!BookingsFieldsValidator.isNullOrEmpty(userDetailResponse) && !BookingsFieldsValidator.isNullOrEmpty(userDetailResponse.getUser())) {
				userList = userDetailResponse.getUser();
				for (OwnerInfo user : userList) {
					BookingApprover bookingApprover = new BookingApprover();
					bookingApprover.setUuid(user.getUuid());
					bookingApprover.setUserName(user.getUserName());
					bookingApprover.setMobileNumber(user.getMobileNumber());
					bookingApprover.setName(user.getName());
					bookingApprover.setId(user.getId());
					bookingApproverList.add(bookingApprover);
				}
			}
		}
		catch (Exception e) {
			throw new CustomException("APPROVER_ERROR", e.getLocalizedMessage());
		}
		return bookingApproverList;
	}
	
	/**
	 * Fetch all approver details.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<ApproverBean> fetchAllApproverDetails(MdmsSearchRequest mdmsSearchRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(mdmsSearchRequest)) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(mdmsSearchRequest.getRequestInfo())) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		Map<String, String> rolesMap = new HashMap<>();
		List<OsbmApproverModel> approverList = new ArrayList<>();
		List<ApproverBean> approverBeanList = new ArrayList<>();
		UserDetailResponse userDetailResponse = new UserDetailResponse();
		List<OwnerInfo> userList = new ArrayList<>();
		Map<Long,String> userMap = new HashMap<>();
		try {
			approverList = osbmApproverRepository.findAll();
			Collections.sort(approverList, new MasterCreatedDateComparator());
			Collections.reverse(approverList);
			if (!BookingsFieldsValidator.isNullOrEmpty(approverList)) {
				StringBuilder url = prepareUrlForUserList();
				userDetailResponse = userService.getUserSearchDetails(url, mdmsSearchRequest.getRequestInfo(), new UserSearchRequest());
				if (!BookingsFieldsValidator.isNullOrEmpty(userDetailResponse) && !BookingsFieldsValidator.isNullOrEmpty(userDetailResponse.getUser())) {
					userList = userDetailResponse.getUser();
					for (OwnerInfo user : userList) {
						userMap.put(user.getId(), user.getUserName());
					}
				}
				MdmsSearchResponse mdmsSearchResponse = mdmsBookingRoles(mdmsSearchRequest.getRequestInfo(), BookingsConstants.BOOKING_MDMS_MODULE_NAME, BookingsConstants.BOOKING_ROLES);
				if (!BookingsFieldsValidator.isNullOrEmpty(mdmsSearchResponse) && !BookingsFieldsValidator.isNullOrEmpty(mdmsSearchResponse.getBookingRoles())) {
					for (MdmsJsonFields mdmsJsonFields : mdmsSearchResponse.getBookingRoles()) {
						rolesMap.put(mdmsJsonFields.getCode(), mdmsJsonFields.getName());
					}
				}
				if(!BookingsFieldsValidator.isNullOrEmpty(rolesMap)) {
					for (OsbmApproverModel osbmApproverModel : approverList) {
						ApproverBean approverBean = new ApproverBean();
						approverBean.setCreatedDate(osbmApproverModel.getCreatedDate());
						approverBean.setId(osbmApproverModel.getId());
						approverBean.setLastModifiedDate(osbmApproverModel.getLastModifiedDate());
						if(!BookingsFieldsValidator.isNullOrEmpty(osbmApproverModel.getUserId())) {
							approverBean.setName(userMap.get(osbmApproverModel.getUserId()));
						}
						approverBean.setUserId(osbmApproverModel.getUserId());
						approverBean.setRoleCode(rolesMap.get(osbmApproverModel.getRoleCode()));
						approverBean.setSector(osbmApproverModel.getSector());
						approverBean.setUuid(osbmApproverModel.getUuid());
						approverBeanList.add(approverBean);
					}
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllApproverDetails " + e);
			e.printStackTrace();
		}
		return approverBeanList;
	}

	/**
	 * Fetch all OSBM fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<OsbmFeeModel> fetchAllOSBMFee() {
		List<OsbmFeeModel> osbmFeeList = new ArrayList<>();
		try {
			osbmFeeList = osbmFeeRepository.findAll();
			Collections.sort(osbmFeeList, new MasterCreatedDateComparator());
			Collections.reverse(osbmFeeList);
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllOSBMFee " + e);
			e.printStackTrace();
		}
		return osbmFeeList;
	}

	/**
	 * Fetch all OSUJM fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<OsujmFeeModel> fetchAllOSUJMFee() {
		List<OsujmFeeModel> osujmFeeList = new ArrayList<>();
		try {
			osujmFeeList = osujmFeeRepository.findAll(); 
			Collections.sort(osujmFeeList, new MasterCreatedDateComparator());
			Collections.reverse(osujmFeeList);
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllOSUJMFee " + e);
			e.printStackTrace();
		}
		return osujmFeeList;
	}

	/**
	 * Fetch all GFCP fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<CommercialGroundFeeModel> fetchAllGFCPFee() {
		List<CommercialGroundFeeModel> gfcpFeeList = new ArrayList<>();
		try {
			gfcpFeeList = commercialGroundFeeRepository.findAll(); 
			Collections.sort(gfcpFeeList, new MasterCreatedDateComparator());
			Collections.reverse(gfcpFeeList);
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllGFCPFee " + e);
			e.printStackTrace();
		}
		return gfcpFeeList;
	}
	
	/**
	 * Fetch all PACC fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<ParkCommunityHallV1MasterModel> fetchAllPACCFee() {
		List<ParkCommunityHallV1MasterModel> paccFeeList = new ArrayList<>();
		try {
			paccFeeList = parkCommunityHallV1MasterRepository.findAll(); 
			Collections.sort(paccFeeList, new MasterCreatedDateComparator());
			Collections.reverse(paccFeeList);
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllPACCFee " + e);
			e.printStackTrace();
		}
		return paccFeeList;
	}
	
	/**
	 * Gets the users.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the users
	 */
	@Override
	public List<UserDetails> getUsers(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
		{
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRoleCode())) 
		{
			throw new IllegalArgumentException("Invalid approver");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo())) 
		{
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		List<String> roleCodes = new ArrayList<>();
		UserDetailResponse userDetailResponse = new UserDetailResponse();
		List<UserDetails> userDetailsList = new ArrayList<>();
		List<OwnerInfo> userList = new ArrayList<>();
		try {
			roleCodes.add(searchCriteriaFieldsDTO.getRoleCode());
			StringBuilder url = prepareUrlForUserList();
			userDetailResponse = userService.getUserSearchDetails(roleCodes, url, searchCriteriaFieldsDTO.getRequestInfo());
			if (!BookingsFieldsValidator.isNullOrEmpty(userDetailResponse) && !BookingsFieldsValidator.isNullOrEmpty(userDetailResponse.getUser())) {
				userList = userDetailResponse.getUser();
				for (OwnerInfo user : userList) {
					UserDetails userDetails = new UserDetails();
					userDetails.setUuid(user.getUuid());
					userDetails.setUserName(user.getUserName());
					userDetails.setId(user.getId());
					userDetailsList.add(userDetails);
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the getUsers " + e);
			e.printStackTrace();
		}
		return userDetailsList;
	}

	/**
	 * Prepare url for user list.
	 *
	 * @return the string builder
	 */
	public StringBuilder prepareUrlForUserList() {
		StringBuilder url = new StringBuilder(config.getUserHost());
		url.append(config.getUserSearchEndpoint());
		return url;
	}
	
	/**
	 * Mdms booking roles.
	 *
	 * @param requestInfo the request info
	 * @param mdmsModuleName the mdms module name
	 * @param mdmsFileName the mdms file name
	 * @return the mdms search response
	 */
	private MdmsSearchResponse mdmsBookingRoles(RequestInfo requestInfo, String mdmsModuleName, String mdmsFileName) {
		MdmsSearchResponse mdmsSearchResponse = new MdmsSearchResponse();
		try {
			Object mdmsData = bookingsUtils.getMdmsSearchRequest(requestInfo, mdmsModuleName, mdmsFileName);
			String jsonString = objectMapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get(mdmsModuleName);
			jsonString = objectMapper.writeValueAsString(mdmsRes);
			mdmsSearchResponse = objectMapper.readValue(jsonString, MdmsSearchResponse.class);
		} catch (Exception e) {
			LOGGER.error("Exception occur in the mdmsBookingRoles " + e);
		}
		return mdmsSearchResponse;
	}

	/**
	 * Prepare to date.
	 *
	 * @param strFromDate the str from date
	 * @return the string
	 */
	private String prepareToDate(String strFromDate) {
		String toDate = "";
		try {
			if(!BookingsFieldsValidator.isNullOrEmpty(strFromDate)) {
				DateFormat formatter = getSimpleDateFormat();
				Date fromDate = formatter.parse(strFromDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(fromDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				Date d = cal.getTime();
			    long time = d.getTime();
			    time -= 1 * 24 * 3600 * 1000;
			    d.setTime(time);
			    cal.setTime(d);
			    cal.set(Calendar.HOUR_OF_DAY, BookingsConstants.HOURS);
				cal.set(Calendar.MINUTE, BookingsConstants.MINUTES);
				cal.set(Calendar.SECOND, BookingsConstants.SECONDS);
				toDate = formatter.format(cal.getTime());
			}
		}
		catch (Exception e) {
			throw new CustomException("TO_DATE_FORMATTER", "ERROR WHILE FORMATTING TODATE OF MODULE FEE");
		}
		return toDate;
	}
	
	/**
	 * Prepare fee object.
	 *
	 * @param commonMasterFields the common master fields
	 * @return the object
	 */
	private Object prepareFeeObject(CommonMasterFields commonMasterFields) {
		CommonMasterFields commonMasterFields2 = new CommonMasterFields();
		DateFormat formatter = getSimpleDateFormat();
		commonMasterFields2.setId(UUID.randomUUID().toString());
		commonMasterFields2.setAmount(commonMasterFields.getAmount());
		commonMasterFields2.setAreaFrom(commonMasterFields.getAreaFrom());
		commonMasterFields2.setAreaTo(commonMasterFields.getAreaTo());
		commonMasterFields2.setBookingAllowedFor(commonMasterFields.getBookingAllowedFor());
		commonMasterFields2.setBookingVenue(commonMasterFields.getBookingVenue());
		commonMasterFields2.setCategory(commonMasterFields.getCategory());
		commonMasterFields2.setCgstRate(commonMasterFields.getCgstRate());
		commonMasterFields2.setCleaningCharges(commonMasterFields.getCleaningCharges());
		commonMasterFields2.setConstructionType(commonMasterFields.getConstructionType());
		commonMasterFields2.setCreatedDate(formatter.format(new Date()));
		commonMasterFields2.setDimensionSqrYards(commonMasterFields.getDimensionSqrYards());
		commonMasterFields2.setDurationInMonths(commonMasterFields.getDurationInMonths());
		commonMasterFields2.setFromDate(commonMasterFields.getFromDate());
		commonMasterFields2.setImagePath(commonMasterFields.getImagePath());
		commonMasterFields2.setIsActive(commonMasterFields.getIsActive());
		commonMasterFields2.setLastModifiedDate(commonMasterFields.getLastModifiedDate());
		commonMasterFields2.setLocality(commonMasterFields.getLocality());
		commonMasterFields2.setLocationChangeAmount(commonMasterFields.getLocationChangeAmount());
		commonMasterFields2.setLuxuryTax(commonMasterFields.getLuxuryTax());
		commonMasterFields2.setName(commonMasterFields.getName());
		commonMasterFields2.setNormalType(commonMasterFields.getNormalType());
		commonMasterFields2.setOldrent1(commonMasterFields.getOldrent1());
		commonMasterFields2.setPaccAmount(commonMasterFields.getPaccAmount());
		commonMasterFields2.setRadius(commonMasterFields.getRadius());
		commonMasterFields2.setRatePerDay(commonMasterFields.getRatePerDay());
		commonMasterFields2.setRatePerSqrFeetPerDay(commonMasterFields.getRatePerSqrFeetPerDay());
		commonMasterFields2.setRefundabelSecurity(commonMasterFields.getRefundabelSecurity());
		commonMasterFields2.setRent(commonMasterFields.getRent());
		commonMasterFields2.setRentNextSession(commonMasterFields.getRentNextSession());
		commonMasterFields2.setResidentialCommercial(commonMasterFields.getResidentialCommercial());
		commonMasterFields2.setReviserate1(commonMasterFields.getReviserate1());
		commonMasterFields2.setRoleCode(commonMasterFields.getRoleCode());
		commonMasterFields2.setSccid(commonMasterFields.getSccid());
		commonMasterFields2.setScid(commonMasterFields.getScid());
		commonMasterFields2.setSector(commonMasterFields.getSector());
		commonMasterFields2.setSlab(commonMasterFields.getSlab());
		commonMasterFields2.setStorage(commonMasterFields.getStorage());
		commonMasterFields2.setSurcharge(commonMasterFields.getSurcharge());
		commonMasterFields2.setToDate(commonMasterFields.getToDate());
		commonMasterFields2.setUserId(commonMasterFields.getUserId());
		commonMasterFields2.setUtgstRate(commonMasterFields.getUtgstRate());
		commonMasterFields2.setUuid(commonMasterFields.getUuid());
		commonMasterFields2.setVenueType(commonMasterFields.getVenueType());
		commonMasterFields2.setVillageCity(commonMasterFields.getVillageCity());
		commonMasterFields2.setX(commonMasterFields.getX());
		commonMasterFields2.setY(commonMasterFields.getY());
		return commonMasterFields2;
	}
	
}
