package org.egov.bookings.finance.contract;

public class FinanceResponseInfo {
	private String api_id;
	private String ver;
	private String ts;
	private String res_msg_id;
	private String msg_id;
	private String status;

	public String getApi_id() {
		return api_id;
	}

	public String getVer() {
		return ver;
	}

	public String getTs() {
		return ts;
	}

	public String getRes_msg_id() {
		return res_msg_id;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public String getStatus() {
		return status;
	}

	// Setter Methods

	public void setApi_id(String api_id) {
		this.api_id = api_id;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public void setRes_msg_id(String res_msg_id) {
		this.res_msg_id = res_msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
