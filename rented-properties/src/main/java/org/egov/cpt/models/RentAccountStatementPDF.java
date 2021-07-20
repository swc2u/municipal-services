package org.egov.cpt.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class RentAccountStatementPDF {
    /*
     * Demand/Payment generation date
     */
    private String date;
    /*
     * Demand/Payment amount
     */
    private String amount;

    private Type type;
    
    private String typeR;
    
    private String typeP;
    
    private String dueAmount;

    private String remainingPrincipal;

    private String remainingInterest;


    @JsonProperty("remainingBalance")
    private String remainingBalance;
    
	@JsonProperty("receiptNo")
	private String receiptNo;



    public enum Type {
        C("C"), D("D");

        private String value;

        Type(String value) {
            this.value = value;
        }
}
}