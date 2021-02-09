package org.egov.bookings.contract;

import java.util.List;

import javax.validation.Valid;

import org.egov.bookings.model.BookingsModel;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookingsRequestKafka {

	@JsonProperty("Booking")
	@Valid
	private List<BookingsModel> bookingsModel;

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

}
