package org.egov.bookings.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterRequest {
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 7816338338999208980L;
	
	/** The approver list. */
	@JsonProperty("ApproverDetails")
	private List<CommonMasterFields> approverList;
	
	/** The osbm fee list. */
	@JsonProperty("OsbmFeeDetails")
	private List<CommonMasterFields> osbmFeeList;
	
	/** The osujm fee list. */
	@JsonProperty("OsujmFeeDetails")
	private List<CommonMasterFields> osujmFeeList;
	
	/** The gfcp fee list. */
	@JsonProperty("GfcpFeeDetails")
	private List<CommonMasterFields> gfcpFeeList;
	
	/** The pacc fee list. */
	@JsonProperty("PaccFeeDetails")
	private List<CommonMasterFields> paccFeeList;
	
	/** The community center room fee list. */
	@JsonProperty("CommunityCenterRoomFeeDetails")
	private List<CommonMasterFields> communityCenterRoomFeeList;
}
