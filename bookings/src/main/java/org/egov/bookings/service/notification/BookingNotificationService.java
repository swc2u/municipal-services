package org.egov.bookings.service.notification;

import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_BWT;
import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_GFCP;
import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_OSBM;
import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_OSUJM;
import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_PACC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Action;
import org.egov.bookings.contract.ActionItem;
import org.egov.bookings.contract.EmailAttachment;
import org.egov.bookings.contract.EmailRequest;
import org.egov.bookings.contract.Event;
import org.egov.bookings.contract.EventRequest;
import org.egov.bookings.contract.Recepient;
import org.egov.bookings.contract.SMSRequest;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.Source;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.NotificationUtil;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */

/** The Constant log. */

/** The Constant log. */
@Slf4j
@Service
public class BookingNotificationService {

    /** The config. */
    private BookingsConfiguration config;

    /** The service request repository. */
    private ServiceRequestRepository serviceRequestRepository;

    /** The util. */
    private NotificationUtil util;


	/**
	 * Instantiates a new booking notification service.
	 *
	 * @param config the config
	 * @param serviceRequestRepository the service request repository
	 * @param util the util
	 */
	@Autowired
	public BookingNotificationService(BookingsConfiguration config, ServiceRequestRepository serviceRequestRepository, NotificationUtil util) {
		this.config = config;
		this.serviceRequestRepository = serviceRequestRepository;
		this.util = util;
	}

    /**
     * Process.
     *
     * @param request the request
     */
    public void process(BookingsRequest request){
        String businessService = request.getBookingsModel().getBusinessService();
		switch(businessService)
		{
			case BUSINESS_SERVICE_OSBM:
			case BUSINESS_SERVICE_BWT:
			case BUSINESS_SERVICE_GFCP:
			case BUSINESS_SERVICE_OSUJM:
			case BUSINESS_SERVICE_PACC:	
				List<SMSRequest> smsRequestsTL = new LinkedList<>();
				List<EmailRequest> emailRequestsTL = new LinkedList<>();

				if(null != config.getIsBKSMSEnabled()) {
					if(config.getIsBKSMSEnabled()) {
						enrichSMSRequest(request,smsRequestsTL);
						if(!CollectionUtils.isEmpty(smsRequestsTL))
							util.sendSMS(smsRequestsTL,true);
					}
				}
				if(null != config.getIsBKEMAILEnabled()) {
					if(config.getIsBKEMAILEnabled()) {
						enrichEMAILRequest(request,emailRequestsTL);
						if(!CollectionUtils.isEmpty(emailRequestsTL))
							util.sendEMAIL(emailRequestsTL,true);
					}
				}
		}
    }

	/**
	 * Enrich SMS request.
	 *
	 * @param request the request
	 * @param smsRequests the sms requests
	 */
	private void enrichSMSRequest(BookingsRequest request, List<SMSRequest> smsRequests) {
		String tenantId = request.getBookingsModel().getTenantId();
		BookingsModel bookingsModel = request.getBookingsModel();
		String businessService = bookingsModel.getBusinessService();
		String message = null;
		String localizationMessages = "";
		switch (businessService) {
		case BUSINESS_SERVICE_OSBM:
		case BUSINESS_SERVICE_BWT:
		case BUSINESS_SERVICE_GFCP:
		case BUSINESS_SERVICE_OSUJM:
		case BUSINESS_SERVICE_PACC:
			localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
			message = util.getCustomizedMsg(request.getRequestInfo(), bookingsModel, localizationMessages);
		}
		
		Map<String, String> mobileNumberToOwner = new HashMap<>();
		if (bookingsModel.getBkMobileNumber() != null)
			mobileNumberToOwner.put(bookingsModel.getBkMobileNumber(), bookingsModel.getBkApplicantName());
		
		smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
		mobileNumberToOwner.remove(bookingsModel.getBkMobileNumber());
		if(BookingsConstants.BUSINESS_SERVICE_BWT.equals(bookingsModel.getBusinessService()) 
				&& BookingsConstants.ACTION_STATUS_PENDINGUPDATE.equals((bookingsModel.getBkAction() + "_" + bookingsModel.getBkApplicationStatus()))) {
			message = util.getCustomizedMsgForDriver(bookingsModel, localizationMessages);
			mobileNumberToOwner.put(bookingsModel.getBkContactNo(), bookingsModel.getBkDriverName());
			smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
		}
		
		// When Applicant submit application of OSBM booking, then SMS notification send to approver of OSBM.
		if(!BookingsFieldsValidator.isNullOrEmpty(request.getUser())) {
			mobileNumberToOwner.remove(bookingsModel.getBkMobileNumber());
			mobileNumberToOwner.put(request.getUser().getMobileNumber(), request.getUser().getUserName());
			message = util.getCustomizedMsgForApprover(bookingsModel.getBkApplicationNumber(), bookingsModel.getBkBookingType(), localizationMessages, request.getUser().getUserName());
			smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
		}
		
	}
    
