package org.egov.bookings.utils;

import org.egov.bookings.config.BookingsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class CalculationUtils.
 */
@Component
public class CalculationUtils {

	/** The config. */
	@Autowired
	private BookingsConfiguration config;
	
	  /**
  	 * Gets the bill generate URI.
  	 *
  	 * @return the bill generate URI
  	 */
  	public String getBillGenerateURI(){
	        StringBuilder url = new StringBuilder(config.getBillingHost());
	        url.append(config.getBillGenerateEndpoint());
	        url.append("?");
	        url.append("tenantId=");
	        url.append("{1}");
	        url.append("&");
	        url.append("consumerCode=");
	        url.append("{2}");
	        url.append("&");
	        url.append("businessService=");
	        url.append("{3}");

	        return url.toString();
	    }
	
	
}
