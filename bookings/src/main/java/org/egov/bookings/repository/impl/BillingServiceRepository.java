package org.egov.bookings.repository.impl;

import java.util.List;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Bill;
import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BillingServiceRepository {

	@Autowired
	private BookingsConfiguration bookingsConfiguration;
	
	@Autowired
	private RestTemplate restTemplate;
	
    public List<Bill> fetchBill(RequestInfo requestInfo, String tenantId, String consumerCode) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("tenantId", tenantId);
       // queryParams.add("billId", StringUtils.join(billIds,","));
        queryParams.add("consumerCode", consumerCode);
        
        String uri = UriComponentsBuilder
                .fromHttpUrl(bookingsConfiguration.getBillingHost())
                .path(bookingsConfiguration.getBillingServiceSearch())
                .queryParams(queryParams)
                .build()
                .toUriString();

        RequestInfoWrapper wrapper = new RequestInfoWrapper(requestInfo);

        try {
            BillResponse response = restTemplate.postForObject(uri, wrapper, BillResponse.class);
            return response.getBill();
        } catch (HttpClientErrorException e) {
            log.error("Unable to fetch bill for Consumer Code: {} in tenant {}", consumerCode, tenantId, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unable to fetch bill for Consumer Code: {} in tenant {}", consumerCode, tenantId, e);
            throw new CustomException("BILLING_SERVICE_ERROR", "Failed to fetch bill, unknown error occurred");
        }
    }
	
	
}
