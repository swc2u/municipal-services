package org.egov.bookings.service.notification;

import static org.egov.bookings.utils.BookingsConstants.BUSINESS_SERVICE_NLUJM;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.EmailRequest;
import org.egov.bookings.contract.SMSRequest;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.utils.NotificationUtil;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * The Class NLUJMBookingNotificationService.
 */
@Service
public class NLUJMBookingNotificationService {
	 
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
	public NLUJMBookingNotificationService(BookingsConfiguration config, ServiceRequestRepository serviceRequestRepository, NotificationUtil util) {
		this.config = config;
		this.serviceRequestRepository = serviceRequestRepository;
		this.util = util;
	}


    /**
     * Process.
     *
     * @param request the request
     */
    public void process(NewLocationRequest request){
        String businessService = request.getNewLocationModel().getBusinessService();
		switch(businessService)
		{
			case BUSINESS_SERVICE_NLUJM:
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
	private void enrichSMSRequest(NewLocationRequest request, List<SMSRequest> smsRequests) {
		String tenantId = request.getNewLocationModel().getTenantId();
		OsujmNewLocationModel osujmNewLocationModel = request.getNewLocationModel();
		String businessService = osujmNewLocationModel.getBusinessService();
		String message = null;
		String localizationMessages;
		switch (businessService) {
		case BUSINESS_SERVICE_NLUJM:
			localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
			message = util.getCustomizedMsg(request.getRequestInfo(), osujmNewLocationModel, localizationMessages);
		}
		Map<String, String> mobileNumberToOwner = new HashMap<>();
		if (osujmNewLocationModel.getContact() != null)
			mobileNumberToOwner.put(osujmNewLocationModel.getContact(), osujmNewLocationModel.getApplicantName());

		smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
	}
    
	/**
	 * Enrich EMAIL request.
	 *
	 * @param request the request
	 * @param emailRequests the email requests
	 */
	private void enrichEMAILRequest(NewLocationRequest request, List<EmailRequest> emailRequests) {
		String tenantId = request.getNewLocationModel().getTenantId();
		OsujmNewLocationModel osujmNewLocationModel = request.getNewLocationModel();
		Map<String, String> emailIdToOwner = new HashMap<>();
		if (osujmNewLocationModel.getMailAddress() != null)
			emailIdToOwner.put(osujmNewLocationModel.getMailAddress(), osujmNewLocationModel.getApplicantName());
		String businessService = osujmNewLocationModel.getBusinessService();
		String message = null;
		String localizationMessages;
		switch (businessService) {
		case BUSINESS_SERVICE_NLUJM:
			localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
			message = util.getMailCustomizedMsg(request.getRequestInfo(), osujmNewLocationModel, localizationMessages);
			break;
		}
		message = message.replace("\\n", "\n");
		emailRequests.addAll(util.createEMAILRequest(message, emailIdToOwner));
	}
    
}
