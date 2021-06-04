package org.egov.bookings.contract;

import java.util.List;

import javax.validation.Valid;

import org.egov.bookings.model.BookingLockModel;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingLockRequest {
	@JsonProperty("BookingLock")
	@Valid
	private List<BookingLockModel> bookingLockModel;

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

}
