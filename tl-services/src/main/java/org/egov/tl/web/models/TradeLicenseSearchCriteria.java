package org.egov.tl.web.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLicenseSearchCriteria {


    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("applicationNumber")
    private String applicationNumber;

    @JsonProperty("licenseNumber")
    private String licenseNumber;

    @JsonProperty("oldLicenseNumber")
    private String oldLicenseNumber;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonIgnore
    private String accountId;


    @JsonProperty("fromDate")
    private Long fromDate = null;

    @JsonProperty("toDate")
    private Long toDate = null;

    @JsonProperty("businessService")
    private String businessService;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("limit")
    private Integer limit;
    
    @JsonProperty("tradeValue")
    private String tradeValue;
    
    @JsonProperty("tradeName")
    private String tradeName;
    
    /*@JsonProperty("trade")
    private String trade;*/
    
    @JsonProperty("applicationType")
    private String applicationType;

    @JsonIgnore
    private List<String> ownerIds;
    
    @JsonProperty("sortOrder")
    private String sortOrder;
    
    @JsonProperty("sortBy")
    private String sortBy;

	@JsonIgnore
    private List<String> statusToHideForEmployee;


    public boolean isEmpty() {
        return (this.tenantId == null && this.status == null && this.ids == null && this.applicationNumber == null
                && this.licenseNumber == null && this.oldLicenseNumber == null && this.mobileNumber == null &&
                this.fromDate == null && this.toDate == null && this.ownerIds == null 
                && this.businessService == null && this.applicationType == null && this.sortBy == null && this.sortOrder == null
        );
    }

    public boolean tenantIdOnly() {
        return (this.tenantId != null && this.status == null && this.ids == null && this.applicationNumber == null
                && this.licenseNumber == null && this.oldLicenseNumber == null && this.mobileNumber == null &&
                this.fromDate == null && this.toDate == null && this.ownerIds == null 
                && this.businessService == null && this.applicationType == null && this.sortBy == null && this.sortOrder == null
        );
    }

}
