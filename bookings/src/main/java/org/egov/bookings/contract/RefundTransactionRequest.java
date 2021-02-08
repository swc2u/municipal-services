package org.egov.bookings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Setter
@NoArgsConstructor
public class RefundTransactionRequest {

	@JsonProperty("RequestInfo")
	private org.egov.common.contract.request.RequestInfo requestInfo;

	@JsonProperty("RefundTransaction")
	private RefundTransaction refundTransaction;
}
