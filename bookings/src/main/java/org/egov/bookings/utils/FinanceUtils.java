package org.egov.bookings.utils;


import org.apache.log4j.Logger;
import org.egov.bookings.config.FinanceConfiguration;
import org.egov.bookings.finance.contract.FinanceAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class FinanceUtils {
	
	private static final Logger LOGGER = Logger.getLogger(FinanceUtils.class);
	
	@Autowired
	private FinanceConfiguration financeConfiguration;

	@Autowired
	private RestTemplate restTemplate;
	
	public FinanceAuthResponse getAccessToken() {
		FinanceAuthResponse result = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("Authorization", financeConfiguration.getKeyAuthorization());
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("grant_type", financeConfiguration.getGrantType());
			params.add("scope", financeConfiguration.getScope());
			params.add("username", financeConfiguration.getUserName());
			params.add("password", financeConfiguration.getIdentification());
			params.add("tenantId", financeConfiguration.getTenantId());
			params.add("userType", financeConfiguration.getUserType());
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
			result = restTemplate.exchange(financeConfiguration.getAuthUrl(),
					HttpMethod.POST, entity, FinanceAuthResponse.class).getBody();
		} catch (Exception e) {
			LOGGER.error("getAccessToken : " + e.getMessage());
		}
		return result;
	}

}
