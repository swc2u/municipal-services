package org.egov.ps.web.contracts;

import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationStatesResponse {

    @JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("BusinessServices")
	@Valid
	private List<State> states;

	@JsonProperty("Status")
	@Valid
	private HashSet<String> status;
}