	/**
	 * Enrich EMAIL request.
	 *
	 * @param request the request
	 * @param emailRequests the email requests
	 */
	private void enrichEMAILRequest(BookingsRequest request, List<EmailRequest> emailRequests) {
		String tenantId = request.getBookingsModel().getTenantId();
		BookingsModel bookingsModel = request.getBookingsModel();
		Map<String, String> emailIdToOwner = new HashMap<>();
		List<EmailAttachment> attachments = new ArrayList<>();
		if (bookingsModel.getBkEmail() != null)
			emailIdToOwner.put(bookingsModel.getBkEmail(), bookingsModel.getBkApplicantName());
		String businessService = bookingsModel.getBusinessService();
		String message = null;
		String localizationMessages = "";
		switch (businessService) {
		case BUSINESS_SERVICE_OSBM:
		case BUSINESS_SERVICE_BWT:
		case BUSINESS_SERVICE_GFCP:
		case BUSINESS_SERVICE_OSUJM:
		case BUSINESS_SERVICE_PACC:
			localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
			message = util.getMailCustomizedMsg(request.getRequestInfo(), bookingsModel, localizationMessages);
			break;
		}
		Map<String, String> receiptURLMap = new HashMap<>();
		if (!BookingsFieldsValidator.isNullOrEmpty(request.getUrlData())) {
			receiptURLMap = request.getUrlData();
		}
		String paymentReceiptURL = "";
		String permissionLetterURL = "";
		if (!BookingsFieldsValidator.isNullOrEmpty(receiptURLMap)){
			paymentReceiptURL = receiptURLMap.get(BookingsConstants.PAYMENT_RECEIPT);
			permissionLetterURL = receiptURLMap.get(BookingsConstants.PERMISSION_LETTER);
		}
		message = message.replace("\\n", "\n");
		if (!BookingsFieldsValidator.isNullOrEmpty(paymentReceiptURL) || !BookingsFieldsValidator.isNullOrEmpty(permissionLetterURL)) {
			attachments = util.prepareEmailAttachment(paymentReceiptURL, permissionLetterURL);
			emailRequests.addAll(util.createEMAILRequest(message, emailIdToOwner, attachments));
		}
		else {
			emailRequests.addAll(util.createEMAILRequest(message, emailIdToOwner));
		}
		
		// When Applicant submit application of OSBM booking, then mail notification send to approver of OSBM.
		if(!BookingsFieldsValidator.isNullOrEmpty(request.getUser())) {
			emailIdToOwner.remove(bookingsModel.getBkEmail());
			emailIdToOwner.put(request.getUser().getEmailId(), request.getUser().getUserName());
			message = util.getMailCustomizedMsgForApprover(bookingsModel.getBkApplicationNumber(), bookingsModel.getBkBookingType(), localizationMessages, request.getUser().getUserName());
			emailRequests.addAll(util.createEMAILRequest(message, emailIdToOwner));
		}
	}
    
