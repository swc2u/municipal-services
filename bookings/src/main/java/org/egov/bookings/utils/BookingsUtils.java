package org.egov.bookings.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.RefundTransactionRequest;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.Transaction;
import org.egov.bookings.contract.TransactionResponse;
import org.egov.bookings.model.ActionHistory;
import org.egov.bookings.model.ActionInfo;
import org.egov.bookings.model.AuditDetails;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsUtils.
 */
@Component

/** The Constant log. */
@Slf4j
public class BookingsUtils {

	/** The mdms host. */
	@Value("${egov.mdms.host}")
	private String mdmsHost;

	/** The mdms endpoint. */
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;

	/** The localization host. */
	@Value("${egov.localization.host}")
	private String localizationHost;

	/** The localization search endpoint. */
	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;

	/** The factory. */
	@Autowired
	private ResponseInfoFactory factory;

	/** The service request repository. */
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;
	
	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Util method to return Auditdetails for create and update processes.
	 *
	 * @param by       the by
	 * @param isCreate the is create
	 * @return the audit details
	 */
	public AuditDetails getAuditDetails(String by, Boolean isCreate) {

		Long dt = new Date().getTime();
		if (isCreate)
			return AuditDetails.builder().createdBy(by).createdTime(dt).lastModifiedBy(by).lastModifiedTime(dt).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(dt).build();
	}

	/**
	 * Prepare request for localization.
	 *
	 * @param uri         the uri
	 * @param requestInfo the request info
	 * @param locale      the locale
	 * @param tenantId    the tenant id
	 * @param module      the module
	 * @return the request info wrapper
	 */
	public RequestInfoWrapper prepareRequestForLocalization(StringBuilder uri, RequestInfo requestInfo, String locale,
			String tenantId, String module) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(localizationHost).append(localizationSearchEndpoint).append("?tenantId=" + tenantId)
				.append("&module=" + module).append("&locale=" + locale);

