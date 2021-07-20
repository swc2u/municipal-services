package org.egov.cpt.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfSearchCriteria {

	@Builder.Default
	private String tenantId = "ch.chandigarh";

	private String key;

}
