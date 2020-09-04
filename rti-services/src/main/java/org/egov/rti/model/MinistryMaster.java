package org.egov.rti.model;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

public class MinistryMaster {

	private String ministryCode;
	private String ministryName;
	private String username;
	private String userId;
	private String effectiveFrom;
	private String effectiveTo;
	private String userTenantId;

}
