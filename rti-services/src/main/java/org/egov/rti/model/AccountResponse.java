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

public class AccountResponse {
	private JSONObject errors;
	private String title;
	private String status;
	private String token;

}
