package org.egov.bookings.contract;

import java.math.BigDecimal;

import org.egov.demands.model.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxHeadMasterFields {

	
	private Category category;
	
	private String service;
	
	private String name;
	
	private String code;
	
	private boolean isDebit;
	
	private boolean isActualDemand;
	
	private BigDecimal order;
	
	private boolean isRequired;
	
	
	private BigDecimal taxAmount;
	
	private BigDecimal facilitationCharge;
	
	
}
