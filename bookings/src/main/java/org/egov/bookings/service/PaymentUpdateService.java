package org.egov.bookings.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.collection.PaymentDetail;
import org.egov.bookings.web.models.collection.PaymentRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentUpdateService {

	@Autowired
	private BookingsConfiguration config;
	
	@Autowired
	private BookingsRepository bookingRepository;
	
	@Autowired
	private BookingsService bookingsService;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WorkflowIntegrator wfIntegrator;


	/**
	 * After payment change the application status
	 *
	 * @param record
	 *            payment request
	 */
	public void process(HashMap<String, Object> record) {
		try {
			System.out.println("I am in process method of Payment Update Service");
			PaymentRequest paymentRequest = mapper.convertValue(record, PaymentRequest.class);
			boolean isServiceMatched = false;

//			for (PaymentDetail paymentDetail : paymentRequest.getPayment().getPaymentDetails()) {
//				System.out.println("Going to look for Buisness service");
//				if (BookingsConstants.BUSINESS_SERVICE_OSBM_PAYMENT.equals(paymentDetail.getBusinessService())) {
//					System.out.println("Now i got the business service as: "+paymentDetail.getBusinessService());
//					isServiceMatched = true;
//				}				
//			}
//			if (!isServiceMatched)
//				return;
			
			for (PaymentDetail paymentDetail : paymentRequest.getPayment().getPaymentDetails()) {
				log.info("Consuming Business Service: {}", paymentDetail.getBusinessService());
				if (paymentDetail.getBusinessService().equalsIgnoreCase(BookingsConstants.BUSINESS_SERVICE_OSBM_PAYMENT)) {
					System.out.println("Inside if statement...");
					BookingsModel bookingsModel = bookingRepository.findByBkApplicationNumber(paymentDetail.getBill().getConsumerCode());
					BookingsRequest bookingsRequest = new BookingsRequest();
					if(paymentRequest.getRequestInfo().getUserInfo().getUserName()==null){
						System.out.println("Compiling user info...");
						paymentRequest.getRequestInfo().setUserInfo(fetchUser(bookingsModel.getUuid(), paymentRequest.getRequestInfo()));
					}
					bookingsRequest.setRequestInfo(paymentRequest.getRequestInfo());
					bookingsRequest.setBookingsModel(bookingsModel);
					bookingsService.update(bookingsRequest);
				}
			}

		} catch (Exception ex) {
			log.error("Failed to process Payment Update message.", ex);
		}
	}

	private User fetchUser(String uuid, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		uri.append(config.getUserHost()).append(config.getUserSearchEndpoint());
		Map<String, Object> userSearchRequest = new HashMap<>();
		List<String> uuidList = Arrays.asList(uuid);
		userSearchRequest.put("RequestInfo", requestInfo);
		userSearchRequest.put("uuid", uuidList);
		Object response = serviceRequestRepository.fetchResult(uri, userSearchRequest);
		List<Object> users = new ArrayList<>();
		try {
			DocumentContext context = JsonPath.parse(mapper.writeValueAsString(response));
			users = context.read("$.user");
		} catch (JsonProcessingException e) {
			log.error("error occurred while parsing user info", e);
		}
		if (CollectionUtils.isEmpty(users)) {
			throw new CustomException("INVALID_SEARCH_ON_USER", "No user found on given criteria!!!");
		}
		return mapper.convertValue(users.get(0), User.class);
	}	

}
