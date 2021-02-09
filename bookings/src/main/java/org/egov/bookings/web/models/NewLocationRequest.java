package org.egov.bookings.web.models;

import javax.validation.Valid;

import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewLocationRequest {

	
	@JsonProperty("NewLocationDetails")
	@Valid
	private OsujmNewLocationModel newLocationModel = null;

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	
}
