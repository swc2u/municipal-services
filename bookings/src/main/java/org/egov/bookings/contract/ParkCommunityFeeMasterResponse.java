package org.egov.bookings.contract;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkCommunityFeeMasterResponse implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	private BigDecimal cgstAmount;
	private BigDecimal ugstAmount;
	private BigDecimal amount;
	private BigDecimal totalAmount;
	private BigDecimal surchargeAmount;
}