	/**
	 * Gets the events for BK.
	 *
	 * @param request the request
	 * @return the events for BK
	 */
	public EventRequest getEventsForBK(BookingsRequest request) {
		List<Event> events = new ArrayList<>();
		String tenantId = request.getBookingsModel().getTenantId();
		String localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
		BookingsModel bookingsModel = request.getBookingsModel();
		String message = util.getCustomizedCTLMessage(request.getRequestInfo(), bookingsModel, localizationMessages);
		message = message.replace("\\n", " ");
		Map<String, String> mobileNumberToOwner = new HashMap<>();
		if (bookingsModel.getBkMobileNumber() != null)
			mobileNumberToOwner.put(bookingsModel.getBkMobileNumber(), bookingsModel.getBkApplicantName());
		List<SMSRequest> smsRequests = util.createSMSRequest(message, mobileNumberToOwner);
		Set<String> mobileNumbers = smsRequests.stream().map(SMSRequest::getMobileNumber).collect(Collectors.toSet());
		Map<String, String> mapOfPhnoAndUUIDs = fetchUserUUIDs(mobileNumbers, request.getRequestInfo(),
				request.getBookingsModel().getTenantId());
		if (CollectionUtils.isEmpty(mapOfPhnoAndUUIDs.keySet())) {
			log.info("UUID search failed!");
		}
		Map<String, String> mobileNumberToMsg = smsRequests.stream()
				.collect(Collectors.toMap(SMSRequest::getMobileNumber, SMSRequest::getMessage));
		for (String mobile : mobileNumbers) {
			if (null == mapOfPhnoAndUUIDs.get(mobile) || null == mobileNumberToMsg.get(mobile)) {
				log.error("No UUID/SMS for mobile {} skipping event", mobile);
				continue;
			}
			List<String> toUsers = new ArrayList<>();
			toUsers.add(mapOfPhnoAndUUIDs.get(mobile));
			Recepient recepient = Recepient.builder().toUsers(toUsers).toRoles(null).build();
			List<String> payTriggerList = Arrays.asList(config.getPayTriggers().split("[,]"));
			Action action = null;
			if (payTriggerList.contains(bookingsModel.getBkApplicationStatus())) {
				List<ActionItem> items = new ArrayList<>();
				String actionLink = config.getCtlPayLink().replace("$mobile", mobile)
						.replace("$applicationNo", bookingsModel.getBkApplicationNumber())
						.replace("$tenantId", bookingsModel.getTenantId());
				actionLink = config.getUiAppHost() + actionLink;
				ActionItem item = ActionItem.builder().actionUrl(actionLink).code(config.getPayCode()).build();
				items.add(item);
				action = Action.builder().actionUrls(items).build();
			}

			events.add(Event.builder().tenantId(bookingsModel.getTenantId()).description(mobileNumberToMsg.get(mobile))
					.eventType(BookingsConstants.USREVENTS_EVENT_TYPE).name(BookingsConstants.USREVENTS_EVENT_NAME)
					.postedBy(BookingsConstants.USREVENTS_EVENT_POSTEDBY).source(Source.WEBAPP).recepient(recepient)
					.eventDetails(null).actions(action).build());

		}
		if (!CollectionUtils.isEmpty(events)) {
			return EventRequest.builder().requestInfo(request.getRequestInfo()).events(events).build();
		} else {
			return null;
		}

	}

    /**
     * Fetches UUIDs of CITIZENs based on the phone number.
     *
     * @param mobileNumbers the mobile numbers
     * @param requestInfo the request info
     * @param tenantId the tenant id
     * @return the map
     */
    private Map<String, String> fetchUserUUIDs(Set<String> mobileNumbers, RequestInfo requestInfo, String tenantId) {
    	Map<String, String> mapOfPhnoAndUUIDs = new HashMap<>();
    	StringBuilder uri = new StringBuilder();
    	uri.append(config.getUserHost()).append(config.getUserSearchEndpoint());
    	Map<String, Object> userSearchRequest = new HashMap<>();
    	userSearchRequest.put("RequestInfo", requestInfo);
		userSearchRequest.put("tenantId", tenantId);
		userSearchRequest.put("userType", "CITIZEN");
    	for(String mobileNo: mobileNumbers) {
    		userSearchRequest.put("userName", mobileNo);
    		try {
    			Object user = serviceRequestRepository.fetchResult(uri, userSearchRequest);
    			if(null != user) {
    				String uuid = JsonPath.read(user, "$.user[0].uuid");
    				mapOfPhnoAndUUIDs.put(mobileNo, uuid);
    			}else {
        			log.error("Service returned null while fetching user for username - "+mobileNo);
    			}
    		}catch(Exception e) {
    			log.error("Exception while fetching user for username - "+mobileNo);
    			log.error("Exception trace: ",e);
    			continue;
    		}
    	}
    	return mapOfPhnoAndUUIDs;
    }
    
}
