package org.egov.pm.web.models.collection;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Collection of audit related fields used by most models
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDetails   {

        @Size(max=64)
        @JsonProperty("createdBy")
        private String createdBy = null;

        @Size(max=64)
        @JsonProperty("lastModifiedBy")
        private String lastModifiedBy = null;

        @JsonProperty("createdTime")
        private Long createdTime = null;

        @JsonProperty("lastModifiedTime")
        private Long lastModifiedTime = null;


}