		return requestInfoWrapper;
	}

	/**
	 * Returns mapper with all the appropriate properties reqd in our
	 * functionalities.
	 * 
	 * @return ObjectMapper
	 */
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		return mapper;
	}

	/**
	 * Returns the roles that need to receive notification at this status and
	 * action.
	 *
	 * @param status the status
	 * @param action the action
	 * @return Set
	 */
	public Set<String> getReceptorsOfNotification(String status, String action) {
		Set<String> setOfRoles = new HashSet<>();
		setOfRoles.addAll(WorkFlowConfigs.getMapOfStatusAndReceptors().get(status));
		if (!StringUtils.isEmpty(action)
				&& (action.equals(WorkFlowConfigs.ACTION_REASSIGN) || action.equals(WorkFlowConfigs.ACTION_REOPEN))) {
			setOfRoles.clear();
			setOfRoles.addAll(WorkFlowConfigs.getMapOfActionAndReceptors().get(action));
		}
		return setOfRoles;
	}

	/**
	 * Splits any camelCase to human readable string.
	 *
	 * @param s the s
	 * @return String
	 */
	public static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	/**
	 * Convert to milli sec.
	 *
	 * @param hours the hours
	 * @return the long
	 */
	public Long convertToMilliSec(Integer hours) {
		Long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(hours));
		log.info("SLA in ms: " + milliseconds);
		return milliseconds;
	}

	/**
	 * returns the current status of the service.
	 *
	 * @param history the history
	 * @return the current status
	 */
	public String getCurrentStatus(ActionHistory history) {
		List<ActionInfo> infos = history.getActions();
		// FIXME pickup latest status another way which is not hardocoded, put query to
		// searcher to pick latest status
		// or use status from service object
		for (int i = 0; i <= infos.size() - 1; i++) {
			String status = infos.get(i).getStatus();
			if (null != status) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gets the booking type.
	 *
	 * @return the booking type
	 */
	private ModuleDetail getBookingType() {

		List<MasterDetail> bkMasterDetails = new ArrayList<>();

		bkMasterDetails.add(MasterDetail.builder().name("BookingType").build());

		ModuleDetail bkModuleDtls = ModuleDetail.builder().masterDetails(bkMasterDetails).moduleName("Booking").build();
		return bkModuleDtls;
	}
	
	/**
	 * Gets the mdms module search type.
	 *
	 * @param moduleName the module name
	 * @param mdmsFileName the mdms file name
	 * @return the mdms module search type
	 */
	private ModuleDetail getMdmsModuleSearchType(String moduleName, String mdmsFileName) {

		List<MasterDetail> bkMasterDetails = new ArrayList<>();

		bkMasterDetails.add(MasterDetail.builder().name(mdmsFileName).build());

		ModuleDetail bkModuleDtls = ModuleDetail.builder().masterDetails(bkMasterDetails).moduleName(moduleName).build();
		return bkModuleDtls;
	}

	/**
	 * Gets the tax head master type.
	 *
	 * @return the tax head master type
	 */
	private ModuleDetail getTaxHeadMasterType() {

		List<MasterDetail> bkMasterDetails = new ArrayList<>();

		bkMasterDetails.add(MasterDetail.builder().name("TaxHeadMaster").build());

		ModuleDetail bkModuleDtls = ModuleDetail.builder().masterDetails(bkMasterDetails).moduleName("BillingService")
				.build();

		return bkModuleDtls;
	}

	/**
	 * Gets the MDMS request.
	 *
	 * @param requestInfo the request info
	 * @param tenantId    the tenant id
	 * @return the MDMS request
	 */
	private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId) {

		ModuleDetail getBookingTypes = getBookingType();

		List<ModuleDetail> moduleDetails = new LinkedList<>();
		moduleDetails.add(getBookingTypes);

		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build();
		log.info("Mdms Criteria : " + mdmsCriteria);
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria).requestInfo(requestInfo)
				.build();

		log.info("Mdms Criteria Req: " + mdmsCriteriaReq);
		return mdmsCriteriaReq;
	}
	
	/**
	 * Gets the MDMS request.
	 *
	 * @param requestInfo the request info
	 * @param tenantId the tenant id
	 * @param moduleName the module name
	 * @param mdmsFileName the mdms file name
	 * @return the MDMS request
	 */
	private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId, String moduleName, String mdmsFileName) {

		ModuleDetail getMdmsModuleTypes = getMdmsModuleSearchType(moduleName, mdmsFileName);

		List<ModuleDetail> moduleDetails = new LinkedList<>();
		moduleDetails.add(getMdmsModuleTypes);

		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build();
		log.info("Mdms Criteria : " + mdmsCriteria);
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria).requestInfo(requestInfo)
				.build();

		log.info("Mdms Criteria Req: " + mdmsCriteriaReq);
		return mdmsCriteriaReq;
	}

	/**
	 * Prepare md ms request for booking.
	 *
	 * @param requestInfo the request info
	 * @return the object
	 */
	public Object prepareMdMsRequestForBooking(RequestInfo requestInfo) {
		String tenantId = requestInfo.getUserInfo().getTenantId();
		MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo, tenantId);
		StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
		Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
		return result;

	}

	/**
	 * Gets the mdms search request.
	 *
	 * @param requestInfo the request info
	 * @param moduleName the module name
	 * @param mdmsFileName the mdms file name
	 * @return the mdms search request
	 */
	public Object getMdmsSearchRequest(RequestInfo requestInfo, String moduleName, String mdmsFileName) {
		String tenantId = requestInfo.getUserInfo().getTenantId();
		MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo, tenantId, moduleName, mdmsFileName);
		StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
		Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
		return result;

	}
	
	/**
	 * Gets the MDMS request for tax head master.
	 *
	 * @param requestInfo the request info
	 * @param tenantId    the tenant id
	 * @return the MDMS request for tax head master
	 */
	private MdmsCriteriaReq getMDMSRequestForTaxHeadMaster(RequestInfo requestInfo, String tenantId) {

		ModuleDetail taxHeadMaster = getTaxHeadMasterType();

		List<ModuleDetail> moduleDetails = new LinkedList<>();
		moduleDetails.add(taxHeadMaster);

		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build();
		log.info("Mdms Criteria : " + mdmsCriteria);
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria).requestInfo(requestInfo)
				.build();

		log.info("Mdms Criteria Req: " + mdmsCriteriaReq);
		return mdmsCriteriaReq;
	}

	/**
	 * Prepare md ms request for tax head master.
	 *
	 * @param requestInfo the request info
	 * @return the object
	 */
	public Object prepareMdMsRequestForTaxHeadMaster(BookingsRequest bookingsRequest) {
		String tenantId = bookingsRequest.getBookingsModel().getTenantId();
		MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequestForTaxHeadMaster(bookingsRequest.getRequestInfo(), tenantId);
		StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
		Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
		return result;

	}

	/**
	 * Gets the current sql date.
	 *
	 * @return the current sql date
	 */
	public java.sql.Date getCurrentSqlDate() {
		long millis = System.currentTimeMillis();
		return new java.sql.Date(millis);
	}

	/**
	 * Gets the demand search URL.
	 *
	 * @return the demand search URL
	 */
	public String getDemandSearchURL() {
		StringBuilder url = new StringBuilder(config.getBillingHost());
		url.append(config.getDemandSearchEndpoint());
		url.append("?");
		url.append("tenantId=");
		url.append("{1}");
		url.append("&");
		url.append("businessService=");
		url.append("{2}");
		url.append("&");
		url.append("consumerCode=");
		url.append("{3}");
		return url.toString();
	}

	/**
	 * Removes the round off.
	 *
	 * @param finalAmount the final amount
	 * @return the big decimal
	 */
	public static BigDecimal removeRoundOff(BigDecimal finalAmount) {
		BigDecimal decimalValue = finalAmount.remainder(BigDecimal.ONE);
		BigDecimal midVal = new BigDecimal(0.5);

		if (decimalValue.compareTo(midVal) == 0)
			return finalAmount;
		else
			return finalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

	}
	
	/**
	 * Round off to nearest.
	 *
	 * @param amount the amount
	 * @return the big decimal
	 */
	public static BigDecimal roundOffToNearest(BigDecimal amount) {
		Double taxAmount = Double.valueOf(amount+"");
		return BigDecimal.valueOf(Math.round(taxAmount));
	}

	public Transaction fetchPaymentTransaction(RefundTransactionRequest refundTransactionRequest) {
		String uri = getPaymentTransactionURL();
		uri = uri.replace("{1}", refundTransactionRequest.getRefundTransaction().getTenantId());
		uri = uri.replace("{2}", refundTransactionRequest.getRefundTransaction().getTxnId());
		
		Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),
				RequestInfoWrapper.builder().requestInfo(refundTransactionRequest.getRequestInfo()).build());

		TransactionResponse transaction;
		try {
			transaction = mapper.convertValue(result, TransactionResponse.class);
		} catch (IllegalArgumentException e) {
			throw new CustomException("PARSING ERROR", "Failed to parse response from Demand Search");
		}
				
		return transaction.getTransaction().get(0);
	}

	private String getPaymentTransactionURL() {
		StringBuilder url = new StringBuilder(config.getPgServiceHost());
		url.append(config.getPgServiceEndPoint());
		url.append("?");
		url.append("tenantId=");
		url.append("{1}");
		url.append("&");
		url.append("txnId=");
		url.append("{2}");
		return url.toString();
	}

}
