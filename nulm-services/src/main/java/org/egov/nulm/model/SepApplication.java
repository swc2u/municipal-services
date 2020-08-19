package org.egov.nulm.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SepApplication {
	
	private String applicationUuid ;
	
	private String applicationId ;
	
	private String nulmApplicationId ;
	
	@NotNull
	@JsonProperty("applicationStatus")
	private StatusEnum applicationStatus ;

	public enum StatusEnum {
	    DRAFTED("DRAFTED"),
	    CREATED("CREATED"),
	    APPROVED("APPROVED"),
		REJECTED("REJECTED");

	    private String value;

	    StatusEnum(String value) {
	      this.value = value;
	    }

	    @Override
	    @JsonValue
	    public String toString() {
	      return String.valueOf(value);
	    }

	    @JsonCreator
	    public static StatusEnum fromValue(String text) {
	      for (StatusEnum b : StatusEnum.values()) {
	        if (String.valueOf(b.value).equals(text)) {
	          return b;
	        }
	      }
	      return null;
	    }
	  }
	@NotNull
	@JsonProperty("tenantId")
	private String tenantId ;
	
	
	@JsonProperty("name")
	private String name ;
	
	
	@JsonProperty("gender")
	private String gender ;
	
	
	@JsonProperty("age")
	private Integer age ;
	
	
	@JsonProperty("dob")
	private String dob ;
	
	
	@JsonProperty("adharNo")
	private String adharNo ;
	
	
	@JsonProperty("motherName")
	private String motherName ;
	
	
	@JsonProperty("fatherOrHusbandName")
	private String fatherOrHusbandName ;
	
	
	@JsonProperty("occupation")
	private String occupation ;
	
	
	@JsonProperty("address")
	private String address ;
	
	
	@JsonProperty("contact")
	private String contact ;
	
	
	@JsonProperty("sinceHowLongInChandigarh")
	private String sinceHowLongInChandigarh ;
	
	
	@JsonProperty("qualification")
	private String qualification ;	
	
	
	@JsonProperty("category")
	private String category ;
	
	
	@JsonProperty("isUrbanPoor")
	private Boolean isUrbanPoor ;
	
	@JsonProperty("bplNo")
	private String bplNo ;	
	
	
	@JsonProperty("isMinority")
	private Boolean isMinority ;
	
	@JsonProperty("minority")
	private String minority ;
	
	
	@JsonProperty("isHandicapped")
	private Boolean isHandicapped ;
	
	
	@JsonProperty("typeOfBusinessToBeStarted")
	private String typeOfBusinessToBeStarted ;
	
	
	@JsonProperty("previousExperience")
	private String previousExperience ;
	
	
	@JsonProperty("placeOfWork")
	private String placeOfWork ;
	
	
	@JsonProperty("bankDetails")
	private String bankDetails ;
	
	
	@JsonProperty("noOfFamilyMembers")
	private String noOfFamilyMembers ;	
	
	@JsonProperty("projectCost")
	private BigDecimal projectCost ;
	
	
	@JsonProperty("loanAmount")
	private BigDecimal loanAmount ;
	
	
	@JsonProperty("recommendedAmount")
	private BigDecimal recommendedAmount ;
	
	
	@JsonProperty("isLoanFromBankinginstitute")
	private Boolean isLoanFromBankinginstitute ;
	
	
	@JsonProperty("isRepaymentMade")
	private Boolean isRepaymentMade ;
	
	@JsonProperty("recommendedBy")
	private String recommendedBy ;
	
	@JsonProperty("representativeName")
	private String representativeName ;
	
	@JsonProperty("representativeAddress")
	private String representativeAddress ;
	
	@JsonProperty("isActive")
	private Boolean isActive ;
	
	@JsonProperty("applicationDocument")
	private List<SepApplicationDocument> applicationDocument;
	
	@JsonProperty("fromDate")
	private String fromDate;

	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails ;

	
	@JsonProperty("remark")
	private String remark ;
	
	
	


}
