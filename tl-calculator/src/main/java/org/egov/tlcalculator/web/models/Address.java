package org.egov.tlcalculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representation of a address. Indiavidual APIs may choose to extend from this using allOf if more details needed to be added in their case.
 */
@ApiModel(description = "Representation of a address. Indiavidual APIs may choose to extend from this using allOf if more details needed to be added in their case. ")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-18T17:06:11.263+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address   {

        @JsonProperty("id")
        private String id;

        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("doorNo")
        private String doorNo;

        @JsonProperty("latitude")
        private Double latitude;

        @JsonProperty("longitude")
        private Double longitude;

        @JsonProperty("addressId")
        private String addressId;

        @JsonProperty("addressNumber")
        private String addressNumber;

        @JsonProperty("type")
        private String type;

        @JsonProperty("addressLine1")
        private String addressLine1;

        @JsonProperty("addressLine2")
        private String addressLine2;

        @JsonProperty("landmark")
        private String landmark;

        @JsonProperty("city")
        private String city;

        @JsonProperty("pincode")
        private String pincode;

        @JsonProperty("detail")
        private String detail;

        @JsonProperty("buildingName")
        private String buildingName;

        @JsonProperty("street")
        private String street;

        @JsonProperty("locality")
        private Boundary locality;


}

