package org.egov.tl.web.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@EqualsAndHashCode
public class Accessory   {

        @Size(max=64)
        @JsonProperty("id")
        private String id;

        @Size(max=64)
        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("active")
        private Boolean active;

        @Size(max=64)
        @JsonProperty("accessoryCategory")
        private String accessoryCategory;

        @Size(max=64)
        @JsonProperty("uom")
        private String uom;

        @Size(max=64)
        @JsonProperty("uomValue")
        private String uomValue;

        @Min(value = 1)
        @JsonProperty("count")
        private Integer count;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails;


}

