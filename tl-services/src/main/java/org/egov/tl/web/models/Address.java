package org.egov.tl.web.models;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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

        @Size(max=64)
        @JsonProperty("id")
        private String id;

        @Size(max=64)
        @JsonProperty("tenantId")
        private String tenantId;

        @Size(max=64)
        @JsonProperty("doorNo")
        private String doorNo;

        @JsonProperty("latitude")
        private Double latitude;

        @JsonProperty("longitude")
        private Double longitude;

        @Size(max=64)
        @JsonProperty("addressId")
        private String addressId;

        @Size(max=64)
        @JsonProperty("addressNumber")
        private String addressNumber;

        @Size(max=64)
        @JsonProperty("type")
        private String type;
        
        @Size(max=256)
        @JsonProperty("addressLine1")
        private String addressLine1;

        @Size(max=256)
        @JsonProperty("addressLine2")
        private String addressLine2;

        @Size(max=64)
        @JsonProperty("landmark")
        private String landmark;

        @Size(max=64)
        @JsonProperty("city")
        private String city;

        @Size(max=64)
        @JsonProperty("pincode")
        private String pincode;

        @Size(max=64)
        @JsonProperty("detail")
        private String detail;

        @Size(max=64)
        @JsonProperty("buildingName")
        private String buildingName;

        @Size(max=64)
        @JsonProperty("street")
        private String street;

        @Valid
        @JsonProperty("locality")
        private Boundary locality;


}

