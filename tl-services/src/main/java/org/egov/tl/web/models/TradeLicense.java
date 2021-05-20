package org.egov.tl.web.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.egov.tl.web.models.calculation.Calculation;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;

/**
 * A Object holds the basic data for a Trade License
 */
@ApiModel(description = "A Object holds the basic data for a Trade License")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-18T17:06:11.263+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  TradeLicense   {
        @Size(max=64)
        @JsonProperty("id")
        private String id;

        @NotNull
        @Size(max=64)
        @JsonProperty("tenantId")
        private String tenantId;

        public enum ApplicationTypeEnum {
            NEW("New"),
            
            RENEW("Renew");

            private String value;

            ApplicationTypeEnum(String value) {
              this.value = value;
            }

            @Override
            @JsonValue
            public String toString() {
              return String.valueOf(value);
            }

            @JsonCreator
            public static ApplicationTypeEnum fromValue(String text) {
              for (ApplicationTypeEnum b : ApplicationTypeEnum.values()) {
                if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                  return b;
                }
              }
              return null;
            }
          }
        
              /**
   * Unique Identifier of the Trade License (UUID)
   */
  public enum LicenseTypeEnum {
    TEMPORARY("TEMPORARY"),
    
    PERMANENT("PERMANENT");

    private String value;

    LicenseTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static LicenseTypeEnum fromValue(String text) {
      for (LicenseTypeEnum b : LicenseTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

        @JsonProperty("businessService")
        private String businessService;

        @JsonProperty("licenseType")
        private LicenseTypeEnum licenseType;
        
        @JsonProperty("applicationType")
        private ApplicationTypeEnum applicationType;

        @Size(max=64)
        @JsonProperty("licenseNumber")
        private String licenseNumber;

        @Size(max=64)
        @JsonProperty("applicationNumber")
        private String applicationNumber;

        @Size(max=64)
        @JsonProperty("oldLicenseNumber")
        private String oldLicenseNumber;


        @Size(max=256)
        @JsonProperty("propertyId")
        private String propertyId;

        @Size(max=64)
        @JsonProperty("oldPropertyId")
        private String oldPropertyId;

        @Size(max=64)
        @JsonProperty("accountId")
        private String accountId;

        @Size(max=256)
        @JsonProperty("tradeName")
        private String tradeName;

        @JsonProperty("applicationDate")
        private Long applicationDate;

        @JsonProperty("commencementDate")
        private Long commencementDate;

        @JsonProperty("issuedDate")
        private Long issuedDate;

        @Size(max=64)
        @JsonProperty("financialYear")
        private String financialYear;

        @JsonProperty("validFrom")
        private Long validFrom;

        @JsonProperty("validTo")
        private Long validTo;

              /**
   * 1. Perform action to change the state of the trade license. 2. INITIATE, if application is getting submitted without required document. 3. APPLY, if application is getting submitted with application documents, in that case api will validate all the required application document. 4. APPROVE action is only applicable for specific role, that role has to be configurable at service level. Employee can approve a application only if application is in APPLIED state and Licesance fees is paid.
   */

        @NotNull
        @Size(max=64)
        @JsonProperty("action")
        private String action;

        @JsonProperty("assignee")
        private List<String> assignee;

        @Valid
        @JsonProperty("wfDocuments")
        private List<Document> wfDocuments;

        @Size(max=64)
        @JsonProperty("status")
        private String status;

        @Valid
        @NotNull
        @JsonProperty("tradeLicenseDetail")
        private TradeLicenseDetail tradeLicenseDetail;

        @JsonProperty("calculation")
        private Calculation calculation;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails;

        @Size(max=128)
        private String comment;

}

