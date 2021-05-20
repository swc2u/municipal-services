package org.egov.bookings.service.notification;

import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_BKROOM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.EmailAttachment;
import org.egov.bookings.contract.EmailRequest;
import org.egov.bookings.contract.SMSRequest;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.NotificationUtil;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * The Class RoomBookingNotificationService.
 */
@Service
@Transactional
public class RoomBookingNotificationService {

	/** The config. */
    private BookingsConfiguration config;

    /** The service request repository. */
    private ServiceRequestRepository serviceRequestRepository;

    /** The util. */
    private NotificationUtil util;


	/**
	 * Instantiates a new NLUJM booking notification service.
	 *
	 * @param config the config
	 * @param serviceRequestRepository the service request repository
	 * @param util the util
	 */
	@Autowired
	public RoomBookingNotificationService(BookingsConfiguration config, ServiceRequestRepository serviceRequestRepository, NotificationUtil util) {
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
    	String businessService = request.getBookingsModel().getRoomsModel().get(0).getRoomBusinessService();
 		switch(businessService)
 		{
 			case BUSINESS_SERVICE_BKROOM:
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
		String businessService = bookingsModel.getRoomsModel().get(0).getRoomBusinessService();
		String message = null;
		String localizationMessages = "";
		switch (businessService) {
		case BUSINESS_SERVICE_BKROOM:
			localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
			message = util.getRoomCustomizedMsg(request.getRequestInfo(), bookingsModel, localizationMessages);
		}
		
		Map<String, String> mobileNumberToOwner = new HashMap<>();
		if (bookingsModel.getBkMobileNumber() != null)
			mobileNumberToOwner.put(bookingsModel.getBkMobileNumber(), bookingsModel.getBkApplicantName());
		
		smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
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
		String businessService = bookingsModel.getRoomsModel().get(0).getRoomBusinessService();
		String message = null;
		String localizationMessages = "";
		switch (businessService) {
		case BUSINESS_SERVICE_BKROOM:
			localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
			message = util.getRoomMailCustomizedMsg(request.getRequestInfo(), bookingsModel, localizationMessages);
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
	}

}
