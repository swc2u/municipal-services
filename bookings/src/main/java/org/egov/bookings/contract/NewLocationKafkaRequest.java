package org.egov.bookings.contract;

import java.util.List;

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
public class NewLocationKafkaRequest {

	
	@JsonProperty("NewLocationDetails")
	@Valid
	private List<OsujmNewLocationModel> newLocationModel = null;

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	
}
