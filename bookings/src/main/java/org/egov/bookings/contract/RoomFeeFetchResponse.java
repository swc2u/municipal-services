package org.egov.bookings.contract;

import java.math.BigDecimal;

import org.egov.bookings.contract.RoomFeeFetchRequest.RoomFeeFetchRequestBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RoomFeeFetchResponse {

	BigDecimal amount;

	BigDecimal ugstAndCgst;

	BigDecimal totalAmount;

}
