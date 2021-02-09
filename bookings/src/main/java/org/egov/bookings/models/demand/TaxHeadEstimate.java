package org.egov.bookings.models.demand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.egov.bookings.contract.TaxHeadMasterFields;
import org.egov.demands.model.enums.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxHeadEstimate {

    private String taxHeadCode;

    private BigDecimal estimateAmount;

    private Category category;
    
   // List<TaxHeadMasterFields> taxHeadMasterFields;

	/*public TaxHeadEstimate(String taxHeadCode, BigDecimal estimateAmount, Category category) {
		super();
		this.taxHeadCode = taxHeadCode;
		this.estimateAmount = estimateAmount;
		this.category = category;
	}*/
    
    
}
