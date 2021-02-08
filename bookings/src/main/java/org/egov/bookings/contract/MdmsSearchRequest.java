package org.egov.bookings.contract;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmsSearchRequest {
		
	/** The request info. */
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	/**
	 * Gets the request info.
	 *
	 * @return the request info
	 */
	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	/**
	 * Sets the request info.
	 *
	 * @param requestInfo the new request info
	 */
	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}
}
